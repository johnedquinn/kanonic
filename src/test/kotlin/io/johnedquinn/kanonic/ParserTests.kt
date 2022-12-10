package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.machine.AutomatonGenerator
import org.junit.jupiter.api.Test

internal class ParserTests {

    @Test
    fun generateAutomatonGrammar11() {
        val rule01 = Rule(
            name = "S",
            items = listOf(
                RuleReference("V"),
                TerminalReference(TokenType.EQUALS),
                RuleReference("E")
            )
        )
        val rule02 = Rule(
            name = "S",
            items = listOf(
                TerminalReference(TokenType.IDENTIFIER)
            )
        )
        val rule03 = Rule(
            name = "V",
            items = listOf(
                TerminalReference(TokenType.IDENTIFIER)
            )
        )
        val rule04 = Rule(
            name = "V",
            items = listOf(
                TerminalReference(TokenType.IDENTIFIER),
                TerminalReference(TokenType.BRACKET_LEFT),
                RuleReference("E"),
                TerminalReference(TokenType.BRACKET_RIGHT),
            )
        )
        val rule05 = Rule(
            name = "E",
            items = listOf(
                RuleReference("V")
            )
        )
        val grammar = Grammar(
            rules = listOf(rule01, rule02, rule03, rule04, rule05),
            options = Grammar.Options("SimpleGrammar", RuleReference("S"))
        )

        val generator = AutomatonGenerator()
        val automaton = generator.generate(grammar)
        println(automaton)
    }


    @Test
    fun generateAutomatonGrammar10() {
        val rule01 = Rule(
            name = "P",
            items = listOf(
                RuleReference("E")
            )
        )
        val rule02 = Rule(
            name = "E",
            items = listOf(
                RuleReference("E"),
                TerminalReference(TokenType.PLUS),
                RuleReference("T")
            )
        )
        val rule03 = Rule(
            name = "E",
            items = listOf(
                RuleReference("T")
            )
        )
        val rule04 = Rule(
            name = "T",
            items = listOf(
                TerminalReference(TokenType.IDENTIFIER),
                TerminalReference(TokenType.PAREN_LEFT),
                RuleReference("E"),
                TerminalReference(TokenType.PAREN_RIGHT),
            )
        )
        val rule05 = Rule(
            name = "T",
            items = listOf(
                TerminalReference(TokenType.IDENTIFIER)
            )
        )

        val grammar = Grammar(
            rules = listOf(rule01, rule02, rule03, rule04, rule05),
            options = Grammar.Options("SimpleGrammar", RuleReference("P"))
        )
        val generator = AutomatonGenerator()
        val automaton = generator.generate(grammar)
        println(automaton)
    }
}
