package io.johnedquinn.calculator.interpreter

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculatorTests {

    @ParameterizedTest
    @ArgumentsSource(SuccessTests.Provider::class)
    fun testAll(tc: SuccessTests.TestCase) {
        val result = CalculatorEvaluator.evaluate(tc.input)
        assertEquals(tc.expected, result)
    }

    @Test
    fun singleTest() {
        val result = CalculatorEvaluator.evaluate("2 ^ 3")
        assertEquals(8, result)
    }

    object SuccessTests {
        data class TestCase(
            val input: String,
            val expected: Int
        )
        class Provider : TestProvider<TestCase>(args)
        val args = listOf(
            TestCase(
                input = "5 * (7 + 2)",
                expected = 45
            ),
            TestCase(
                input = "(7)",
                expected = 7
            ),
            TestCase(
                input = "2 * 3 + 1 + 5 * 7",
                expected = 42
            ),
            TestCase(
                input = "3 - 4",
                expected = -1
            ),
            TestCase(
                input = "2 / 3",
                expected = 0
            ),
            TestCase(
                input = "2 * 3 * 4",
                expected = 24
            ),
            TestCase(
                input = "1 + 2 + 3 + 4 + 5",
                expected = 15
            ),
            TestCase(
                input = "1 + 2 + 3",
                expected = 6
            ),
            TestCase(
                input = "1 ^ 2",
                expected = 1
            ),
            TestCase(
                input = "2 ^ 3",
                expected = 8
            ),
        )
    }
}
