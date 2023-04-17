package io.johnedquinn.ion.parser

import com.amazon.ionelement.api.createIonElementLoader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IonParserTests {

    private val loader = createIonElementLoader()

    @Test
    fun test() {
        val ion = "hello"
        val expected = loader.loadSingleElement(ion)
        val result = IonParser.parse(ion)
        assertEquals(expected, result)
    }

    @Test
    fun testAnnotation() {
        val ion = "'hello'"
        val expected = loader.loadSingleElement(ion)
        val result = IonParser.parse(ion)
        assertEquals(expected, result)
    }

    @Test
    fun testAnnotation2() {
        val ion = "a::'hello'"
        val expected = loader.loadSingleElement(ion)
        val result = IonParser.parse(ion)
        assertEquals(expected, result)
    }

    @Test
    fun testSexp() {
        val ion = "(a b::a)"
        val expected = loader.loadSingleElement(ion)
        val result = IonParser.parse(ion)
        assertEquals(expected, result)
    }
}
