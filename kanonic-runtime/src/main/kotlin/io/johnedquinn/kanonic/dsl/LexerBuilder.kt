package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral
import java.util.Locale

class LexerBuilder {
    private val tokens = mutableListOf<TokenDefinition>()
    init {
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EOF, "EOF", "", false))
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EPSILON, "EPSILON", "", false))
    }
    operator fun String.minus(other: String) {
        val token = TokenDefinition(tokens.size, this, other, false)
        tokens.add(token)
    }

    operator fun String.minus(other: TokenMeta) {
        val hidden = when (other.channel.lowercase(Locale.getDefault())) {
            "hidden" -> true
            "main" -> false
            else -> error("Unrecognized channel")
        }
        val token = TokenDefinition(tokens.size, this, other.def, hidden)
        tokens.add(token)
    }

    infix fun String.channel(other: String): TokenMeta {
        return TokenMeta(this, other)
    }

    class TokenMeta(val def: String, val channel: String)

    fun build() = tokens.toList()
}
