package io.johnedquinn.kanonic.runtime.grammar

data class RuleVariant(
    var name: String,
    val parentName: String,
    val items: List<SymbolReference>,
)
