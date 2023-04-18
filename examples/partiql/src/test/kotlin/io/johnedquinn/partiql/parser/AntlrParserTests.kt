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
}