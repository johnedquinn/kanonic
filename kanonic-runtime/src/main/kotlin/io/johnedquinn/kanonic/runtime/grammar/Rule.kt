package io.johnedquinn.kanonic.runtime.grammar

data class Rule(
    val name: String,
    val variants: List<RuleVariant>,
    var generated: Boolean = false
) : Symbol
