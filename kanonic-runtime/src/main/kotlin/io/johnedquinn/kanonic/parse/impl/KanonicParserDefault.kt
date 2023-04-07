package io.johnedquinn.kanonic.parse.impl

import io.johnedquinn.kanonic.machine.ParseTable
import io.johnedquinn.kanonic.machine.ParseTableSerializer
import io.johnedquinn.kanonic.parse.KanonicLexer
import io.johnedquinn.kanonic.parse.KanonicParser
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.ParserInternal
import io.johnedquinn.kanonic.parse.ParserMetadata
import io.johnedquinn.kanonic.utils.Logger

internal class KanonicParserDefault(
    private val info: ParserMetadata,
    private val lexer: KanonicLexer
) : KanonicParser {

    private val grammar = info.grammar

    // private val generator = AutomatonGenerator()
    // private val automaton = generator.generate(grammar)
    // private val table: ParseTable = TableGenerator(grammar, automaton).generate()

    private val table: ParseTable = ParseTableSerializer.deserialize(info.getTable(), grammar.tokens.size)

    init {
        Logger.debug(table.prettify(grammar))
    }

    override fun parse(input: String): Node {
        val parser = ParserInternal(grammar, table, info)
        val tokens = lexer.tokenize(input)
        Logger.debug("TOKENS: $tokens")
        return parser.parse(tokens)
    }

    class Builder : KanonicParser.Builder {
        lateinit var metadata: ParserMetadata
        lateinit var lexer: KanonicLexer

        override fun withMetadata(metadata: ParserMetadata): KanonicParser.Builder = this.apply {
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
}
