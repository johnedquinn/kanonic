package io.johnedquinn.partiql.parser

import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.partiql.generated.PartiQLSpecification
import org.partiql.lang.domains.PartiqlAst
import org.partiql.lang.syntax.Parser

object PartiQLParser : Parser {

    private val parser = KanonicParser.Builder.standard().withSpecification(PartiQLSpecification).build()

    override fun parseAstStatement(source: String): PartiqlAst.Statement {
        val ast = parser.parse(source)
        return PartiQLVisitor.visit(ast)
    }
}
