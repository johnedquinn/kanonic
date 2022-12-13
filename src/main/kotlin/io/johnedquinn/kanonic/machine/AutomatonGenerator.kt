package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.*

internal class AutomatonGenerator {

    private val states = mutableMapOf<List<StateRule>, Int>()
    private val statesTemp = mutableSetOf<State>()
    private val automatonStates = mutableListOf<State>()
    private val edges = mutableMapOf<Int, MutableList<Int>>()

    internal fun generate(grammar: Grammar): Automaton {

        // Create Start State
        val stateIndex = 0
        val kernel = createKernel(grammar, grammar.options.start)
        val closure = computeClosure(grammar, kernel)
        automatonStates.add(closure)
        states[closure.rules] = stateIndex

        // Create Children & Edges
        val children = createChildrenStates(grammar, closure)
        addEdges(stateIndex, children)

        return Automaton(automatonStates, edges)
    }

    private fun createKernel(grammar: Grammar, start: RuleReference): State {
        val kernelRules = grammar.rules.filter { rule ->
            rule.name == start.name
        }.map { rule ->
            StateRule(rule, 0, mutableSetOf(TokenType.EOF))
        }.toMutableList()
        return State(states.size, kernelRules)
    }

    private fun createChildrenStates(grammar: Grammar, input: State): Map<SymbolReference, State> {
        // Grab rules that have transitions
        val remainingRules = input.rules.filter { stateRule -> hasMoreItems(stateRule) }

        // Find transitions
        val ruleMap: MutableMap<SymbolReference, MutableList<StateRule>> = mutableMapOf()
        remainingRules.forEach { stateRule ->
            val symbol = stateRule.plainRule.items[stateRule.position]
            val newLookahead = mutableSetOf<TokenType>().also { it.addAll(stateRule.lookahead) }
            val newRule = StateRule(
                plainRule = stateRule.plainRule,
                position = stateRule.position + 1,
                lookahead = newLookahead
            )
            when (ruleMap.contains(symbol)) {
                false -> ruleMap[symbol] = mutableListOf(newRule)
                true -> ruleMap[symbol]!!.add(newRule)
            }
        }

        // Recursively create next states
        val nextStates: MutableMap<SymbolReference, State> = mutableMapOf()
        ruleMap.forEach { (ref, ruleList) ->
            val nextKernel = State(states.size, ruleList)
            val nextClosure = computeClosure(grammar, nextKernel)
            val nextStateIndex = states.size
            when (states.contains(nextClosure.rules)) {
                true -> {
                    val targetIndex = states[nextClosure.rules]!!
                    addEdge(input.index, targetIndex)
                    return@forEach
                }
                false -> {
                    states[nextClosure.rules] = nextStateIndex
                    automatonStates.add(nextClosure)
                }
            }
            val children = createChildrenStates(grammar, nextClosure)
            addEdges(nextStateIndex, children)
            nextStates[ref] = nextClosure
        }
        return nextStates
    }

    private fun computeClosure(grammar: Grammar, kernel: State): State {
        val closureRules = kernel.rules.toMutableList()
        val kernelRuleLastIndex = kernel.rules.lastIndex
        val added = mutableSetOf<RuleReference>()

        // Compute Closure
        var hasModified = true
        while (hasModified) {
            hasModified = false
            val toAddRules = mutableListOf<StateRule>()
            closureRules.filter { stateRule ->
                hasMoreItems(stateRule)
            }.forEach { stateRule ->
                val symbolReference = getCurrentSymbol(stateRule)
                if (symbolReference !is RuleReference || added.contains(symbolReference)) { return@forEach }
                val rules = grammar.getRules(symbolReference)
                val lookahead = mutableSetOf<TokenType>()
                val stateRules = rules.map { rule -> StateRule(rule, 0, lookahead) }
                added.add(symbolReference)
                toAddRules.addAll(stateRules)
                hasModified = true

            }
            closureRules.addAll(toAddRules)
        }

        hasModified = true
        while (hasModified) {
            hasModified = false
            closureRules.filter { stateRule ->
                hasMoreItems(stateRule)
            }.forEach { stateRule ->
                val symbolReference = getCurrentSymbol(stateRule)
                if (symbolReference !is RuleReference) {
                    return@forEach
                }

                // Calculate Lookahead
                val remainingItems = stateRule.plainRule.items.subList(stateRule.position + 1, stateRule.plainRule.items.size)
                val remainingFirstSet = grammar.computeFirst(remainingItems).map { it.type }.toMutableSet()
                val lookaheadAdder = when {
                    // For an item like A → α.B with a lookahead of {L},
                    //  add new rules like B → .γ with a lookahead of {L}.
                    remainingItems.isEmpty() -> stateRule.lookahead
                    // For an item like A → α.Bβ, with a lookahead of {L}, and If β CANNOT produce ε,
                    //  add new rules like B → .γ with a lookahead as follows: the lookahead is FIRST(β).
                    remainingFirstSet.contains(TokenType.EPSILON).not() -> remainingFirstSet
                    // For an item like A → α.Bβ, with a lookahead of {L}, and if β CAN produce ε
                    //  add new rules like B → .γ with a lookahead as follows: the lookahead is FIRST(β) ∪{L}.
                    else -> remainingFirstSet.union(stateRule.lookahead).toMutableSet()
                }
                val lookahead = mutableSetOf<TokenType>().also { it.addAll(lookaheadAdder) }

                // Modify Relevant Rules
                val toModifyRules = closureRules.filterIndexed { index, closureRule ->
                    closureRule.plainRule.name == symbolReference.name && index > kernelRuleLastIndex
                }
                toModifyRules.forEach { toModifyRule ->
                    if (toModifyRule.lookahead.addAll(lookahead)) hasModified = true
                }
            }
        }

        return State(states.size, closureRules)
    }

    private fun addEdges(srcIndex: Int, children: Map<SymbolReference, State>) = children.forEach { (_, child) ->
        addEdge(srcIndex, child.index)
    }

    private fun addEdge(srcIndex: Int, childIndex: Int) = when (edges.contains(srcIndex)) {
        true -> edges[srcIndex]!!.add(childIndex)
        false -> edges[srcIndex] = mutableListOf(childIndex)
    }

    private fun hasMoreItems(stateRule: StateRule): Boolean = stateRule.position <= stateRule.plainRule.items.lastIndex

    private fun getCurrentSymbol(stateRule: StateRule) = stateRule.plainRule.items[stateRule.position]
}
