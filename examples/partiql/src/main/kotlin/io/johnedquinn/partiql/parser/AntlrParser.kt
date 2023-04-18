package io.johnedquinn.partiql.parser

import io.johnedquinn.partiql.antlr.generated.PartiQLParser
import io.johnedquinn.partiql.antlr.generated.PartiQLTokens
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

object AntlrParser {
    fun parse(input: String): PartiQLParser.StatementContext {
        val lexer = PartiQLTokens(CharStreams.fromString(input))
        val parser = PartiQLParser(CommonTokenStream(lexer))
        return parser.statement()
    }
}