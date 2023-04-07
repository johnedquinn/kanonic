package io.johnedquinn.kanonic.runtime.grammar

data class TerminalReference(val type: Int) : SymbolReference() {
    override fun getName(grammar: Grammar): String {
        return grammar.tokens[type].name
    }
}
