package io.johnedquinn.kanonic

data class Grammar(val rules: List<Rule>, val options: Options) {
    internal val firstSet = computeFirstSet()

    data class Options(
        val grammarName: String,
        val start: RuleReference
    )

    fun getRules(ref: RuleReference) = rules.filter { rule ->
        rule.name == ref.name
    }

    internal fun computeFirst(symbolReference: SymbolReference) = when (symbolReference) {
        is TerminalReference -> setOf(symbolReference)
        is RuleReference -> firstSet[symbolReference]!!
    }

    private fun computeFirstSet(): Map<SymbolReference, Set<TerminalReference>> {
        val firsts = mutableMapOf<SymbolReference, MutableSet<TokenType>>()

        while (true) {
            var changesMade = false

            rules.forEach ruleList@{ rule ->
                val ruleRef = RuleReference(rule.name)
                val firstsForRuleRef = mutableSetOf<TokenType>()

                // Check for empty rule
                if (rule.items.size == 1 && rule.items.first() == TerminalReference(TokenType.EPSILON)) {
                    firstsForRuleRef.add(TokenType.EPSILON)
                }

                // Loop through rule items
                run itemsWrap@ {
                    rule.items.forEach { item ->
                        when (item) {
                            // Break on terminals
                            is TerminalReference -> {
                                firstsForRuleRef.add(item.type)
                                return@itemsWrap
                            }
                            // Check for empty states
                            // TODO: Do we add all of first from epsilon states? Or, do we just add the epsilon?
                            is RuleReference -> {
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

        // Convert token type to TerminalReference
        return firsts.entries.associate { entry ->
            entry.key to entry.value.map { type -> TerminalReference(type) }.toSet()
        }
    }

}