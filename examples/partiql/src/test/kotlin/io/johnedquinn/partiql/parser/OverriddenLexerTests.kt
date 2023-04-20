package io.johnedquinn.partiql.parser

import org.junit.jupiter.api.Test

class OverriddenLexerTests {
    @Test
    public fun test() {
        val query = "SELECT a FROM b"
        OverriddenLexer.tokenize(query).forEach {
            println(it)
        }
    }
}