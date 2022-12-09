package io.johnedquinn.kanonic

data class Grammar(val rules: List<Rule>, val options: Options) {
    internal val firstSet = computeFirstSet()

    data class Options(
        val grammarName: String,
        val start: SymbolReference.RuleReference
    )

    fun getRules(ref: SymbolReference.RuleReference) = rules.filter { rule ->
        rule.name == ref.name
    }

    private fun computeFirstSet(): Map<SymbolReference, Set<TokenType>> {
        val firsts = mutableMapOf<SymbolReference, MutableSet<TokenType>>()

        // Find Duplicate Definition Rules
        val duplicateDefinedRules = mutableMapOf<SymbolReference, MutableList<Rule>>()
        rules.forEach { rule ->
            val ruleRef = SymbolReference.RuleReference(rule.name)
            when (val duplicates = duplicateDefinedRules[ruleRef]) {
                null -> duplicateDefinedRules[ruleRef] = mutableListOf(rule)
                else -> duplicates.add(rule)
            }
        }

        // Continue while changes still being made
        while (true) {
            var changesMade = false
            duplicateDefinedRules.forEach { (ruleRef, ruleList) ->
                println(ruleRef)
                val firstsForRuleRef = mutableSetOf<TokenType>()
                ruleList.forEach ruleList@{ rule ->

                    // Check for empty rule
                    if (rule.items.size == 1 && rule.items.first() == SymbolReference.TerminalReference(TokenType.EPSILON)) {
                        firstsForRuleRef.add(TokenType.EPSILON)
                        return@ruleList
                    }

                    // Loop through items
                    rule.items.forEach { item ->
                        when (item) {

                            // Break on terminals
                            is SymbolReference.TerminalReference -> {
                                firstsForRuleRef.add(item.type)
                                return@ruleList
                            }

                            // Check for empty states
                            is SymbolReference.RuleReference -> {
                                when (firsts[item]?.contains(TokenType.EPSILON)) {
                                    true -> firstsForRuleRef.addAll(firsts[item]!!)
                                    false -> {
                                        firstsForRuleRef.addAll(firsts[item]!!)
                                        return@ruleList
                                    }
                                    null -> { /* Do nothing */
                                    }
                                }
                            }
                        }
                    }
                }

                // Add Results
                when (firsts[ruleRef]) {
                    null -> {
                        firsts[ruleRef] = firstsForRuleRef
                        changesMade = true
                    }
                    else -> {
                        if (firsts[ruleRef]!!.addAll(firstsForRuleRef)) {
                            changesMade = true
                        }
                    }
                }
            }
            if (changesMade.not()) break
        }
        return firsts
    }

    fun computeFirst(symbolReference: SymbolReference) = when (symbolReference) {
        is SymbolReference.TerminalReference -> setOf(symbolReference)
        is SymbolReference.RuleReference -> firstSet[symbolReference]
    }
}