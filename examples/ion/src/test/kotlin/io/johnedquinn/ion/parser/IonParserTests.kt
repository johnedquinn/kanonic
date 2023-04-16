package io.johnedquinn.ion.parser

import com.amazon.ionelement.api.createIonElementLoader
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class IonParserTests {

    private val loader = createIonElementLoader()

    @Test
    fun test() {
        val ion = "hello"
        val expected = loader.loadSingleElement(ion)
        val result = IonParser.parse(ion)
        assertEquals(expected, result)
    }
}
