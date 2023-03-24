package io.johnedquinn.kanonic.gen

import io.johnedquinn.kanonic.dsl.grammar
import io.johnedquinn.kanonic.gen.impl.KanonicGrammar
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class KanonicGeneratorTests {

    @Test
    fun test() {
        // Create Grammar
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

        // Generate Files
        val files = KanonicGenerator.generate(grammar)
        files.forEach {
            println("NAME: ${it.name}")
            it.writeTo(System.out)
        }
    }

    @Test
    fun kanonic() {
        val grammar = KanonicGrammar.grammar
        val files = KanonicGenerator.generate(grammar)
        files.forEach {
            val path = Path("build/generated-src")
            println("NAME: ${it.name}")
            println("PATH: ${path.toAbsolutePath()}")
            it.writeTo(path)
        }
    }
}
