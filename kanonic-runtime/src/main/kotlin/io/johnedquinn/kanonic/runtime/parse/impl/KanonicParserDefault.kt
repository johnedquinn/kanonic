package io.johnedquinn.kanonic.runtime.parse.impl

import io.johnedquinn.kanonic.runtime.parse.ParseTable
import io.johnedquinn.kanonic.runtime.parse.ParseTableSerializer
import io.johnedquinn.kanonic.runtime.ast.GeneratedNode
import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.parse.ParserSpecification
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.parse.Action
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
import io.johnedquinn.kanonic.runtime.utils.Logger
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
internal class KanonicParserDefault(
    private val info: ParserSpecification,
    private val lexer: KanonicLexer
) : KanonicParser {

    private val grammar = info.grammar
    private val table: ParseTable = ParseTableSerializer.deserialize(info.getTable(), grammar.tokens.size)

    /**
     * Parse
     */
    override fun parse(input: String): Node {
        val tokens = lexer.tokenize(input)
        Logger.debug("TOKENS: $tokens")
        return parse(tokens)
    }

    /**
     * Builder
     */
    class Builder : KanonicParser.Builder {
        lateinit var metadata: ParserSpecification
        lateinit var lexer: KanonicLexer

        override fun withSpecification(metadata: ParserSpecification): KanonicParser.Builder = this.apply {
            this.metadata = metadata
        }

        override fun withLexer(lexer: KanonicLexer): KanonicParser.Builder = this.apply {
            this.lexer = lexer
        }

        override fun build(): KanonicParser {
            if (this::lexer.isInitialized.not()) {
                lexer = KanonicLexerDefault(metadata.grammar.tokens)
            }
            return KanonicParserDefault(metadata, lexer)
        }
    }

    private fun parse(tokens: List<TokenLiteral>): Node {
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
                is Action.Accept -> {
                    Logger.debug("ACCEPT!")
                    val childrenReversed = toAddNodes.toList().flatMap {
                        getChildren(it)
                    }
                    return info.createRuleNode(0, currentState, childrenReversed, null).also { root ->
                        root.children.forEach { it.parent = root }
                    }
                }
                is Action.Shift -> {
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
                is Action.Reduce -> {
                    Logger.debug("REDUCE!")
                    reduce(action, stack, toAddNodes, currentState)
                }
                null -> {
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
    private fun shift(action: Action.Shift, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int, token: TokenLiteral) {
        stack.push(action.state)
        toAddNodes.push(
            TerminalNode(currentState, null, token)
        )
    }

    /**
     * Reduces the number of states on the stack by the number of symbols in the matching rule. Also adds all the
     * building nodes as the children of the reduction node.
     */
    private fun reduce(action: Action.Reduce, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int) {
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
        val ruleIndex = grammar.rules.map { it.name }.indexOf(rule.parentName)
        val goToState = table.goToTable[topState][ruleIndex] as Action.Shift
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
