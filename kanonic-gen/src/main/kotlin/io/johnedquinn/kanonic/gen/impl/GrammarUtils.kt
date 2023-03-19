package io.johnedquinn.kanonic.gen.impl

import io.johnedquinn.kanonic.Grammar

internal object GrammarUtils {

    public fun getPackageName(grammar: Grammar): String = grammar.options.packageName ?: "io.johnedquinn.kanonic.example"

    public fun getMetadataName(grammar: Grammar): String = "${grammar.options.grammarName}Metadata"

    public fun getGeneratedClassName(prefix: String) = "${prefix}Node"

    public fun getGrammarNodeName(grammar: Grammar) = getGeneratedClassName(grammar.options.grammarName)
}
