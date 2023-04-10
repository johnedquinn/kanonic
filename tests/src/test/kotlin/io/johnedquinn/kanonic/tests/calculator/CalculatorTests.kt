package io.johnedquinn.kanonic.tests.calculator

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CalculatorTests {
    @Test
    public fun plus() {
        val result = CalculatorEvaluator.evaluate("1 + 1")
        assertEquals(2, result)
    }

    @Test
    public fun plusExample() {
        val result = CalculatorEvaluator.evaluate("1 + 2 + 3 + 4 + 5")
        assertEquals(15, result)
    }

    @Test
    public fun mult() {
        val result = CalculatorEvaluator.evaluate("2 * 3")
        assertEquals(6, result)
    }

    @Test
    public fun div() {
        val result = CalculatorEvaluator.evaluate("2 / 3")
        assertEquals(0, result)
    }

    @Test
    public fun minus() {
        val result = CalculatorEvaluator.evaluate("3 - 4")
        assertEquals(-1, result)
    }

    @Test
    public fun complex() {
        val result = CalculatorEvaluator.evaluate("2 * 3 + 1 + 5 * 7")
        assertEquals(42, result)
    }
}
