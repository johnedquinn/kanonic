package io.johnedquinn.kanonic

sealed class SymbolReference {
    data class RuleReference(val name: String) : SymbolReference()
    data class TerminalReference(val type: TokenType) : SymbolReference()
}