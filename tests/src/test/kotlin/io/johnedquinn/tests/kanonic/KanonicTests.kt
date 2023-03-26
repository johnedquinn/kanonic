package io.johnedquinn.tests.simple

import io.johnedquinn.kanonic.syntax.KanonicGrammar
import io.johnedquinn.kanonic.generated.KanonicMetadata
import io.johnedquinn.kanonic.parse.KanonicParser
import io.johnedquinn.kanonic.utils.KanonicNodeFormatter
import org.junit.jupiter.api.Test

class KanonicTests {

    @Test
    public fun test() {
        val grammar = KanonicGrammar.grammar
        val parser = KanonicParser.Builder
            .standard()
            .withMetadata(KanonicMetadata())
            .withGrammar(grammar)
            .build()
        val document = """
            CD:"h"
            TOKEN:"a"
            AB:"b"
        """
        val ast = parser.parse(document)
        println(KanonicNodeFormatter().format(ast))
        println(ast)
    }
}
