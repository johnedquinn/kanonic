package io.johnedquinn.kanonic.runtime.grammar

import io.johnedquinn.kanonic.runtime.grammar.Grammar

sealed class SymbolReference {
    abstract fun getName(grammar: Grammar): String
}
