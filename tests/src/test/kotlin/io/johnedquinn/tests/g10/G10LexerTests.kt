package io.johnedquinn.tests.g10

import io.johnedquinn.kanonic.TokenType
import io.johnedquinn.kanonic.dsl.grammar
import org.junit.jupiter.api.Test

class G10LexerTests {

    private val grammar = grammar("G10", "P") {
        tokens {
            "IDENTIFIER" - "[a-zA-Z]+"
            "PAREN_LEFT" - "\\("
            "PAREN_RIGHT" - "\\)"
            "PLUS" - "\\+"
        }
        "P" eq "E" alias "Root"
        "E" eq "E" - TokenType.PLUS - "T" alias "ExprPlus"
        "E" eq "T" alias "ExprFall"
        "T" eq TokenType.IDENTIFIER - TokenType.PAREN_LEFT - "E" - TokenType.PAREN_RIGHT alias "Index"
        "T" eq TokenType.IDENTIFIER alias "Ident"
    }.toGrammar()

    @Test
    fun test() {
        val query = "a + ab + ( )"
        println("Grammar Tokens: ${grammar.tokens}")
        val tokens = G10Lexer(grammar.tokens).tokenize(query)
        tokens.forEach { token ->
            println(token)
        }
    }
}