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
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.gen.GrammarSpec
import io.johnedquinn.kanonic.gen.VariantSpec
import io.johnedquinn.kanonic.parse.Node

internal object NodeGenerator {

    /**
     * Creates the [FileSpec] containing the Grammar Node and its variants (Rule Nodes and the
     * Rule Variant Nodes).
     */
    public fun generate(grammar: Grammar, spec: GrammarSpec): FileSpec {
        val packageName = GrammarUtils.getPackageName(grammar)
        val type = Private.createType(grammar, spec)
        val name = GrammarUtils.getGrammarNodeName(grammar)
        return FileSpec.builder(packageName, name).also { file ->
            file.addType(type)
        }.build()
    }

    private object Private {

        /**
         * Creates the Grammar Node and adds the Rule Nodes and the Rule Variant Nodes. Each variant should be a
         * top-level Rule (sealed class) containing Rule Variants (data classes).
         */
        public fun createType(grammar: Grammar, spec: GrammarSpec): TypeSpec {
            // Define the top-level grammar Node
            val className = GrammarUtils.getGeneratedClassName(grammar.options.grammarName)
            val type = TypeSpec.classBuilder(className)
            type.addModifiers(KModifier.SEALED)
            type.superclass(ClassNames.NODE)
            type.addSuperclassConstructorParameter(CodeBlock.of("state"))
            type.addSuperclassConstructorParameter(CodeBlock.of("children"))
            type.addSuperclassConstructorParameter(CodeBlock.of("parent"))
            type.primaryConstructor(createPrimaryConstructor())

            // Add Variant Definitions
            val ruleSpecs = createNodes(grammar, spec)
            ruleSpecs.forEach { rule -> type.addType(rule) }
            return type.build()
        }

        /**
         * Creates the Rule Nodes and the Rule Variant Nodes
         */
        private fun createNodes(grammar: Grammar, spec: GrammarSpec): List<TypeSpec> {
            return grammar.rules.mapNotNull { rule ->
                if (rule.generated) {
                    return@mapNotNull null
                }
                // Shared Information
                val ruleClassName = GrammarUtils.getGeneratedClassName(rule.name)
                val packageName = GrammarUtils.getPackageName(grammar)
                val grammarNodeName = GrammarUtils.getGrammarNodeName(grammar)

                // Create Rule Variants (Data Classes)
                val ruleClassReference = ClassName(packageName, grammarNodeName, ruleClassName)
                val typeSpecs = rule.variants.map { variant ->
                    val variantClassName = GrammarUtils.getGeneratedClassName(variant.name)
                    val variantSpec = TypeSpec.classBuilder(variantClassName)
                    variantSpec.addModifiers(KModifier.DATA)
                    variantSpec.addToString()
                    variantSpec.superclass(ruleClassReference)
                    // TODO: This is a work-around
                    val variantSpecification = spec.rules.flatMap { it.variants }.firstOrNull {
                        it.originalName == variant.name
                    } ?: error("Couldn't find variant ${variant.name}")
                    variantSpec.addChildrenFunctions(variantSpecification, grammar, spec)
                    variantSpec.addApplyMethod(spec, variantSpecification)
                    variantSpec.addPrimaryConstructor()
                    variantSpec.build()
                }

                // Create Top-Level Rule (Abstract Class)
                val ruleSpec = TypeSpec.classBuilder(ruleClassName)
                val grammarNodeReference = ClassNames.createGrammarNodeClass(packageName, grammar.options.grammarName)
                ruleSpec.addModifiers(KModifier.SEALED)
                ruleSpec.superclass(grammarNodeReference)
                ruleSpec.addPrimaryConstructor()
                ruleSpec.addTypes(typeSpecs)
                ruleSpec.build()
            }
        }

        private fun TypeSpec.Builder.addChildrenFunctions(variant: VariantSpec, grammar: Grammar, spec: GrammarSpec) {
            val itemCounts = variant.implicitItems.groupingBy { it }.eachCount()
            itemCounts.entries.forEach { entry ->
                when (entry.value) {
                    // 0 -> this.addChildrenFunctionSingle(entry.key, grammar, spec)
                    0 -> this.addChildrenFunctionMultiple(entry.key, grammar, spec)
                    else -> this.addChildrenFunctionMultiple(entry.key, grammar, spec)
                }
            }
        }

        private fun TypeSpec.Builder.addChildrenFunctionSingle(symbol: SymbolReference, grammar: Grammar, spec: GrammarSpec) {
            val name = getName(symbol, grammar)
            val function = FunSpec.builder(name)
            spec.rules.firstOrNull { it.name == name }?.className?.let {
                function.returns(it)
                function.addStatement("return this.children.filterIsInstance<%L>().first()", it)
            } ?: grammar.tokens.firstOrNull { it.name == name }?.let {
                function.returns(ClassNames.TERMINAL_NODE)
                function.beginControlFlow("return this.children.filterIsInstance<%L>().first", ClassNames.TERMINAL_NODE)
                function.addStatement("it.token.type == %L", it.index)
                function.endControlFlow()
            } ?: error("Unable to find rule/token reference")
            this.addFunction(function.build())
        }
        private fun TypeSpec.Builder.addChildrenFunctionMultiple(symbol: SymbolReference, grammar: Grammar, spec: GrammarSpec) {
            val name = getName(symbol, grammar)
            val function = FunSpec.builder(name)
            spec.rules.firstOrNull { it.name == name }?.className?.let {
                function.returns(ClassNames.LIST.parameterizedBy(it))
                function.addStatement("return this.children.filterIsInstance<%L>()", it)
            } ?: grammar.tokens.firstOrNull { it.name == name }?.let {
                function.returns(ClassNames.LIST_TERMINAL_NODE)
                function.beginControlFlow("return this.children.filterIsInstance<%L>().filter", ClassNames.TERMINAL_NODE)
                function.addStatement("it.token.type == %L", it.index)
                function.endControlFlow()
            } ?: error("Unable to find rule/token reference")
            this.addFunction(function.build())
        }

        private fun getName(symbol: SymbolReference, grammar: Grammar) = when (symbol) {
            is RuleReference -> symbol.name
            is TerminalReference -> grammar.tokens[symbol.type].name
        }

        private fun createAcceptFunction(grammar: GrammarSpec, variantSpec: VariantSpec) = FunSpec.builder("accept")
            .addTypeVariable(TypeVariableName("R"))
            .addTypeVariable(TypeVariableName("C"))
            .addParameter("visitor", ClassNames.NODE_VISITOR.parameterizedBy(TypeVariableName("R"), TypeVariableName("C")))
            .addParameter("ctx", TypeVariableName("C"))
            .beginControlFlow("return when (visitor)")
            .addStatement("is %T -> visitor.%L(this, ctx)", grammar.visitorClassName, variantSpec.visitMethodName)
            .addStatement("else -> visitor.visit(this, ctx)")
            .endControlFlow()
            .addModifiers(KModifier.OVERRIDE)
            .returns(TypeVariableName("R"))
            .build()

        private fun TypeSpec.Builder.addApplyMethod(grammarSpec: GrammarSpec, variantSpec: VariantSpec) = this.apply {
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
            this.addProperty(PropertySpec.builder("state", Int::class, KModifier.OVERRIDE).initializer("state").build())
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
