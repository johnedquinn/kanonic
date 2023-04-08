package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.grammar.RuleReference
import io.johnedquinn.kanonic.runtime.grammar.SymbolReference
import io.johnedquinn.kanonic.runtime.grammar.TerminalReference

internal object NodeGenerator {

    /**
     * Creates the [FileSpec] containing the Grammar Node and its variants (Rule Nodes and the
     * Rule Variant Nodes).
     */
    public fun generate(grammarSpec: GrammarSpec): FileSpec {
        val packageName = grammarSpec.packageName
        val type = Private.createType(grammarSpec)
        val name = grammarSpec.nodeName
        return FileSpec.builder(packageName, name).also { file ->
            file.addType(type)
        }.build()
    }

    private object Private {

        /**
         * Creates the Grammar Node and adds the Rule Nodes and the Rule Variant Nodes. Each variant should be a
         * top-level Rule (sealed class) containing Rule Variants (data classes).
         */
        public fun createType(spec: GrammarSpec): TypeSpec {
            // Define the top-level grammar Node
            val className = spec.nodeName
            val type = TypeSpec.classBuilder(className)
            type.addModifiers(KModifier.SEALED)
            type.superclass(ClassNames.NODE)
            type.addSuperclassConstructorParameter(CodeBlock.of("state"))
            type.addSuperclassConstructorParameter(CodeBlock.of("children"))
            type.addSuperclassConstructorParameter(CodeBlock.of("parent"))
            type.primaryConstructor(createPrimaryConstructor())

            // Add Variant Definitions
            val ruleSpecs = createNodes(spec)
            ruleSpecs.forEach { rule -> type.addType(rule) }
            return type.build()
        }

        /**
         * Creates the Rule Nodes and the Rule Variant Nodes
         */
        private fun createNodes(spec: GrammarSpec): List<TypeSpec> {
            return spec.rules.mapNotNull { rule ->
                if (rule.generated) {
                    return@mapNotNull null
                }
                // Shared Information
                val ruleClassName = GrammarUtils.getGeneratedClassName(rule.name)
                val packageName = spec.packageName
                val grammarNodeName = spec.nodeName

                // Create Rule Variants (Data Classes)
                val ruleClassReference = ClassName(packageName, grammarNodeName, ruleClassName)
                val typeSpecs = rule.variants.map { variant ->
                    val variantClassName = GrammarUtils.getGeneratedClassName(variant.originalName)
                    val variantSpec = TypeSpec.classBuilder(variantClassName)
                    variantSpec.addModifiers(KModifier.DATA)
                    variantSpec.addToString()
                    variantSpec.superclass(ruleClassReference)
                    // TODO: This is a work-around
                    val variantSpecification = spec.rules.flatMap { it.variants }.firstOrNull {
                        it.originalName == variant.originalName
                    } ?: error("Couldn't find variant ${variant.originalName}")
                    variantSpec.addChildrenFunctions(variantSpecification, spec)
                    variantSpec.addApplyMethod(spec, variantSpecification)
                    variantSpec.addPrimaryConstructor()
                    variantSpec.build()
                }

                // Create Top-Level Rule (Abstract Class)
                val ruleSpec = TypeSpec.classBuilder(ruleClassName)
                val grammarNodeReference = ClassNames.createGrammarNodeClass(packageName, spec.nodeName)
                ruleSpec.addModifiers(KModifier.SEALED)
                ruleSpec.superclass(grammarNodeReference)
                ruleSpec.addPrimaryConstructor()
                ruleSpec.addTypes(typeSpecs)
                ruleSpec.build()
            }
        }

        private fun TypeSpec.Builder.addChildrenFunctions(variant: VariantSpec, spec: GrammarSpec) {
            val itemCounts = variant.implicitItems.groupingBy { it }.eachCount()
            itemCounts.entries.forEach { entry ->
                when (entry.value) {
                    // 0 -> this.addChildrenFunctionSingle(entry.key, grammar, spec)
                    0 -> this.addChildrenFunctionMultiple(entry.key, spec)
                    else -> this.addChildrenFunctionMultiple(entry.key, spec)
                }
            }
        }

