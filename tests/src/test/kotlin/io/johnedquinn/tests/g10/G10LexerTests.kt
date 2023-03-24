package io.johnedquinn.tests.g10

import io.johnedquinn.kanonic.dsl.grammar

class G10LexerTests {

    private val grammar = grammar("G10", "p") {
        tokens {
            "IDENTIFIER" - "[a-zA-Z]+"
            "PAREN_LEFT" - "\\("
            "PAREN_RIGHT" - "\\)"
            "PLUS" - "\\+"
        }
        "p" eq "e" alias "Root"
        "e" eq "e" - "PLUS" - "t" alias "ExprPlus"
        "e" eq "t" alias "ExprFall"
        "t" eq "IDENTIFIER" - "PAREN_LEFT" - "e" - "PAREN_RIGHT" alias "Index"
        "t" eq "IDENTIFIER" alias "Ident"
    }.toGrammar()

}
