package io.johnedquinn.kanonic.gen

import io.johnedquinn.kanonic.TokenType
import io.johnedquinn.kanonic.dsl.grammar
import org.junit.jupiter.api.Test

class KanonicGeneratorTests {

    @Test
    fun test() {
        // Create Grammar
        val grammar = grammar("G10", "P") {
            "P" eq "E" alias "Root"
            "E" eq "E" - TokenType.PLUS - "T" alias "ExprPlus"
            "E" eq "T" alias "ExprFall"
            "T" eq TokenType.IDENTIFIER - TokenType.PAREN_LEFT - "E" - TokenType.PAREN_RIGHT alias "Index"
            "T" eq TokenType.IDENTIFIER alias "Ident"
        }.toGrammar()

        //
        val generator = KanonicGenerator(grammar)
        val file = generator.generate(grammar)
        file.writeTo(System.out)
    }
}