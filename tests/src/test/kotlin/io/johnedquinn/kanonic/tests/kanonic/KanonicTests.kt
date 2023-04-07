package io.johnedquinn.kanonic.tests.kanonic

import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.syntax.generated.KanonicSpecification
import io.johnedquinn.kanonic.tool.KanonicNodeFormatter
import org.junit.jupiter.api.Test

class KanonicTests {

    @Test
    public fun test() {
        val document = GrammarProvider.provide("grammar_definitions/calculator.knc")
        val parser = KanonicParser.Builder
            .standard()
            .withSpecification(KanonicSpecification)
            .build()
        val ast = parser.parse(document)
        println(KanonicNodeFormatter.format(ast))
    }
}
