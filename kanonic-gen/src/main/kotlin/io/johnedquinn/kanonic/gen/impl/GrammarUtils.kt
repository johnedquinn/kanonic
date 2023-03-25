package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.Grammar

internal object GrammarUtils {

    public fun getPackageName(grammar: Grammar): String = grammar.options.packageName ?: "io.johnedquinn.kanonic.example"

    public fun getMetadataName(grammar: Grammar): String = "${grammar.options.grammarName}Metadata"

    public fun getGeneratedClassName(prefix: String) = "${prefix}Node"

    public fun getGeneratedVisitorName(grammar: Grammar) = "${grammar.options.grammarName}Visitor"

    public fun getGeneratedBaseVisitorName(grammar: Grammar) = "${grammar.options.grammarName}BaseVisitor"

    public fun getGrammarNodeName(grammar: Grammar) = getGeneratedClassName(grammar.options.grammarName)

    public fun getGeneratedNodeClassName(grammar: Grammar): ClassName {
        return ClassName(getPackageName(grammar), getGrammarNodeName(grammar))
    }
}