        private fun TypeSpec.Builder.addChildrenFunctionSingle(symbol: SymbolReference, spec: GrammarSpec) {
            val name = getName(symbol, spec)
            val function = FunSpec.builder(name)
            spec.rules.firstOrNull { it.name == name }?.className?.let {
                function.returns(it)
                function.addStatement("return this.children.filterIsInstance<%L>().first()", it)
            } ?: spec.tokens.firstOrNull { it.name == name }?.let {
                function.returns(ClassNames.TERMINAL_NODE)
                function.beginControlFlow("return this.children.filterIsInstance<%L>().first", ClassNames.TERMINAL_NODE)
                function.addStatement("it.token.type == %L", it.index)
                function.endControlFlow()
            } ?: error("Unable to find rule/token reference")
            this.addFunction(function.build())
        }

        private fun TypeSpec.Builder.addChildrenFunctionMultiple(symbol: SymbolReference, spec: GrammarSpec) {
            val name = getName(symbol, spec)
            val normalizedName = GrammarUtils.getNormalizedCamelCaseName(name)
            val function = FunSpec.builder(normalizedName)
            spec.rules.firstOrNull { it.name == name }?.className?.let {
                function.returns(ClassNames.LIST.parameterizedBy(it))
                function.addStatement("return this.children.filterIsInstance<%L>()", it)
            } ?: spec.tokens.firstOrNull { it.name == name }?.let {
                function.returns(ClassNames.LIST_TERMINAL_NODE)
                function.beginControlFlow(
                    "return this.children.filterIsInstance<%L>().filter",
                    ClassNames.TERMINAL_NODE
                )
                function.addStatement("it.token.type == %L", it.index)
                function.endControlFlow()
            } ?: error("Unable to find rule/token reference")
            this.addFunction(function.build())
        }

        private fun getName(symbol: SymbolReference, spec: GrammarSpec) = when (symbol) {
            is RuleReference -> symbol.name
            is TerminalReference -> spec.tokens[symbol.type].name
        }

        private fun createAcceptFunction(spec: GrammarSpec, variantSpec: VariantSpec) = FunSpec.builder("accept")
            .addTypeVariable(TypeVariableName("R"))
            .addTypeVariable(TypeVariableName("C"))
            .addParameter(
                "visitor",
                ClassNames.NODE_VISITOR.parameterizedBy(TypeVariableName("R"), TypeVariableName("C"))
            )
            .addParameter("ctx", TypeVariableName("C"))
            .beginControlFlow("return when (visitor)")
            .addStatement("is %T -> visitor.%L(this, ctx)", spec.visitorClassName, variantSpec.visitMethodName)
            .addStatement("else -> visitor.visit(this, ctx)")
            .endControlFlow()
            .addModifiers(KModifier.OVERRIDE)
            .returns(TypeVariableName("R"))
            .build()

        private fun TypeSpec.Builder.addApplyMethod(grammarSpec: GrammarSpec, variantSpec: VariantSpec) =
            this.apply {
                val accept = createAcceptFunction(grammarSpec, variantSpec)
                addFunction(accept)
            }

        private fun createPrimaryConstructor() = FunSpec.constructorBuilder()
            .addParameter("state", Int::class)
            .addParameter("children", ClassNames.LIST_NODE)
            .addParameter("parent", Node::class.asTypeName().copy(nullable = true))
            .build()

        private fun TypeSpec.Builder.addPrimaryConstructor() = this.apply {
            this.primaryConstructor(createPrimaryConstructor())
            this.addProperty(
                PropertySpec.builder("state", Int::class, KModifier.OVERRIDE).initializer("state").build()
            )
            this.addProperty(
                PropertySpec.builder("children", ClassNames.LIST_NODE, KModifier.OVERRIDE).initializer("children")
                    .build()
            )
            this.addProperty(
                PropertySpec.builder(
                    "parent",
                    Node::class.asTypeName().copy(nullable = true),
                    KModifier.OVERRIDE
                ).mutable(true).initializer("parent").build()
            )
            this.addSuperclassConstructorParameter(CodeBlock.of("state"))
            this.addSuperclassConstructorParameter(CodeBlock.of("children"))
            this.addSuperclassConstructorParameter(CodeBlock.of("parent"))
        }

        private fun TypeSpec.Builder.addToString() = this.apply {
            this.addFunction(
                FunSpec.builder("toString")
                    .addCode(
                        CodeBlock.of(
                            "return \"\"\"%L(state: %L, children: %L)\"\"\"",
                            "\${this::class.simpleName}",
                            "\$state",
                            "\$children",
                        )
                    )
                    .returns(String::class)
                    .addModifiers(KModifier.OVERRIDE)
                    .build()
            )
        }
    }
}
