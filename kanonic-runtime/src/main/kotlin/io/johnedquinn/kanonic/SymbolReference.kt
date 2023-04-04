package io.johnedquinn.kanonic

sealed class SymbolReference {
    abstract fun getName(grammar: Grammar): String
}
