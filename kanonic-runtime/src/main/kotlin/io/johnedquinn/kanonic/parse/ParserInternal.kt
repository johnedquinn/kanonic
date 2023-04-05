package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.machine.AcceptAction
import io.johnedquinn.kanonic.machine.ParseTable
import io.johnedquinn.kanonic.machine.ReduceAction
import io.johnedquinn.kanonic.machine.ShiftAction
import io.johnedquinn.kanonic.utils.Logger
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
            Logger.debug("CURRENT STATE: $currentState")
            val token = tokens[tokenIndex]
            Logger.debug("TOKEN: $token")
            var updateToken = true
            val action = when (val pre = table.actionTable[currentState][token.type]) {
                null -> {
                    updateToken = false
                    table.actionTable[currentState][TokenLiteral.ReservedTypes.EPSILON]
                }
                else -> pre
            }

            when (action) {
                is AcceptAction -> {
                    Logger.debug("ACCEPT!")
                    val childrenReversed = toAddNodes.toList().flatMap {
                        getChildren(it)
                    }
                    return info.createRuleNode(0, currentState, childrenReversed, null).also { root ->
                        root.children.forEach { it.parent = root }
                    }
                }
                is ShiftAction -> {
                    Logger.debug("SHIFT!")
                    val foundToken = when (updateToken) {
                        true -> {
                            tokenIndex++
                            token
                        }
                        else -> TokenLiteral(TokenLiteral.ReservedTypes.EPSILON, token.index, "<epsilon>")
                    }
                    shift(action, stack, toAddNodes, currentState, foundToken)
                }
                is ReduceAction -> {
                    Logger.debug("REDUCE!")
                    reduce(action, stack, toAddNodes, currentState)
                }
                null -> {
                    // TODO: Add expected tokens
                    val possible = mutableSetOf<Int>()
                    table.actionTable[currentState].forEachIndexed { index, act ->
                        if (act != null) { possible.add(index) }
                    }
                    table.goToTable[currentState].forEachIndexed { index, act ->
                        if (act != null) { possible.add(index) }
                    }
                    val tokenNames = possible.map { tokenInd ->
                        grammar.tokens[tokenInd].name
                    }
                    Logger.error("Failure at state: $currentState and token: ${token.content}, index: ${token.index}")
                    Logger.error("Received $token, but expected token type of: $tokenNames")
                    throw ParseFailureException()
                }
            }
        }
    }

    /**
     * Adds another state to the stack and add the token to the list of building nodes
     */
    private fun shift(action: ShiftAction, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int, token: TokenLiteral) {
        stack.push(action.state)
        toAddNodes.push(
            TerminalNode(currentState, null, token)
        )
    }

    /**
     * Reduces the number of states on the stack by the number of symbols in the matching rule. Also adds all the
     * building nodes as the children of the reduction node.
     */
    private fun reduce(action: ReduceAction, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int) {
        val rule = grammar.rules.flatMap { it.variants }[action.rule]
        val children = mutableListOf<Node>()
        repeat(rule.items.size) {
            stack.pop()
            val node = toAddNodes.pop()
            children.addAll(getChildren(node).reversed())
        }
        val childrenReversed = children.reversed()
        Logger.debug("REDUCE USING ACTION $action")
        val newNode = info.createRuleNode(action.rule, currentState, childrenReversed, null)
        Logger.debug("FOUND NODE: $newNode")
        toAddNodes.push(newNode)
        val topState = stack.peek()
        val ruleIndex = table.nonTerminals.indexOf(rule.parentName)
        val goToState = table.goToTable[topState][ruleIndex] as ShiftAction
        stack.push(goToState.state)
    }

    private fun getChildren(node: Node): List<Node> = when (node) {
        is GeneratedNode -> node.children.filterNot {
            it is TerminalNode && it.token.type == TokenLiteral.ReservedTypes.EPSILON
        }.flatMap {
            getChildren(it)
        }
        else -> listOf(node)
    }

    internal class ParseFailureException : RuntimeException()
}
