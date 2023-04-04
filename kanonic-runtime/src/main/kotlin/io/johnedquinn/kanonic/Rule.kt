package io.johnedquinn.kanonic

data class Rule(
    val name: String,
    val items: List<SymbolReference>,
    var alias: String = "None",
    var generated: Boolean = false
) : Symbol
