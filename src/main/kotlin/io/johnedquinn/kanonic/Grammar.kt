package io.johnedquinn.kanonic

data class Grammar(val rules: List<Rule>, val options: Options) {
    data class Options(
        val grammarName: String,
        val start: SymbolReference.RuleReference
    )

    fun getRules(ref: SymbolReference.RuleReference) = rules.filter { rule ->
        rule.name == ref.name
    }
}