package io.johnedquinn.kanonic.runtime.parse

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.parse.impl.KanonicParserDefault

public interface KanonicParser {

    public fun parse(input: String): Node

    public interface Builder {
        companion object {
            @JvmStatic
            public fun standard(): Builder {
                return KanonicParserDefault.Builder()
            }
        }

        fun withSpecification(metadata: ParserSpecification): Builder

        fun withLexer(lexer: KanonicLexer): Builder

        fun build(): KanonicParser
    }
}
