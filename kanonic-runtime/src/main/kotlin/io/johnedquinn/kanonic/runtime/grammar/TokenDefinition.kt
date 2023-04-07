package io.johnedquinn.kanonic.runtime.grammar

data class TokenDefinition(
    val index: Int,
    val name: String,
    val def: String,
    val hidden: Boolean
)
