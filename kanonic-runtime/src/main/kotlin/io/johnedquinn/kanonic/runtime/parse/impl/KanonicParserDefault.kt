package io.johnedquinn.kanonic.runtime.parse.impl

import io.johnedquinn.kanonic.runtime.ast.GeneratedNode
import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.grammar.RuleVariant
import io.johnedquinn.kanonic.runtime.parse.Action
import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.runtime.parse.ParseTable
import io.johnedquinn.kanonic.runtime.parse.ParserSpecification
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
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

    // private val logger = KanonicLogger.getLogger()
    private val grammar = info.grammar
    private val tokens = grammar.tokens
    private val variants = grammar.rules.flatMap { it.variants }
    private val variantAlias = variants.associate {
        val parent = grammar.rules.find { rule -> it.parentName == rule.name }
        "${it.parentName}++${it.name}" to parent?.alias
    }
    private val ruleNameMap = grammar.rules.mapIndexed { index, rule -> index to rule }.associate { it.second.name to it.first }
    private val table: ParseTable = ParseTableDeserializer.deserialize(info.getTable(), grammar.tokens.size)

    /**
     * Parse
     */
    override fun parse(input: String): Node {
        val tokens = lexer.tokenize(input)
        // logger.fine("TOKENs:")
        // tokens.forEach { token ->
            // logger.fine(" - $token")
        // }
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

    private fun getAlias(variant: RuleVariant): String? = variantAlias["${variant.parentName}++${variant.name}"]

    private fun parse(tokens: Sequence<TokenLiteral>): Node {
        // Add first state
        val stack = Stack<Int>().also { it.push(0) }
        val toAddNodes = Stack<Node>()

        var tokenIndex = 0
        val tokenIter = tokens.iterator()
        var token = tokenIter.next()
        while (true) {
            val currentState = stack.peek()
            // logger.fine("CURRENT STATE: $currentState")
            // logger.fine("TOKEN: $token")
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
                    val childrenReversed = toAddNodes.toList().flatMap {
                        getChildren(it)
                    }
                    val result = info.createRuleNode(0, currentState, childrenReversed, null, null).also { root ->
                        root.children.forEach { it.parent = root }
                    }
                    getChildren(result).also {
                        if (it.size != 1) {
                            error("fff")
                        }
                        return it.first()
                    }
                }
                is Action.Shift -> {
                    val foundToken = when (updateToken) {
                        true -> {
                            val shiftToken = token
                            token = tokenIter.next()
                            // tokenIndex++
                            shiftToken
                        }
                        else -> TokenLiteral(TokenLiteral.ReservedTypes.EPSILON, token.index, "<epsilon>")
                    }
                    shift(action, stack, toAddNodes, currentState, foundToken)
                }
                is Action.Reduce -> {
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
                        this.tokens[tokenInd].name
                    }
                    // logger.severe("Failure at state: $currentState and token: ${token.content}, index: ${token.index}")
                    // logger.severe("Received $token, but expected token type of: $tokenNames")
                    throw RuntimeException("Failure at state: $currentState and token: ${token.content}, index: ${token.index}\n" + "Received $token, but expected token type of: $tokenNames")
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
            TerminalNode(currentState, null, token, token.type, null)
        )
    }

    /**
     * Reduces the number of states on the stack by the number of symbols in the matching rule. Also adds all the
     * building nodes as the children of the reduction node.
     */
    private fun reduce(action: Action.Reduce, stack: Stack<Int>, toAddNodes: Stack<Node>, currentState: Int) {
        val rule = variants[action.rule]
        val children = mutableListOf<Node>()
        val alias = getAlias(rule)
        repeat(rule.items.size) {
            stack.pop()
            val node = toAddNodes.pop()
            children.addAll(getChildren(node).reversed())
        }
        val childrenReversed = children.reversed()
        // logger.fine("REDUCE USING ACTION $action")
        val newNode = info.createRuleNode(action.rule, currentState, childrenReversed, null, alias)
        // logger.fine("FOUND NODE: $newNode")
        toAddNodes.push(newNode)
        val topState = stack.peek()
        val ruleIndex = ruleNameMap[rule.parentName] ?: error("Could not find rule index of ${rule.parentName}")
        val goToState = table.goToTable[topState][ruleIndex] as Action.Shift
        stack.push(goToState.state)
    }

    private fun getChildren(node: Node): List<Node> = when (node) {
        is GeneratedNode -> node.children.filterNot {
            it is TerminalNode && it.token.type == TokenLiteral.ReservedTypes.EPSILON
        }.flatMap {
            getChildren(it)
        }.also {
            if (node.alias != null) {
                it.map { child -> child.alias = node.alias }
            }
        }
        else -> listOf(node)
    }

    internal class ParseFailureException : RuntimeException()
}
