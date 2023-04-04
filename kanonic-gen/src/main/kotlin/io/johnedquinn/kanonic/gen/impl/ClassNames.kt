package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal object ClassNames {
    val GRAMMAR = ClassName("io.johnedquinn.kanonic", "Grammar")
    val GRAMMAR_BUILDER_COMPANION = ClassName("io.johnedquinn.kanonic.dsl.GrammarBuilder", "Companion")
    val NODE = ClassName("io.johnedquinn.kanonic.parse", "Node")
    val GENERATED_NODE = ClassName("io.johnedquinn.kanonic.parse", "GeneratedNode")
    val TERMINAL_NODE = ClassName("io.johnedquinn.kanonic.parse", "TerminalNode")
    val NODE_VISITOR = ClassName("io.johnedquinn.kanonic.parse", "NodeVisitor")
    val CREATE_NODE = ClassName("io.johnedquinn.kanonic.parse", "CreateNode")
    val PARSER_INFO = ClassName("io.johnedquinn.kanonic.parse", "ParserMetadata")
    val LIST = ClassName("kotlin.collections", "List")
    val LIST_TERMINAL_NODE = LIST.parameterizedBy(TERMINAL_NODE)
    val LIST_NODE = LIST.parameterizedBy(NODE)
    val LIST_CREATE_NODE = ClassName("kotlin.collections", "List").parameterizedBy(CREATE_NODE)

    internal fun createGrammarNodeClass(packageName: String, grammarName: String) =
        ClassName(packageName, "${grammarName}Node")
}
