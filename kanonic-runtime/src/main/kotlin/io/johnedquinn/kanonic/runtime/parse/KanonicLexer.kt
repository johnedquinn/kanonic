package io.johnedquinn.kanonic.runtime.parse

import io.johnedquinn.kanonic.runtime.grammar.TokenDefinition
import io.johnedquinn.kanonic.runtime.parse.impl.KanonicLexerDefault

public interface KanonicLexer {
    public fun tokenize(input: String): Sequence<TokenLiteral>

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
