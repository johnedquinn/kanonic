package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.parse.Node

internal class KanonicGenerator(grammar: Grammar) {

    private val nodeRuntimeClassName = ClassName("io.johnedquinn.kanonic.parse", "Node")
    private val createNode = ClassName("io.johnedquinn.kanonic.parse", "CreateNode")
    private val parserInfoRuntimeClassName = ClassName("io.johnedquinn.kanonic.parse", "ParserInfo")
    private val listNodeClassName = ClassName("kotlin.collections", "List")
        .parameterizedBy(nodeRuntimeClassName)
    private val listCreateNode = ClassName("kotlin.collections", "List")
        .parameterizedBy(createNode)

    private val packageName = "io.johnedquinn.kanonic.example"
    private val parserInfoName = "${grammar.options.grammarName}Info"

    internal fun generate(grammar: Grammar): FileSpec {
        // Create Lambda Functions
        val infoClass = createParserInfoClass(grammar)

        // Create Abstract and Data Classes
        val ruleSpecs = createNodes(grammar)

        return FileSpec.builder(packageName, "ExampleAST").also { file ->
            file.addType(infoClass)
            ruleSpecs.forEach { rule -> file.addType(rule) }
        }.build()
    }

    private fun createLambdaFunctions(): PropertySpec {
        val nodeList = PropertySpec.builder("nodeLambdaList", listCreateNode)
        nodeList.addModifiers(KModifier.PRIVATE)
        nodeList.initializer("initializeLambdas()")
        return nodeList.build()
    }

    // TODO: Adjust returns
    private fun createLambdaInitializerFunction(grammar: Grammar): FunSpec {
        val funSpec = FunSpec.builder("initializeLambdas")
        funSpec.addModifiers(KModifier.PRIVATE)
        funSpec.returns(listCreateNode)
        funSpec.beginControlFlow("return buildList")
        grammar.rules.groupBy { it.name }.map { (rule, ruleVariants) ->
            ruleVariants.forEach { variant ->
                val ruleSpec = ClassName("$packageName.${getGeneratedClassName(rule)}", getGeneratedClassName(variant.alias))
                funSpec.addStatement("add(CreateNode { state, children, parent -> %T(state, children, parent) })", ruleSpec)
            }
        }
        funSpec.endControlFlow()
        return funSpec.build()
    }

    private fun createParserInfoClass(grammar: Grammar): TypeSpec {
        val infoSpec = TypeSpec.classBuilder(parserInfoName)
        infoSpec.addSuperinterface(parserInfoRuntimeClassName)
        infoSpec.addFunction(createLambdaInitializerFunction(grammar))
        infoSpec.addProperty(createLambdaFunctions())
        infoSpec.addFunction(createCreateRuleNode())
        return infoSpec.build()
    }

    private fun createCreateRuleNode(): FunSpec {
        val func = FunSpec.builder("createRuleNode")
        func.returns(nodeRuntimeClassName)
        func.addModifiers(KModifier.OVERRIDE)
        func.addParameter("index", Int::class)
        func.addParameter("state", Int::class)
        func.addParameter("children", listNodeClassName)
        func.addParameter("parent", Node::class.asTypeName().copy(nullable = true))
        func.addStatement("val nodeCreator = nodeLambdaList[index]")
        func.addStatement("return nodeCreator.create(state, children, parent)")
        return func.build()
    }

    private fun createNodes(grammar: Grammar): List<TypeSpec> = grammar.rules.groupBy { it.name }.map { (ruleName, ruleVariants) ->
        // Create Abstract Class Primary Constructor
        val primaryConstructor = FunSpec.constructorBuilder()
            .addParameter("state", Int::class)
            .addParameter("children", listNodeClassName)
            .addParameter("parent", Node::class.asTypeName().copy(nullable = true))
            .build()
        // Create Abstract Class
        val ruleSpec = TypeSpec.classBuilder(getGeneratedClassName(ruleName))
        ruleSpec.addModifiers(KModifier.SEALED, KModifier.INTERNAL)
        ruleSpec.superclass(nodeRuntimeClassName)
        ruleSpec.addSuperclassConstructorParameter(CodeBlock.of("state"))
        ruleSpec.addSuperclassConstructorParameter(CodeBlock.of("children"))
        ruleSpec.addSuperclassConstructorParameter(CodeBlock.of("parent"))
        ruleSpec.primaryConstructor(primaryConstructor)
        val ruleClassName = ClassName(packageName, getGeneratedClassName(ruleName))

        // Create Variant Data Classes
        val typeSpecs = ruleVariants.map { variant ->
            val typeConstructor = FunSpec.constructorBuilder()
                .addParameter("state", Int::class)
                .addParameter("children", listNodeClassName)
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

    private fun getGeneratedClassName(prefix: String) = "${prefix}Node"
}
