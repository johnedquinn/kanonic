package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.Token
import io.johnedquinn.kanonic.machine.AcceptAction
import io.johnedquinn.kanonic.machine.ParseTable
import io.johnedquinn.kanonic.machine.ReduceAction
import io.johnedquinn.kanonic.machine.ShiftAction
import java.util.Stack

/**
 * Push S0 onto S.
 * Let a be the first input token.
 *
 * Loop:
 *   - Let s be the top of the stack.
 *   - If ACTION[s, a] is accept:
 *     - Parse complete.
 *   - Else if ACTION[s, a] is shift t:
 *     - Push state t on the stack.
 *     - Let a be the next input token.
 *   - Else if ACTION[s, a] is reduce A → β:
 *     - Pop states corresponding to β from the stack.
 *     - Let t be the top of stack.
 *     - Push GOTO[t, A] onto the stack.
 *   - Otherwise:
 *     - Halt with a parse error.
 */
internal class Parser(val grammar: Grammar, val table: ParseTable) {
    private val stack = Stack<Int>()

    internal fun parse(tokens: List<Token>) {
        // Add first state
        stack.push(0)

        // X
        var tokenIndex = 0
        var currentState: Int
        while (true) {
            currentState = stack.peek()
            val token = tokens[tokenIndex]
            println("Evaluating $token with state ($currentState)")
            when (val action = table.actionTable[currentState][token.type.ordinal]) {
                is AcceptAction -> {
                    break
                }
                is ShiftAction -> {
                    stack.push(action.state)
                    tokenIndex++
                }
                is ReduceAction -> {
                    val rule = grammar.rules[action.rule]
                    repeat(rule.items.size) {
                        stack.pop()
                    }
                    currentState = stack.peek()
                    val ruleIndex = table.nonTerminals.indexOf(rule.name)
                    val goToState = table.goToTable[currentState][ruleIndex] as ShiftAction
                    stack.push(goToState.state)
                }
                null -> {
                    throw ParseFailureException()
                }
            }
        }
    }

    internal class ParseFailureException : RuntimeException()
}