package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.parse.Node

internal object NodeGenerator {

    /**
     * Creates the [FileSpec] containing the Grammar Node and its variants (Rule Nodes and the
     * Rule Variant Nodes).
     */
    public fun generate(grammar: Grammar): FileSpec {
        val packageName = GrammarUtils.getPackageName(grammar)
        val type = Private.createType(grammar)
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
        public fun createType(grammar: Grammar): TypeSpec {
            // Define the top-level grammar Node
            val className = GrammarUtils.getGeneratedClassName(grammar.options.grammarName)
            val type = TypeSpec.classBuilder(className)
            type.addModifiers(KModifier.SEALED, KModifier.INTERNAL)
            type.superclass(ClassNames.NODE)
            type.addSuperclassConstructorParameter(CodeBlock.of("state"))
            type.addSuperclassConstructorParameter(CodeBlock.of("children"))
            type.addSuperclassConstructorParameter(CodeBlock.of("parent"))
            type.primaryConstructor(createPrimaryConstructor())

            // Add Variant Definitions
            val ruleSpecs = createNodes(grammar)
            ruleSpecs.forEach { rule -> type.addType(rule) }
            return type.build()
        }

        /**
         * Creates the Rule Nodes and the Rule Variant Nodes
         */
        private fun createNodes(grammar: Grammar): List<TypeSpec> {
            return grammar.rules.groupBy { it.name }.map { (ruleName, ruleVariants) ->
                // Shared Information
                val ruleClassName = GrammarUtils.getGeneratedClassName(ruleName)
                val packageName = GrammarUtils.getPackageName(grammar)
                val grammarNodeName = GrammarUtils.getGrammarNodeName(grammar)

                // Create Rule Variants (Data Classes)
                val ruleClassReference = ClassName(packageName, grammarNodeName, ruleClassName)
                val typeSpecs = ruleVariants.map { variant ->
                    val variantClassName = GrammarUtils.getGeneratedClassName(variant.alias)
                    val variantSpec = TypeSpec.classBuilder(variantClassName)
                    variantSpec.addModifiers(KModifier.DATA)
                    variantSpec.addToString()
                    variantSpec.superclass(ruleClassReference)
                    variantSpec.addPrimaryConstructor()
                    variantSpec.build()
                }

                // Create Top-Level Rule (Abstract Class)
                val ruleSpec = TypeSpec.classBuilder(ruleClassName)
                val grammarNodeReference = ClassNames.createGrammarNodeClass(packageName, grammar.options.grammarName)
                ruleSpec.addModifiers(KModifier.SEALED, KModifier.INTERNAL)
                ruleSpec.superclass(grammarNodeReference)
                ruleSpec.addPrimaryConstructor()
                ruleSpec.addTypes(typeSpecs)
                ruleSpec.build()
            }
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
