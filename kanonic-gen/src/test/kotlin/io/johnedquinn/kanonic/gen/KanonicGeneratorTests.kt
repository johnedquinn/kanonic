package io.johnedquinn.kanonic.gen

import io.johnedquinn.kanonic.dsl.grammar
import org.junit.jupiter.api.Test

class KanonicGeneratorTests {

    @Test
    fun test() {
        // Create Grammar
        val grammar = grammar("G10", "P") {
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

        //
        val generator = KanonicGenerator(grammar)
        val file = generator.generate(grammar)
        file.writeTo(System.out)
    }
}