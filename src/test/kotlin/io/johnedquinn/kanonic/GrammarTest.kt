package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.dsl.grammar
import org.junit.jupiter.api.Test

class GrammarTest {

    @Test
    fun geeksForGeeksExample() {
        // See: https://www.geeksforgeeks.org/program-calculate-first-follow-sets-given-grammar/
        val grammar = grammar("Geeks", "E") {
            add("E") { +"T" - "R" }
            add("R") { +TokenType.PLUS - "T" - "R" }
            add("R") { +TokenType.HASHTAG }
            add("T") { +"F" - "Y" }
            add("Y") { +TokenType.ASTERISK - "F" - "Y"}
            add("Y") { +TokenType.HASHTAG }
            add("F") { +TokenType.PAREN_LEFT - "E" - TokenType.PAREN_RIGHT }
            add("F") { +TokenType.IDENTIFIER }
        }.toGrammar()
        grammar.printInformation()
    }

    @Test
    fun csUaf4_11() {
        // See: https://www.cs.uaf.edu/~cs331/notes/FirstFollow.pdf
        val grammar = grammar("CS UAF 4.11", "E") {
            add("E") { + "T" - "U" }
            add("U") { + TokenType.PLUS - "T" - "U" }
            add("U") { + TokenType.EPSILON }
            add("T") { + "F" - "X" }
            add("X") { + TokenType.ASTERISK - "F" - "X" }
            add("X") { + TokenType.EPSILON }
            add("F") { + TokenType.PAREN_LEFT - "E" - TokenType.PAREN_RIGHT }
            add("F") { + TokenType.IDENTIFIER }
        }.toGrammar()
        grammar.printInformation()
    }
}