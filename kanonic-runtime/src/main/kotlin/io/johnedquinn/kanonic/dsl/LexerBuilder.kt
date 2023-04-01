package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral

class LexerBuilder {
    private val tokens = mutableListOf<TokenDefinition>()
    init {
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EOF, "EOF", ""))
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EPSILON, "EPSILON", ""))
    }
    operator fun String.minus(other: String) {
        val token = TokenDefinition(tokens.size, this, other)
        tokens.add(token)
    }

    fun build() = tokens.toList()
}
