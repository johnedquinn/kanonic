package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.parse.impl.KanonicParserDefault

public interface KanonicParser {

    public fun parse(input: String): Node

    public interface Builder {
        companion object {
            @JvmStatic
            public fun standard(): Builder {
                return KanonicParserDefault.Builder()
            }
        }

        fun withMetadata(metadata: ParserMetadata): Builder

        fun withLexer(lexer: KanonicLexer): Builder

        fun withGrammar(grammar: Grammar): Builder

        fun build(): KanonicParser
    }
}
