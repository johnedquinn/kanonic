package io.johnedquinn.partiql.parser

import org.junit.jupiter.api.Test

class AntlrParserTests {
    val parser = AntlrParser
    
    @Test
    fun test() {
        val query = "SELECT a FROM b"
        parser.parse(query)
    }

    @Test
    fun testNested() {
        val query = "SELECT (SELECT a FROM b) FROM (SELECT c FROM d)"
        parser.parse(query)
    }

    @Test
    fun testArray() {
        val query = "[SELECT (SELECT a FROM b) FROM (SELECT c FROM d), d, e]"
        parser.parse(query)
    }

    @Test
    fun testComplex() {
        val query = "[a, b, [c, d, [e, (f)]], (SELECT a FROM (SELECT (SELECT [a, b, (c)] FROM d AS e) FROM f AS k) AS x)]"
        parser.parse(query)
    }

    @Test
    fun testParen() {
        val query = "((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((a))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))"
        parser.parse(query)
    }
}