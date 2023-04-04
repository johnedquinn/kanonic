package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral
import java.util.logging.Logger

data class Grammar(val rules: List<Rule>, val options: Options, val tokens: List<TokenDefinition>) {
    private val firstSet by lazy { computeFirstSet() }
    private val followSet by lazy { computeFollowSet() }

    data class Options(
        val grammarName: String,
        var start: RuleReference,
        val packageName: String? = null,
    )

    fun getRules(ref: RuleReference) = rules.filter { rule ->
        rule.name == ref.name
    }

    internal fun computeFirst(symbolReference: SymbolReference) = when (symbolReference) {
        is TerminalReference -> setOf(symbolReference)
        is RuleReference -> firstSet[symbolReference]!!
    }

    internal fun computeFirst(refs: List<SymbolReference>): Set<TerminalReference> {
        val firsts = mutableSetOf<TerminalReference>()
        run wrapper@{
            refs.forEachIndexed { refIndex, ref ->
                val current = computeFirst(ref).toMutableSet()
                if (current.contains(TerminalReference(TokenLiteral.ReservedTypes.EPSILON)).not()) {
                    firsts.addAll(current)
                    return@wrapper
                } else {
                    val toAdd = when (refIndex != refs.lastIndex) {
                        true -> current - setOf(TerminalReference(TokenLiteral.ReservedTypes.EPSILON))
                        false -> current
                    }
                    firsts.addAll(toAdd)
                }
            }
        }
        return firsts
    }

    private fun computeFirstSet(): Map<SymbolReference, Set<TerminalReference>> {
        // Initialize first set
        val firsts = mutableMapOf<SymbolReference, MutableSet<Int>>()
        rules.forEach { rule ->
            firsts[RuleReference(rule.name)] = mutableSetOf()
        }

        while (true) {
            var changesMade = false

            rules.forEach ruleList@{ rule ->
                val ruleRef = RuleReference(rule.name)
                val firstsForRuleRef = mutableSetOf<Int>()

                // Check for empty rule
                if (rule.items.size == 1 && rule.items.first() == TerminalReference(TokenLiteral.ReservedTypes.EPSILON)) {
                    firstsForRuleRef.add(TokenLiteral.ReservedTypes.EPSILON)
                }

                // Loop through rule items
                run itemsWrap@{
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
                                when (firsts[item]?.contains(TokenLiteral.ReservedTypes.EPSILON)) {
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
                if (firsts[ruleRef]!!.addAll(firstsForRuleRef)) {
                    changesMade = true
                }
            }
            if (changesMade.not()) break
        }

        // Convert token type to TerminalReference
        return firsts.entries.associate { entry ->
            entry.key to entry.value.map { type -> TerminalReference(type) }.toSet()
        }
    }

    private fun computeFollowSet(): Map<SymbolReference, Set<TerminalReference>> {
        // Initialize follow set
        val follows = mutableMapOf<RuleReference, MutableSet<Int>>()
        rules.forEach { rule -> follows[RuleReference(rule.name)] = mutableSetOf() }
        follows[options.start]!!.add(TokenLiteral.ReservedTypes.EOF)

        while (true) {
            var changesMade = false
            rules.forEach ruleList@{ rule ->
                rule.items.forEachIndexed { itemIndex, item ->
                    if (item is TerminalReference) { return@forEachIndexed }
                    val remaining = rule.items.subList(itemIndex + 1, rule.items.size)
                    val nextFirsts = computeFirst(remaining).map { it.type }.toMutableSet()

                    when (remaining.isEmpty() || nextFirsts.contains(TokenLiteral.ReservedTypes.EPSILON)) {
                        true -> {
                            nextFirsts.remove(TokenLiteral.ReservedTypes.EPSILON)
                            if (follows[item]!!.addAll(nextFirsts)) { changesMade = true }
                            if (follows[item]!!.addAll(follows[RuleReference(rule.name)]!!)) { changesMade = true }
                        }
                        false -> {
                            if (follows[item]!!.addAll(nextFirsts)) { changesMade = true }
                        }
                    }
                }
            }
            if (changesMade.not()) break
        }
        // Convert token type to TerminalReference
        return follows.entries.associate { entry ->
            entry.key to entry.value.map { type -> TerminalReference(type) }.toSet()
        }
    }

    fun printInformation() {
        println("GRAMMAR (name: ${options.grammarName}, start: ${options.start.name})")
        printRules()
        printFirst()
        printFollow()
    }

    private fun printRules() {
        println("RULES:")
        rules.forEachIndexed { _, rule ->
            print("\t${rule.name} --> ")
            rule.items.forEachIndexed { itemIndex, item ->
                when (item) {
                    is RuleReference -> print(item.name)
                    is TerminalReference -> print(item.type)
                }
                if (itemIndex != rule.items.lastIndex) {
                    print(" - ")
                }
            }
            println()
        }
    }

    private fun printFirst() {
        println("FIRST SET")
        firstSet.forEach { entry ->
            println("\t${entry.key} ---> ${entry.value}")
        }
    }

    private fun printFollow() {
        println("FOLLOW SET")
        followSet.forEach { entry ->
            println("\t${entry.key} ---> ${entry.value}")
        }
    }
}
