package io.johnedquinn.kanonic

data class RuleReference(val name: String) : SymbolReference() {
    override fun getName(grammar: Grammar): String = name
}
