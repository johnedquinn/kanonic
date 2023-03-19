package io.johnedquinn.kanonic.parse.impl

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.machine.AutomatonGenerator
import io.johnedquinn.kanonic.machine.ParseTable
import io.johnedquinn.kanonic.machine.TableGenerator
import io.johnedquinn.kanonic.parse.KanonicLexer
import io.johnedquinn.kanonic.parse.KanonicParser
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.ParserInternal
import io.johnedquinn.kanonic.parse.ParserMetadata

internal class KanonicParserDefault(
    private val grammar: Grammar,
    private val info: ParserMetadata,
    private val lexer: KanonicLexer
) : KanonicParser {

    private val generator = AutomatonGenerator()
    private val automaton = generator.generate(grammar)
    private val table: ParseTable = TableGenerator(grammar, automaton).generate()

    init {
        println(table.prettify(grammar))
    }

    override fun parse(input: String): Node {
        val parser = ParserInternal(grammar, table, info)
        val tokens = lexer.tokenize(input)
        println("TOKENS: $tokens")
        return parser.parse(tokens)
    }

    class Builder : KanonicParser.Builder {
        lateinit var metadata: ParserMetadata
        lateinit var lexer: KanonicLexer
        lateinit var grammar: Grammar

        override fun withMetadata(metadata: ParserMetadata): KanonicParser.Builder = this.apply {
            this.metadata = metadata
        }

        override fun withLexer(lexer: KanonicLexer): KanonicParser.Builder = this.apply {
            this.lexer = lexer
        }

        override fun withGrammar(grammar: Grammar): KanonicParser.Builder = this.apply {
            this.grammar = grammar
        }

        override fun build(): KanonicParser {
            if (this::lexer.isInitialized.not()) {
                lexer = KanonicLexerDefault(grammar.tokens)
            }
            return KanonicParserDefault(grammar, metadata, lexer)
        }
    }
}
