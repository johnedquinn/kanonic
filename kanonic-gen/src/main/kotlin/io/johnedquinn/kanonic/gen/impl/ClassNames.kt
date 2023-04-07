package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal object ClassNames {
    val GRAMMAR = ClassName("io.johnedquinn.kanonic.runtime.grammar", "Grammar")
    val GRAMMAR_BUILDER_COMPANION = ClassName("io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder", "Companion")
    val RULE_BUILDER_COMPANION = ClassName("io.johnedquinn.kanonic.runtime.grammar.RuleBuilder", "Companion")
    val NODE = ClassName("io.johnedquinn.kanonic.runtime.ast", "Node")
    val GENERATED_NODE = ClassName("io.johnedquinn.kanonic.runtime.ast", "GeneratedNode")
    val TERMINAL_NODE = ClassName("io.johnedquinn.kanonic.runtime.ast", "TerminalNode")
    val NODE_VISITOR = ClassName("io.johnedquinn.kanonic.runtime.ast", "NodeVisitor")
    val CREATE_NODE = ClassName("io.johnedquinn.kanonic.runtime.parse", "NodeCreator")
    val PARSER_INFO = ClassName("io.johnedquinn.kanonic.runtime.parse", "ParserSpecification")
    val LIST = ClassName("kotlin.collections", "List")
    val LIST_TERMINAL_NODE = LIST.parameterizedBy(TERMINAL_NODE)
    val LIST_NODE = LIST.parameterizedBy(NODE)
    val LIST_CREATE_NODE = ClassName("kotlin.collections", "List").parameterizedBy(CREATE_NODE)

    internal fun createGrammarNodeClass(packageName: String, grammarName: String) =
        ClassName(packageName, "${grammarName}Node")
}
