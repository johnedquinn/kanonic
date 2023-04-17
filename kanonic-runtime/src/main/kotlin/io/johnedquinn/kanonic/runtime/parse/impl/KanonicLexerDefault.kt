package io.johnedquinn.kanonic.runtime.parse.impl

import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.kanonic.runtime.grammar.TokenDefinition
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral

internal class KanonicLexerDefault(private val definitions: List<TokenDefinition>) : KanonicLexer {
    public override fun tokenize(input: String): Sequence<TokenLiteral> {
        var currentIndex = 0
        var tokenStartIndex: Int
        var currentString = ""
        return sequence {
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
                    var matching: TokenDefinition? = it
                    var result: TokenDefinition = it
                    while (matching != null) {
                        result = matching
                        currentIndex++
                        if (currentIndex > input.lastIndex) {
                            break
                        }
                        currentString += input[currentIndex]
                        matching = definitions.firstOrNull { def -> currentString.matches(def.def.toRegex()) }
                    }
                    // TODO: Adjust index
                    val actualString = when (currentIndex <= input.lastIndex) {
                        true -> currentString.dropLast(1)
                        false -> currentString
                    }
                    val tokenLiteral = TokenLiteral(result.index, tokenStartIndex.toLong(), actualString)
                    if (result.hidden.not()) {
                        yield(tokenLiteral)
                    }
                    currentIndex--
                    currentString = ""
                }
                currentIndex++
            }
            yield(TokenLiteral(type = TokenLiteral.ReservedTypes.EOF, input.length.toLong(), "<EOF>"))
        }
    }

    internal class Builder : KanonicLexer.Builder {
        var definitions: List<TokenDefinition> = emptyList()

        override fun withDefinitions(
            definitions: List<TokenDefinition>
        ): KanonicLexer.Builder = this.apply { this.definitions = definitions }

        override fun build(): KanonicLexer = KanonicLexerDefault(definitions)
    }
}
