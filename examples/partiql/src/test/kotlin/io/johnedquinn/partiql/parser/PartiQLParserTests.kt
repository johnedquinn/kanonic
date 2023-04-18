package io.johnedquinn.partiql.parser

import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.partiql.generated.PartiQLSpecification
import org.junit.jupiter.api.Test
import org.partiql.lang.domains.PartiqlAst
import java.time.Instant
import kotlin.test.assertEquals

class PartiQLParserTests {
    private val lexer = KanonicLexer.Builder.standard().withDefinitions(PartiQLSpecification.grammar.tokens).build()
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
        val start = Instant.now()
        parser.parseAstStatement(query)
        val end = Instant.now()
        val total = timeDiff(start, end)
        println("PARSED IN $total")
    }

    @Test
    fun test4() {
        val query = "SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)"
        val start = Instant.now()
        lexer.tokenize(query).toList()
        val end = Instant.now()
        val total = timeDiff(start, end)
        println("TOKENIZED IN $total")
    }

    public fun timeDiff(older: Instant, newer: Instant): String {
        val secondsInNanos = (newer.epochSecond - older.epochSecond) * 1_000_000_000L + newer.nano - older.nano
        return "$secondsInNanos ns"
    }
}