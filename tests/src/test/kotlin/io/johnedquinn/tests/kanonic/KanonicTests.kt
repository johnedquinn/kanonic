package io.johnedquinn.tests.simple

import io.johnedquinn.kanonic.dsl.grammar
import io.johnedquinn.kanonic.gen.impl.KanonicGrammar
import io.johnedquinn.kanonic.generated.KanonicMetadata
import io.johnedquinn.kanonic.parse.KanonicParser
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
            CD::TOKEN::AB::
        """
        val ast = parser.parse(document)
        println(ast)
    }
}
