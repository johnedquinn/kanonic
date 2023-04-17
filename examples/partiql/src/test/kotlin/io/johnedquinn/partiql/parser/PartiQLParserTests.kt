package io.johnedquinn.partiql.parser

import org.junit.jupiter.api.Test
import org.partiql.lang.domains.PartiqlAst
import kotlin.test.assertEquals

class PartiQLParserTests {
    private val parser = PartiQLParser

    @Test
    fun test() {
        val query = "SELECT a FROM b"
        val expected = PartiqlAst.build {
            query(
                select(
                    project = projectList(projectExpr_(id("a", caseInsensitive(), unqualified()))),
                    from = scan(id("b", caseInsensitive(), unqualified()))
                )
            )
        }
        val result = parser.parseAstStatement(query)
        assertEquals(expected, result)
    }
    @Test
    fun test2() {
        val query = "SELECT (SELECT a FROM b) FROM b"
        parser.parseAstStatement(query)
    }

    @Test
    fun test3() {
        val query = "SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)"
        parser.parseAstStatement(query)
    }
}