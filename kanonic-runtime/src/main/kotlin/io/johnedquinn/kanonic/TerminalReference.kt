package io.johnedquinn.kanonic

data class TerminalReference(val type: Int) : SymbolReference() {
    override fun getName(grammar: Grammar): String {
        return grammar.tokens[type].name
    }
}
