package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.dsl.grammar
import io.johnedquinn.kanonic.machine.AutomatonGenerator
import io.johnedquinn.kanonic.machine.TableGenerator
import org.junit.jupiter.api.Test

internal class ParserTests {

    @Test
    fun generateAutomatonGrammar11() {
        val grammar = grammar("G11", "S") {
            add("S") { + "V" - TokenType.EQUALS - "E" }
            add("S") { + TokenType.IDENTIFIER }
            add("V") { + TokenType.IDENTIFIER }
            add("V") { + TokenType.IDENTIFIER - TokenType.BRACKET_LEFT - "E" - TokenType.BRACKET_RIGHT }
            add("E") { + "V" }
        }.toGrammar()
        val generator = AutomatonGenerator()
        val automaton = generator.generate(grammar)
        grammar.printInformation()
        automaton.printInfo()
        val table = TableGenerator(grammar, automaton).generate()
        println(table.prettify())
    }


    @Test
    fun generateAutomatonGrammar10() {
        val grammar = grammar("G10", "P") {
            add("P") { + "E" }
            add("E") { + "E" - TokenType.PLUS - "T" }
            add("E") { + "T" }
            add("T") { + TokenType.IDENTIFIER - TokenType.PAREN_LEFT - "E" - TokenType.PAREN_RIGHT }
            add("T") { + TokenType.IDENTIFIER }
        }.toGrammar()
        val generator = AutomatonGenerator()
        val automaton = generator.generate(grammar)
        grammar.printInformation()
        automaton.printInfo()
        val table = TableGenerator(grammar, automaton).generate()
        println(table.prettify())
    }
}
