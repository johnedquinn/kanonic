package io.johnedquinn.ion.parser

import com.amazon.ionelement.api.createIonElementLoader
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class IonParserTests {

    companion object {
        private var loader = createIonElementLoader()
        private var parser = IonParser
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            loader = createIonElementLoader()
            parser = IonParser
        }
    }

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
        val result = parser.parse(ion)
        assertEquals(expected, result)
    }

    @Test
    fun testSexp() {
        val ion = "(a b::a c d e f g h i j k l m n o p q r s t u v w x y z)"
        val result = IonParser.parse(ion)
        val loaderStart = Instant.now()
        val expected = loader.loadSingleElement(ion)
        val resultReceived = Instant.now()
        val totalTime = "${resultReceived.epochSecond - loaderStart.epochSecond} seconds and ${resultReceived.nano - loaderStart.nano} ns"
        val secondsInNanos = (resultReceived.epochSecond - loaderStart.epochSecond) * 1_000_000_000L + resultReceived.nano - loaderStart.nano
        println("Total loader time: $totalTime")
        println("Total loader time: $secondsInNanos ns")
        assertEquals(expected, result)
    }
}
