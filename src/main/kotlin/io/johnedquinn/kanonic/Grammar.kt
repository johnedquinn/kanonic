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

        while (true) {
            var changesMade = false

            rules.forEach ruleList@{ rule ->
                val ruleRef = SymbolReference.RuleReference(rule.name)
                val firstsForRuleRef = mutableSetOf<TokenType>()

                // Check for empty rule
                if (rule.items.size == 1 && rule.items.first() == SymbolReference.TerminalReference(TokenType.EPSILON)) {
                    firstsForRuleRef.add(TokenType.EPSILON)
                }

                // Loop through rule items
                run itemsWrap@ {
                    rule.items.forEach { item ->
                        when (item) {
                            // Break on terminals
                            is SymbolReference.TerminalReference -> {
                                firstsForRuleRef.add(item.type)
                                return@itemsWrap
                            }
                            // Check for empty states
                            // TODO: Do we add all of first from epsilon states? Or, do we just add the epsilon?
                            is SymbolReference.RuleReference -> {
                                when (firsts[item]?.contains(TokenType.EPSILON)) {
                                    true -> firstsForRuleRef.addAll(firsts[item]!!)
                                    false -> {
                                        firstsForRuleRef.addAll(firsts[item]!!)
                                        return@itemsWrap
                                    }
                                    null -> { /* Do nothing */
                                    }
                                }
                            }
                        }
                    }
                }

                // Add results and check for changes made
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

    internal fun computeFirst(symbolReference: SymbolReference) = when (symbolReference) {
        is SymbolReference.TerminalReference -> setOf(symbolReference)
        is SymbolReference.RuleReference -> firstSet[symbolReference]
    }
}