package io.johnedquinn.kanonic.runtime.parse.impl

import io.johnedquinn.kanonic.runtime.parse.KanonicLexer
import io.johnedquinn.kanonic.runtime.grammar.TokenDefinition
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
import io.johnedquinn.kanonic.runtime.utils.KanonicLogger

internal class KanonicLexerDefault(private val definitions: List<TokenDefinition>) : KanonicLexer {
    public override fun tokenize(input: String): List<TokenLiteral> {
        val tokens = mutableListOf<TokenLiteral>()
        var currentIndex = 0
        var tokenStartIndex: Int
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
                val regex = it.def.toRegex()
                KanonicLogger.debug(" - Def: $${it.def} - Regex: $regex")
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
                if (it.hidden.not()) {
                    tokens.add(tokenLiteral)
                }
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
