package io.johnedquinn.kanonic

data class Rule(
    val name: String,
    val items: List<SymbolReference>
) : Symbol