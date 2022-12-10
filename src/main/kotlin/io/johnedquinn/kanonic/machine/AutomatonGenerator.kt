package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TokenType

internal class AutomatonGenerator {

    private val states = mutableMapOf<List<StateRule>, Int>()
    private val automatonStates = mutableListOf<State>()
    private val edges = mutableMapOf<Int, MutableList<Int>>()

    internal fun generate(grammar: Grammar): Automaton {

        // Create Start State
        val stateIndex = 0
        val kernel = createKernel(grammar, grammar.options.start)
        val closure = computeClosure(grammar, kernel)
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
            StateRule(rule, 0, setOf(TokenType.EOF))
        }.toMutableList()
        val kernel = State(states.size, kernelRules)
        automatonStates.add(kernel)
        return kernel
    }

    private fun createChildrenStates(grammar: Grammar, input: State): Map<SymbolReference, State> {
        // Grab rules that have transitions
        val remainingRules = input.rules.filter { stateRule -> hasMoreItems(stateRule) }

        // Find transitions
        val ruleMap: MutableMap<SymbolReference, MutableList<StateRule>> = mutableMapOf()
        remainingRules.forEach { stateRule ->
            val symbol = stateRule.plainRule.items[stateRule.position]
            val newRule = StateRule(
                plainRule = stateRule.plainRule,
                position = stateRule.position + 1,
                lookahead = stateRule.lookahead
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
        val added = mutableSetOf<RuleReference>()

        // Compute Closure
        var hasAdded = true
        while (hasAdded) {
            hasAdded = false
            val toAddRules = mutableListOf<StateRule>()
            closureRules.filter { stateRule ->
                hasMoreItems(stateRule)
            }.forEach { stateRule ->
                val symbolReference = getCurrentSymbol(stateRule)
                if (symbolReference is RuleReference && added.contains(symbolReference).not()) {
                    val rules = grammar.getRules(symbolReference)
                    val stateRules = rules.map { rule ->
                        StateRule(rule, 0, emptySet())
                    }
                    added.add(symbolReference)
                    toAddRules.addAll(stateRules)
                    hasAdded = true
                }
            }
            closureRules.addAll(toAddRules)
        }

        // TODO: Compute Lookaheads
        hasAdded = true
        while (hasAdded) {
            hasAdded = false
            closureRules.filter { stateRule ->
                hasMoreItems(stateRule)
            }.forEach { stateRule ->
                val currentSymbol = stateRule.plainRule.items[stateRule.position]
                // if (isRule(currentSymbol) )
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
