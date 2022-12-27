package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.gen.Utils.getGeneratedClassName
import io.johnedquinn.kanonic.parse.Node

internal class NodeGenerator(private val grammar: Grammar) {

    private val packageName = grammar.options.packageName ?: "io.johnedquinn.kanonic.example"
    private val grammarNode = getGeneratedClassName(grammar.options.grammarName)

    /**
     * Creates the top-level parser node
     */
    internal fun generate(): TypeSpec {
        val ruleSpecs = createNodes(grammar)
        val type = TypeSpec.classBuilder(getGeneratedClassName(grammar.options.grammarName))
        val primaryConstructor = FunSpec.constructorBuilder()
            .addParameter("state", Int::class)
            .addParameter("children", ClassNames.LIST_NODE)
            .addParameter("parent", Node::class.asTypeName().copy(nullable = true))
            .build()
        type.addModifiers(KModifier.SEALED, KModifier.INTERNAL)
        type.superclass(ClassNames.NODE)
        type.addSuperclassConstructorParameter(CodeBlock.of("state"))
        type.addSuperclassConstructorParameter(CodeBlock.of("children"))
        type.addSuperclassConstructorParameter(CodeBlock.of("parent"))
        type.primaryConstructor(primaryConstructor)
        ruleSpecs.forEach { rule -> type.addType(rule) }
        return type.build()
    }

    private fun createNodes(grammar: Grammar): List<TypeSpec> = grammar.rules.groupBy { it.name }.map { (ruleName, ruleVariants) ->
        // Create Abstract Class Primary Constructor
        val primaryConstructor = FunSpec.constructorBuilder()
            .addParameter("state", Int::class)
            .addParameter("children", ClassNames.LIST_NODE)
            .addParameter("parent", Node::class.asTypeName().copy(nullable = true))
            .build()
        // Create Abstract Class
        val ruleSpec = TypeSpec.classBuilder(getGeneratedClassName(ruleName))
        ruleSpec.addModifiers(KModifier.SEALED, KModifier.INTERNAL)
        ruleSpec.superclass(ClassNames.createGrammarNodeClass(grammar.options.packageName!!, grammar.options.grammarName))
        ruleSpec.addSuperclassConstructorParameter(CodeBlock.of("state"))
        ruleSpec.addSuperclassConstructorParameter(CodeBlock.of("children"))
        ruleSpec.addSuperclassConstructorParameter(CodeBlock.of("parent"))
        ruleSpec.primaryConstructor(primaryConstructor)
        val ruleClassName = ClassName(packageName, grammarNode, getGeneratedClassName(ruleName))

        // Create Variant Data Classes
        val typeSpecs = ruleVariants.map { variant ->
            val typeConstructor = FunSpec.constructorBuilder()
                .addParameter("state", Int::class)
                .addParameter("children", ClassNames.LIST_NODE)
                .addParameter("parent", Node::class.asTypeName().copy(nullable = true))
                .build()
            val variantSpec = TypeSpec.classBuilder(getGeneratedClassName(variant.alias))
            variantSpec.addModifiers(KModifier.INTERNAL)
            variantSpec.superclass(ruleClassName)
            variantSpec.addSuperclassConstructorParameter(CodeBlock.of("state"))
            variantSpec.addSuperclassConstructorParameter(CodeBlock.of("children"))
            variantSpec.addSuperclassConstructorParameter(CodeBlock.of("parent"))
            variantSpec.primaryConstructor(typeConstructor)
            variantSpec.build()
        }
        ruleSpec.addTypes(typeSpecs)
        ruleSpec.build()
    }
}
