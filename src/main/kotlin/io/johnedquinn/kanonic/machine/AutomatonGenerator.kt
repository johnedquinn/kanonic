package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TokenType

class AutomatonGenerator {

    private val states = mutableSetOf<List<StateRule>>()

    fun generate(grammar: Grammar): Automaton {

        val kernel = createKernel(grammar, grammar.options.start)
        val closure = computeClosure(grammar, kernel)
        states.add(closure.rules)
        val children = createNextKernels(grammar, closure)
        closure.children = children

        println("STATES")
        states.forEachIndexed { index, stateRules ->
            println("STATE_$index")
            stateRules.forEachIndexed { ruleIndex, stateRule ->
               println("  RULE_$ruleIndex: $stateRule")
            }
        }

        return Automaton(listOf(closure))
    }

    private fun createKernel(grammar: Grammar, start: SymbolReference.RuleReference): State {
        val kernelRules = grammar.rules.filter { rule ->
            rule.name == start.name
        }.map { rule ->
            StateRule(rule, 0)
        }.toMutableList()
        return State(kernelRules)
    }

    private fun createNextKernels(grammar: Grammar, input: State): Map<SymbolReference, State> {
        val remainingRules = input.rules.filter { stateRule ->
            stateRule.position < stateRule.rule.items.lastIndex
        }

        val lastRules = input.rules.filter { stateRule ->
            stateRule.position == stateRule.rule.items.lastIndex
        }

        val ruleMap: MutableMap<SymbolReference, MutableList<StateRule>> = mutableMapOf()
        remainingRules.forEach { stateRule ->
            val symbol = stateRule.rule.items[stateRule.position]
            val newRule = StateRule(
                stateRule.rule,
                stateRule.position + 1
            )
            when (ruleMap.contains(symbol)) {
                false -> ruleMap[symbol] = mutableListOf(newRule)
                true -> ruleMap[symbol]!!.add(newRule)
            }
        }

        // Recursively create next state
        val nextStates: MutableMap<SymbolReference, State> = mutableMapOf()
        ruleMap.forEach { (ref, ruleList) ->
            val nextKernel = State(ruleList)
            val nextClosure = computeClosure(grammar, nextKernel)
            when (states.contains(nextClosure.rules)) {
                true -> return@forEach
                false -> states.add(nextClosure.rules)
            }
            val children = createNextKernels(grammar, nextClosure)
            nextClosure.children = children
            nextStates[ref] = nextClosure
        }
        states.add(lastRules)
        nextStates[SymbolReference.TerminalReference(TokenType.EPSILON)] = State(lastRules)
        return nextStates
    }

    private fun computeClosure(grammar: Grammar, kernel: State): State {
        val closureRules = kernel.rules.toMutableList()
        val added = mutableSetOf<SymbolReference.RuleReference>()

        // Compute Closure
        var hasAdded = true
        while (hasAdded) {
            hasAdded = false
            val toAddRules = mutableListOf<StateRule>()
            closureRules.forEach { stateRule ->
                val symbolReference = stateRule.rule.items[stateRule.position]
                if (symbolReference is SymbolReference.RuleReference && added.contains(symbolReference).not()) {
                    val rules = grammar.getRules(symbolReference)
                    val stateRules = rules.map { rule ->
                        StateRule(rule, 0)
                    }
                    added.add(symbolReference)
                    toAddRules.addAll(stateRules)
                    hasAdded = true
                }
            }
            closureRules.addAll(toAddRules)
        }

        return State(closureRules)
    }
}