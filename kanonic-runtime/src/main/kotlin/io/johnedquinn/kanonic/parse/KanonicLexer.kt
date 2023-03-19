package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.parse.impl.KanonicLexerDefault

public interface KanonicLexer {
    public fun tokenize(input: String): List<TokenLiteral>

    public interface Builder {
        companion object {
            public fun standard(): Builder {
                return KanonicLexerDefault.Builder()
            }
        }

        fun withDefinitions(definitions: List<TokenDefinition>): Builder

        fun build(): KanonicLexer
    }
}
