package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar
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
internal class ParserInternal(private val grammar: Grammar, private val table: ParseTable, private val info: ParserMetadata) {

    public fun parse(tokens: List<TokenLiteral>): Node {
        // Add first state
        val stack = Stack<Int>().also { it.push(0) }
        val toAddNodes = Stack<Node>()

        var tokenIndex = 0
        while (true) {
            val currentState = stack.peek()
            println("CURRENT STATE: $currentState")
            val token = tokens[tokenIndex]
            println("TOKEN: $token")

            when (val action = table.actionTable[currentState][token.type]) {
                is AcceptAction -> {
                    println("ACCEPT!")
                    val childrenReversed = toAddNodes.reversed()
                    return info.createRuleNode(0, currentState, childrenReversed, null).also { root ->
                        root.children.forEach { it.parent = root }
                    }
                }
                is ShiftAction -> {
                    println("SHIFT!")
                    shift(action, stack, toAddNodes, currentState)
                    tokenIndex++
                }
                is ReduceAction -> {
                    println("REDUCE!")
                    reduce(action, stack, toAddNodes, currentState)
                }
                null -> {
                    println("Failure at state: $currentState and token: ${token.content}, index: ${token.index}")
                    throw ParseFailureException()
                }
            }
        }
    }

    /**
     * Adds another state to the stack and add the token to the list of building nodes
     */
    private fun shift(action: ShiftAction, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int) {
        stack.push(action.state)
        toAddNodes.push(
            TerminalNode(currentState, null)
        )
    }

    /**
     * Reduces the number of states on the stack by the number of symbols in the matching rule. Also adds all the
     * building nodes as the children of the reduction node.
     */
    private fun reduce(action: ReduceAction, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int) {
        val rule = grammar.rules[action.rule]
        val children = mutableListOf<Node>()
        repeat(rule.items.size) {
            stack.pop()
            children.add(toAddNodes.pop())
        }
        val childrenReversed = children.reversed()
        val newNode = info.createRuleNode(action.rule, currentState, childrenReversed, null)
        println("FOUND NODE: $newNode")
        toAddNodes.push(newNode)
        val topState = stack.peek()
        val ruleIndex = table.nonTerminals.indexOf(rule.name)
        val goToState = table.goToTable[topState][ruleIndex] as ShiftAction
        stack.push(goToState.state)
    }

    internal class ParseFailureException : RuntimeException()
}
