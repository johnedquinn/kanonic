package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.parse.Node

internal class KanonicGenerator(grammar: Grammar) {

    private val packageName = grammar.options.packageName ?: "io.johnedquinn.kanonic.example"
    private val parserInfoName = "${grammar.options.grammarName}Info"
    private val grammarNode = getGeneratedClassName(grammar.options.grammarName)

    /**
     * Generates all files
     */
    internal fun generate(grammar: Grammar): FileSpec {
        // Create Lambda Functions
        val infoClass = createParserInfoClass(grammar)

        // Create Abstract and Data Classes
        val grammarNode = NodeGenerator(grammar).generate()

        return FileSpec.builder(packageName, "ExampleAST").also { file ->
            file.addType(infoClass)
            file.addType(grammarNode)
        }.build()
    }

    private fun createLambdaFunctions(): PropertySpec {
        val nodeList = PropertySpec.builder("nodeLambdaList", ClassNames.LIST_CREATE_NODE)
        nodeList.addModifiers(KModifier.PRIVATE)
        nodeList.initializer("initializeLambdas()")
        return nodeList.build()
    }

    // TODO: Adjust returns
    private fun createLambdaInitializerFunction(grammar: Grammar): FunSpec {
        val funSpec = FunSpec.builder("initializeLambdas")
        funSpec.addModifiers(KModifier.PRIVATE)
        funSpec.returns(ClassNames.LIST_CREATE_NODE)
        funSpec.beginControlFlow("return buildList")
        grammar.rules.groupBy { it.name }.map { (rule, ruleVariants) ->
            ruleVariants.forEach { variant ->
                val ruleSpec = ClassName(packageName, grammarNode, getGeneratedClassName(rule), getGeneratedClassName(variant.alias))
                funSpec.addStatement("add(CreateNode·{·state,·children,·parent·->·%T(state,·children,·parent)·})", ruleSpec)
            }
        }
        funSpec.endControlFlow()
        return funSpec.build()
    }

    private fun createParserInfoClass(grammar: Grammar): TypeSpec {
        val infoSpec = TypeSpec.classBuilder(parserInfoName)
        infoSpec.addSuperinterface(ClassNames.PARSER_INFO)
        infoSpec.addFunction(createLambdaInitializerFunction(grammar))
        infoSpec.addProperty(createLambdaFunctions())
        infoSpec.addFunction(createCreateRuleNode())
        return infoSpec.build()
    }

    private fun createCreateRuleNode(): FunSpec {
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

    private fun getGeneratedClassName(prefix: String) = "${prefix}Node"
}
