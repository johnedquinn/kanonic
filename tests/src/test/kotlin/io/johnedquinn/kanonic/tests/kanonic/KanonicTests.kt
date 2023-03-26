package io.johnedquinn.kanonic.tests.kanonic

import io.johnedquinn.kanonic.parse.KanonicParser
import io.johnedquinn.kanonic.runtime.generated.KanonicMetadata
import io.johnedquinn.kanonic.syntax.KanonicGrammar
import io.johnedquinn.kanonic.tests.kanonic.GrammarProvider
import io.johnedquinn.kanonic.utils.KanonicNodeFormatter
import org.junit.jupiter.api.Test

class KanonicTests {

    @Test
    public fun test() {
        val document = GrammarProvider.provide("grammar_definitions/calculator.knc")
        val grammar = KanonicGrammar.grammar
        val parser = KanonicParser.Builder
            .standard()
            .withMetadata(KanonicMetadata())
            .withGrammar(grammar)
            .build()
        val ast = parser.parse(document)
        println(KanonicNodeFormatter.format(ast))
    }
}
