package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.dsl.grammar
import io.johnedquinn.kanonic.machine.AutomatonGenerator
import io.johnedquinn.kanonic.machine.TableGenerator
import io.johnedquinn.kanonic.parse.Parser
import io.johnedquinn.kanonic.utils.NodeFormatter
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
        // Create Grammar
        val grammar = grammar("G10", "P") {
            "P" eq "E" alias "Root"
            "E" eq "E" - TokenType.PLUS - "T" alias "ExprPlus"
            "E" eq "T" alias "ExprFall"
            "T" eq TokenType.IDENTIFIER - TokenType.PAREN_LEFT - "E" - TokenType.PAREN_RIGHT alias "Index"
            "T" eq TokenType.IDENTIFIER alias "Ident"
        }.toGrammar()

        // Create Query
        val tokens = listOf(
            Token(TokenType.IDENTIFIER),
            Token(TokenType.PLUS),
            Token(TokenType.IDENTIFIER),
            Token(TokenType.PLUS),
            Token(TokenType.IDENTIFIER),
            Token(TokenType.PLUS),
            Token(TokenType.IDENTIFIER),
            Token(TokenType.PAREN_LEFT),
            Token(TokenType.IDENTIFIER),
            Token(TokenType.PLUS),
            Token(TokenType.IDENTIFIER),
            Token(TokenType.PAREN_RIGHT),
            Token(TokenType.EOF)
        )

        // Create Parser
        val generator = AutomatonGenerator()
        val automaton = generator.generate(grammar)
        val table = TableGenerator(grammar, automaton).generate()
        val parser = Parser(grammar, table)

        // Print Information
        grammar.printInformation()
        automaton.printInfo()
        println(table.prettify())

        // Parse
        val tree = parser.parse(tokens)
        println(NodeFormatter.format(tree))
    }
}
