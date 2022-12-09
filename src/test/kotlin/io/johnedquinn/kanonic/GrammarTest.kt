package io.johnedquinn.kanonic

import org.junit.jupiter.api.Test

class GrammarTest {

    @Test
    fun test() {

        val rule01 = Rule(
            name = "E",
            items = listOf(
                SymbolReference.RuleReference("T"),
                SymbolReference.RuleReference("R")
            )
        )
        val rule02 = Rule(
            name = "R",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.PLUS),
                SymbolReference.RuleReference("T"),
                SymbolReference.RuleReference("R")
            )
        )
        val rule03 = Rule(
            name = "R",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.HASHTAG)
            )
        )
        val rule04 = Rule(
            name = "T",
            items = listOf(
                SymbolReference.RuleReference("F"),
                SymbolReference.RuleReference("Y"),
            )
        )
        val rule05 = Rule(
            name = "Y",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.ASTERISK),
                SymbolReference.RuleReference("F"),
                SymbolReference.RuleReference("Y"),
            )
        )
        val rule06 = Rule(
            name = "Y",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.HASHTAG),
            )
        )
        val rule07 = Rule(
            name = "F",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.PAREN_LEFT),
                SymbolReference.RuleReference("E"),
                SymbolReference.TerminalReference(TokenType.PAREN_RIGHT),
            )
        )
        val rule08 = Rule(
            name = "F",
            items = listOf(
                SymbolReference.TerminalReference(TokenType.IDENTIFIER),
            )
        )
        val grammar = Grammar(
            rules = listOf(rule01, rule02, rule03, rule04, rule05, rule06, rule07, rule08),
            options = Grammar.Options("SimpleGrammar", SymbolReference.RuleReference("E"))
        )

        println(grammar.firstSet)
    }
}