package io.johnedquinn.partiql.parser

import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.partiql.generated.PartiQLSpecification
import org.junit.jupiter.api.Test

class KanonicParserTests {
    private val parser = KanonicParser.Builder.standard().withSpecification(PartiQLSpecification).build()
    @Test
    fun test() {
        parser.parse("[a, b, c, SELECT c FROM d, (e)]")
    }
}