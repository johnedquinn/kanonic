package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.gen.GrammarSpec
import io.johnedquinn.kanonic.machine.ParseTableSerializer
import io.johnedquinn.kanonic.parse.Node

internal object MetadataGenerator {

    /**
     * Generates the Parser File
     */
    public fun generate(grammarSpec: GrammarSpec): FileSpec {
        val packageName = grammarSpec.packageName
        val infoClass = Internal.createParserInfoClass(grammarSpec)
        val infoName = grammarSpec.metadataName
        return FileSpec.builder(packageName, infoName).also { file ->
            file.addImport(ClassNames.GRAMMAR_BUILDER_COMPANION, "buildGrammar")
            file.addImport(ClassNames.RULE_BUILDER_COMPANION, "buildRule")
            if (grammarSpec.rules.any { it.generated }) {
                file.addImport(ClassNames.RULE_BUILDER_COMPANION, "buildGeneratedRule")
            }
            file.addType(infoClass)
        }.build()
    }

    private object Internal {

        public fun createParserInfoClass(grammarSpec: GrammarSpec): TypeSpec {
            val parserInfoName = grammarSpec.metadataName
            val infoSpec = TypeSpec.classBuilder(parserInfoName)
            infoSpec.addSuperinterface(ClassNames.PARSER_INFO)
            infoSpec.addProperty(createGrammarVariable(grammarSpec))
            infoSpec.addFunction(createTableFunction(grammarSpec))
            infoSpec.addFunction(createLambdaInitializerFunction(grammarSpec))
            infoSpec.addProperty(createLambdaFunctions())
            infoSpec.addFunction(createCreateRuleNode())
            return infoSpec.build()
        }

        public fun createTableFunction(grammarSpec: GrammarSpec): FunSpec {
            val serialized = ParseTableSerializer.serialize(grammarSpec.table)
            val func = FunSpec.builder("getTable")
            func.addModifiers(KModifier.OVERRIDE)
            func.returns(String::class)
            func.addStatement("return \"\"\"%L\"\"\"", serialized)
            return func.build()
        }

        public fun createGrammarVariable(grammarSpec: GrammarSpec): PropertySpec {
            val block = CodeBlock.builder()
            block.beginControlFlow("buildGrammar(%L, %L)", "\"${grammarSpec.nodeName}\"", "\"${grammarSpec.start.name}\"")
            block.addStatement("packageName(%L)", "\"${grammarSpec.packageName}\"")
            block.beginControlFlow("tokens")
            // Skip EOF and EPSILON
            grammarSpec.tokens.subList(2, grammarSpec.tokens.size).forEach { token ->
                // TODO: Re-escape everything
                val escapedDef = token.def.replace("\\", "\\\\").replace("\"", "\\\"")
                val def = when (token.hidden) {
                    true -> "(\"$escapedDef\" channel \"hidden\")"
                    false -> "\"$escapedDef\""
                }
                block.addStatement("\"${token.name}\" - $def")
            }
            block.endControlFlow()
            grammarSpec.rules.forEach { rule ->
                val function = when (rule.generated) {
                    true -> "buildGeneratedRule"
                    false -> "buildRule"
                }
                block.beginControlFlow("\"%L\"·eq·%L(this,·\"%L\")", rule.name, function, rule.name)
                rule.variants.forEach { variant ->
                    val itemsList = variant.items.map { item ->
                        "\"${item.getName(grammarSpec)}\""
                    }
                    val itemsSubStr = itemsList.joinToString("·-·")
                    // ruleName" eq "ruleRef" - "TOKEN" - "TOKEN" - "someOtherRef" --> alias
                    block.addStatement("\"%L\"·eq·%L", variant.originalName, itemsSubStr)
                }
                block.endControlFlow()
            }
            block.endControlFlow()
            return PropertySpec.builder("grammar", ClassNames.GRAMMAR).addModifiers(KModifier.OVERRIDE).initializer(block.build()).build()
        }

        private fun SymbolReference.getName(grammarSpec: GrammarSpec): String = when (this) {
            is TerminalReference -> grammarSpec.tokens[this.type].name
            is RuleReference -> this.name
        }

        public fun createLambdaFunctions(): PropertySpec {
            val nodeList = PropertySpec.builder("nodeLambdaList", ClassNames.LIST_CREATE_NODE)
            nodeList.addModifiers(KModifier.PRIVATE)
            nodeList.initializer("initializeLambdas()")
            return nodeList.build()
        }

        // TODO: Adjust returns
        public fun createLambdaInitializerFunction(grammarSpec: GrammarSpec): FunSpec {
            val packageName = grammarSpec.packageName
            val grammarNodeName = grammarSpec.nodeName
            val funSpec = FunSpec.builder("initializeLambdas")
            funSpec.addModifiers(KModifier.PRIVATE)
            funSpec.returns(ClassNames.LIST_CREATE_NODE)
            funSpec.beginControlFlow("return buildList")
            grammarSpec.rules.map { rule ->
                rule.variants.forEach { variant ->
                    val ruleSpec = when (rule.generated) {
                        true -> ClassNames.GENERATED_NODE
                        false -> ClassName(
                            packageName,
                            grammarNodeName,
                            GrammarUtils.getGeneratedClassName(rule.name),
                            GrammarUtils.getGeneratedClassName(variant.originalName)
                        )
                    }
                    funSpec.addStatement(
                        "add(CreateNode·{·state,·children,·parent·->·%T(state,·children,·parent)·})",
                        ruleSpec
                    )
                }
            }
            funSpec.endControlFlow()
            return funSpec.build()
        }

        public fun createCreateRuleNode(): FunSpec {
            val func = FunSpec.builder("createRuleNode")
            func.returns(ClassNames.NODE)
            func.addModifiers(KModifier.OVERRIDE)
            func.addParameter("index", Int::class)
            func.addParameter("state", Int::class)
            func.addParameter("children", ClassNames.LIST_NODE)
            func.addParameter("parent", Node::class.asTypeName().copy(nullable = true))
            func.addStatement("val nodeCreator = nodeLambdaList[index]")
            func.addStatement("return nodeCreator.create(state, children, parent)")
            return func.build()
        }
    }
}
