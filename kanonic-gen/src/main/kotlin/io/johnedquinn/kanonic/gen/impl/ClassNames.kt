package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal object ClassNames {
    val NODE = ClassName("io.johnedquinn.kanonic.parse", "Node")
    val NODE_VISITOR = ClassName("io.johnedquinn.kanonic.parse", "NodeVisitor")
    val CREATE_NODE = ClassName("io.johnedquinn.kanonic.parse", "CreateNode")
    val PARSER_INFO = ClassName("io.johnedquinn.kanonic.parse", "ParserMetadata")
    val LIST_NODE = ClassName("kotlin.collections", "List").parameterizedBy(NODE)
    val LIST_CREATE_NODE = ClassName("kotlin.collections", "List").parameterizedBy(CREATE_NODE)

    internal fun createGrammarNodeClass(packageName: String, grammarName: String) =
        ClassName(packageName, "${grammarName}Node")
}