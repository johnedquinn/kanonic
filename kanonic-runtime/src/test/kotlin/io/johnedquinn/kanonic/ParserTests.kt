package io.johnedquinn.kanonic

import org.junit.jupiter.api.Test

internal class ParserTests {

    @Test
    fun generateAutomatonGrammar11() {
//        val grammar = grammar("G11", "S") {
//            add("S") { +"V" - TokenType.EQUALS - "E" }
//            add("S") { +TokenType.IDENTIFIER }
//            add("V") { +TokenType.IDENTIFIER }
//            add("V") { +TokenType.IDENTIFIER - TokenType.BRACKET_LEFT - "E" - TokenType.BRACKET_RIGHT }
//            add("E") { +"V" }
//        }.toGrammar()
//        val generator = AutomatonGenerator()
//        val automaton = generator.generate(grammar)
//        grammar.printInformation()
//        automaton.printInfo()
//        val table = TableGenerator(grammar, automaton).generate()
//        println(table.prettify())
    }

//    @Test
//    fun generateAutomatonGrammar10() {
//        // Create Grammar
//        val grammar = grammar("G10", "p") {
//            tokens {
//                "IDENTIFIER" - "[a-zA-Z]+"
//                "PAREN_LEFT" - "\\("
//                "PAREN_RIGHT" - "\\)"
//                "PLUS" - "\\+"
//            }
//            "p" eq "e" alias "Root"
//            "e" eq "e" - "PLUS" - "t" alias "ExprPlus"
//            "e" eq "t" alias "ExprFall"
//            "t" eq "IDENTIFIER" - "PAREN_LEFT" - "e" - "PAREN_RIGHT" alias "Index"
//            "t" eq "IDENTIFIER" alias "Ident"
//        }.toGrammar()
//
//        val ident = 2
//        val parenLeft = 3
//        val parenRight = 4
//        val plus = 5
//
//        // Create Query
//        val tokens = listOf(
//            TokenLiteral(ident, 0, "a"),
//            TokenLiteral(plus, 1, "+"),
//            TokenLiteral(ident, 2, "b"),
//            TokenLiteral(plus, 3, "+"),
//            TokenLiteral(ident, 4, "c"),
//            TokenLiteral(plus, 5, "+"),
//            TokenLiteral(ident, 6, "d"),
//            TokenLiteral(parenLeft, 7, "("),
//            TokenLiteral(ident, 8, "f"),
//            TokenLiteral(plus, 9, "+"),
//            TokenLiteral(ident, 10, "g"),
//            TokenLiteral(parenRight, 11, ")"),
//            TokenLiteral(TokenLiteral.ReservedTypes.EOF, 12, "")
//        )
//
//        // Create Parser
//        val generator = AutomatonGenerator()
//        val automaton = generator.generate(grammar)
//        val table = TableGenerator(grammar, automaton).generate()
//        val parser = ParserInternal(grammar, table, G10Metadata())
//
//        // Print Information
//        grammar.printInformation()
//        automaton.printInfo()
//        println(table.prettify(grammar))
//
//        // Parse
//        val tree = parser.parse(tokens)
//        println(NodeFormatter.format(tree))
//    }
}
