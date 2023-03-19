package io.johnedquinn.kanonic.parse.impl

import io.johnedquinn.kanonic.parse.KanonicLexer
import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral

internal class KanonicLexerDefault(private val definitions: List<TokenDefinition>) : KanonicLexer {
    public override fun tokenize(input: String): List<TokenLiteral> {
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
                    if (currentIndex > input.lastIndex) {
                        break
                    }
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
        tokens.add(TokenLiteral(type = TokenLiteral.ReservedTypes.EOF, input.length.toLong(), "<EOF>"))
        return tokens
    }

    internal class Builder : KanonicLexer.Builder {
        var definitions: List<TokenDefinition> = emptyList()

        override fun withDefinitions(
            definitions: List<TokenDefinition>
        ): KanonicLexer.Builder = this.apply { this.definitions = definitions }

        override fun build(): KanonicLexer = KanonicLexerDefault(definitions)
    }
}
