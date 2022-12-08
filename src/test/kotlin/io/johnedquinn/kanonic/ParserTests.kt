package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.machine.AutomatonGenerator
import org.junit.jupiter.api.Test

internal class ParserTests {

    @Test
    fun generateAutomatonGrammar11() {
        val rule01 = Rule(
            name = "S",
            items = listOf(
                SymbolReference.RuleReference("V"),
                SymbolReference.TerminalReference(TokenType.EQUALS),
                SymbolReference.RuleReference("E")
            )
        )
        val rule02 = Rule(
            name = "S",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.IDENTIFIER)
            )
        )
        val rule03 = Rule(
            name = "V",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.IDENTIFIER)
            )
        )
        val rule04 = Rule(
            name = "V",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.IDENTIFIER),
                SymbolReference.TerminalReference(TokenType.BRACKET_LEFT),
                SymbolReference.RuleReference("E"),
                SymbolReference.TerminalReference(TokenType.BRACKET_RIGHT),
            )
        )
        val rule05 = Rule(
            name = "E",
            items = listOf(
                SymbolReference.RuleReference("V")
            )
        )
        val grammar = Grammar(
            rules = listOf(rule01, rule02, rule03, rule04, rule05),
            options = Grammar.Options("SimpleGrammar", SymbolReference.RuleReference("S"))
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
                SymbolReference.RuleReference("E")
            )
        )
        val rule02 = Rule(
            name = "E",
            items = listOf(
                SymbolReference.RuleReference("E"),
                SymbolReference.TerminalReference(TokenType.PLUS),
                SymbolReference.RuleReference("T")
            )
        )
        val rule03 = Rule(
            name = "E",
            items = listOf(
                SymbolReference.RuleReference("T")
            )
        )
        val rule04 = Rule(
            name = "T",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.IDENTIFIER),
                SymbolReference.TerminalReference(TokenType.PAREN_LEFT),
                SymbolReference.RuleReference("E"),
                SymbolReference.TerminalReference(TokenType.PAREN_RIGHT),
            )
        )
        val rule05 = Rule(
            name = "T",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.IDENTIFIER)
            )
        )

        val grammar = Grammar(
            rules = listOf(rule01, rule02, rule03, rule04, rule05),
            options = Grammar.Options("SimpleGrammar", SymbolReference.RuleReference("P"))
        )
        val generator = AutomatonGenerator()
        val automaton = generator.generate(grammar)
        println(automaton)
    }
}
