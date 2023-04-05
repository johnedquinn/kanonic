package io.johnedquinn.kanonic

data class Rule(
    val name: String,
    val variants: List<RuleVariant>,
    var generated: Boolean = false
) : Symbol
