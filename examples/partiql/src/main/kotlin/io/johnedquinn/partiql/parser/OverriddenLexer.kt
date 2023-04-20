package io.johnedquinn.partiql.parser

import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
import io.johnedquinn.partiql.antlr.generated.PartiQLTokens
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token

object OverriddenLexer : KanonicLexer {
    override fun tokenize(input: String): Sequence<TokenLiteral> {
        val lexer = PartiQLTokens(CharStreams.fromString(input))
        var token: Token? = lexer.nextToken() ?: return emptySequence()
        return sequence {
            while (token != null) {
                val type = when (token!!.type) {
                    -1 -> 0
                    else -> token!!.type
                }
                val kanonicToken = TokenLiteral(type, token!!.charPositionInLine.toLong(), token!!.text)
                yield(kanonicToken)
                if (kanonicToken.type == 0) return@sequence
                token = lexer.nextToken()
                while (token != null && token!!.channel != 0) {
                    token = lexer.nextToken()
                }
            }
            return@sequence
        }
    }
}
