package io.johnedquinn.kanonic.runtime.grammar

data class RuleReference(val name: String) : SymbolReference() {
    override fun getName(grammar: Grammar): String = name
}
