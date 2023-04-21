package io.johnedquinn.kanonic.runtime.parse.impl

import io.johnedquinn.kanonic.runtime.ast.GeneratedNode
import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.parse.Action
import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.runtime.parse.ParseTable
import io.johnedquinn.kanonic.runtime.parse.ParserSpecification
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral

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
    private val tokens = grammar.tokens.toTypedArray()
    private val variants = grammar.rules.flatMap { it.variants }.toTypedArray()
    private val ruleNameMap = grammar.rules.mapIndexed { index, rule -> index to rule }.associate { it.second.name to it.first }
    private val table: ParseTable = ParseTableDeserializer.deserialize(info.getTable(), grammar.tokens.size)

    /**
     * Parse
     */
    override fun parse(input: String): Node {
        val tokens = lexer.tokenize(input)
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

    private fun parse(tokens: Sequence<TokenLiteral>): Node {
        // Add first state
        val stack = mutableListOf<Int>()
        val toAddNodes = mutableListOf<Node>()
        stack.add(0)

        val tokenIter = tokens.iterator()
        var token = tokenIter.next()
        while (true) {
            val currentState = stack.last()
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
                    val children = toAddNodes.flatMap { getChildren(it) }
                    val result = info.createRuleNode(0, currentState, children, null, null).also { root ->
                        root.children.forEach { it.parent = root }
                    }
                    getChildren(result).also {
                        if (it.size != 1) { error("fff") }
                        return it.first()
                    }
                }
                is Action.Shift -> {
                    when (updateToken) {
                        true -> {
                            shift(action, stack, toAddNodes, currentState, token)
                            token = tokenIter.next()
                        }
                        false -> stack.add(action.state)
                    }
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
                    throw RuntimeException("Failure at state: $currentState and token: ${token.content}, index: ${token.index}\n" + "Received $token, but expected token type of: $tokenNames")
                }
            }
        }
    }

    /**
     * Adds another state to the stack and add the token to the list of building nodes
     */
    private fun shift(action: Action.Shift, stack: MutableList<Int>, toAddNodes: MutableList<Node>, currentState: Int, token: TokenLiteral) {
        stack.add(action.state)
        toAddNodes.add(
            TerminalNode(currentState, null, token, token.type, null)
        )
    }

    /**
     * Reduces the number of states on the stack by the number of symbols in the matching rule. Also adds all the
     * building nodes as the children of the reduction node.
     */
    private fun reduce(action: Action.Reduce, stack: MutableList<Int>, toAddNodes: MutableList<Node>, currentState: Int) {
        val variant = variants[action.rule]
        val children = mutableListOf<Node>()
        repeat(variant.items.size) {
            stack.removeLast()
        }
        val firstIndex = toAddNodes.size - variant.normalizedSize
        for (i in firstIndex..toAddNodes.lastIndex) {
            children.addAll(getChildren(toAddNodes[i]))
        }
        toAddNodes.subList(firstIndex, toAddNodes.size).clear()
        val newNode = info.createRuleNode(action.rule, currentState, children, null, variant.alias)
        toAddNodes.add(newNode)
        val topState = stack.last()
        val ruleIndex = ruleNameMap[variant.parentName] ?: error("Could not find rule index of ${variant.parentName}")
        val goToState = table.goToTable[topState][ruleIndex] as Action.Shift
        stack.add(goToState.state)
    }

    private fun getChildren(node: Node): List<Node> = when (node) {
        !is GeneratedNode -> listOf(node)
        else -> node.children.flatMap {
            getChildren(it)
        }.also {
            if (node.alias != null) {
                it.map { child -> child.alias = node.alias }
            }
        }
    }
}
