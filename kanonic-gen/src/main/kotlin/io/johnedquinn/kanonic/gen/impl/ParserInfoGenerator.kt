package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.gen.ClassNames
import io.johnedquinn.kanonic.parse.Node

internal object ParserInfoGenerator {

    /**
     * Generates the Parser File
     */
    public fun generate(grammar: Grammar): FileSpec {
        val packageName = GrammarUtils.getPackageName(grammar)
        val infoClass = Internal.createParserInfoClass(grammar)
        val infoName = GrammarUtils.getMetadataName(grammar)
        return FileSpec.builder(packageName, infoName).also { file ->
            file.addType(infoClass)
        }.build()
    }

    private object Internal {

        public fun createParserInfoClass(grammar: Grammar): TypeSpec {
            val parserInfoName = GrammarUtils.getMetadataName(grammar)
            val infoSpec = TypeSpec.classBuilder(parserInfoName)
            infoSpec.addSuperinterface(ClassNames.PARSER_INFO)
            infoSpec.addFunction(createLambdaInitializerFunction(grammar))
            infoSpec.addProperty(createLambdaFunctions())
            infoSpec.addFunction(createCreateRuleNode())
            return infoSpec.build()
        }

        public fun createLambdaFunctions(): PropertySpec {
            val nodeList = PropertySpec.builder("nodeLambdaList", ClassNames.LIST_CREATE_NODE)
            nodeList.addModifiers(KModifier.PRIVATE)
            nodeList.initializer("initializeLambdas()")
            return nodeList.build()
        }

        // TODO: Adjust returns
        public fun createLambdaInitializerFunction(grammar: Grammar): FunSpec {
            val packageName = GrammarUtils.getPackageName(grammar)
            val grammarNodeName = GrammarUtils.getGrammarNodeName(grammar)
            val funSpec = FunSpec.builder("initializeLambdas")
            funSpec.addModifiers(KModifier.PRIVATE)
            funSpec.returns(ClassNames.LIST_CREATE_NODE)
            funSpec.beginControlFlow("return buildList")
            grammar.rules.groupBy { it.name }.map { (rule, ruleVariants) ->
                ruleVariants.forEach { variant ->
                    val ruleSpec = ClassName(
                        packageName,
                        grammarNodeName,
                        GrammarUtils.getGeneratedClassName(rule),
                        GrammarUtils.getGeneratedClassName(variant.alias)
                    )
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
