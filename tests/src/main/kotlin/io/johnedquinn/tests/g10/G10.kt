package io.johnedquinn.tests.g10

import io.johnedquinn.kanonic.dsl.grammar
import io.johnedquinn.kanonic.machine.AutomatonGenerator
import io.johnedquinn.kanonic.machine.ParseTable
import io.johnedquinn.kanonic.machine.TableGenerator
import io.johnedquinn.kanonic.parse.CreateNode
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.Parser
import io.johnedquinn.kanonic.parse.ParserInfo
import io.johnedquinn.kanonic.parse.ParserInternal
import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral
import kotlin.Int
import kotlin.collections.List

public class G10Parser : Parser {

    override val info: ParserInfo = G10Info()

    override val grammar = grammar("G10", "P") {
        tokens {
            "IDENTIFIER" - "[a-zA-Z]+"
            "PAREN_LEFT" - "\\("
            "PAREN_RIGHT" - "\\)"
            "PLUS" - "\\+"
        }
        "p" eq "e" alias "Root"
        "e" eq "e" - "PLUS" - "t" alias "ExprPlus"
        "e" eq "t" alias "ExprFall"
        "t" eq "IDENTIFIER" - "PAREN_LEFT" - "e" - "PAREN_RIGHT" alias "Index"
        "t" eq "IDENTIFIER" alias "Ident"
    }.toGrammar()

    private val generator = AutomatonGenerator()

    private val lexer: G10Lexer = G10Lexer(grammar.tokens)

    private val automaton = generator.generate(grammar)

    override val table: ParseTable = TableGenerator(grammar, automaton).generate()

    override fun parse(input: String): Node {
        val parser = ParserInternal(grammar, table, info)
        val tokens = lexer.tokenize(input)
        return parser.parse(tokens)
    }
}

internal class G10Lexer(private val definitions: List<TokenDefinition>) {
    internal fun tokenize(input: String): List<TokenLiteral> {
        val tokens = mutableListOf<TokenLiteral>()
        var currentIndex = 0
        var tokenStartIndex = 0
        var currentString = ""
        while (currentIndex < input.length) {
            currentString += input[currentIndex]
            if (currentString.isBlank()) {
                currentString = ""
                currentIndex++
                continue
            }
            definitions.firstOrNull {
                currentString.matches(it.def.toRegex())
            }?.let {
                tokenStartIndex = currentIndex
                while (currentString.matches(it.def.toRegex())) {
                    currentIndex++
                    if (currentIndex > input.lastIndex) { break }
                    currentString += input[currentIndex]
                }
                // TODO: Adjust index
                val actualString = when (currentIndex <= input.lastIndex) {
                    true -> currentString.dropLast(1)
                    false -> currentString
                }
                val tokenLiteral = TokenLiteral(it.index, tokenStartIndex.toLong(), actualString)
                tokens.add(tokenLiteral)
                currentIndex--
                currentString = ""
            }
            currentIndex++
        }
        return tokens
    }
}

public class G10Info : ParserInfo {
    private val nodeLambdaList: List<CreateNode> = initializeLambdas()

    private fun initializeLambdas(): List<CreateNode> = buildList {
        add(CreateNode { state, children, parent -> G10Node.PNode.RootNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.ENode.ExprPlusNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.ENode.ExprFallNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.TNode.IndexNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.TNode.IdentNode(state, children, parent) })
    }

    public override fun createRuleNode(
        index: Int,
        state: Int,
        children: List<Node>,
        parent: Node?,
    ): Node {
        val nodeCreator = nodeLambdaList[index]
        return nodeCreator.create(state, children, parent)
    }
}

internal sealed class G10Node(
    state: Int,
    children: List<Node>,
    parent: Node?,
) : Node(state, children, parent) {
    internal sealed class PNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : G10Node(state, children, parent) {
        internal class RootNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : PNode(state, children, parent)
    }

    internal sealed class ENode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : G10Node(state, children, parent) {
        internal class ExprPlusNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : ENode(state, children, parent)

        internal class ExprFallNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : ENode(state, children, parent)
    }

    internal sealed class TNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : G10Node(state, children, parent) {
        internal class IndexNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : TNode(state, children, parent)

        internal class IdentNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : TNode(state, children, parent)
    }
}
