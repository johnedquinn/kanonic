package io.johnedquinn.kanonic

data class RuleVariant(
    var name: String,
    val parentName: String,
    val items: List<SymbolReference>,
)
