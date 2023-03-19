package io.johnedquinn.tests.simple

import io.johnedquinn.kanonic.dsl.grammar
import io.johnedquinn.kanonic.parse.KanonicParser
import org.junit.jupiter.api.Test

class SimpleTests {

    @Test
    public fun test() {
        val grammar = grammar("Simple", "p") {
            tokens {
                "IDENTIFIER" - "[a-zA-Z]+"
                "PAREN_LEFT" - "\\("
                "PAREN_RIGHT" - "\\)"
                "PLUS" - "\\+"
            }
            packageName("io.johnedquinn.tests.simple")
            "p" eq "e" alias "Root"
            "e" eq "e" - "PLUS" - "t" alias "ExprPlus"
            "e" eq "t" alias "ExprFall"
            "t" eq "IDENTIFIER" - "PAREN_LEFT" - "e" - "PAREN_RIGHT" alias "Index"
            "t" eq "IDENTIFIER" alias "Ident"
        }.toGrammar()
        val parser = KanonicParser.Builder
            .standard()
            .withMetadata(SimpleMetadata())
            .withGrammar(grammar)
            .build()
        val ast = parser.parse("a + b")
        println(ast)
    }
}
