package io.johnedquinn.kanonic.gen.impl

import io.johnedquinn.kanonic.runtime.grammar.Grammar
import io.johnedquinn.kanonic.runtime.grammar.RuleReference
import io.johnedquinn.kanonic.runtime.grammar.TerminalReference
import io.johnedquinn.kanonic.runtime.parse.Action
import io.johnedquinn.kanonic.runtime.parse.ParseTable

class TableGenerator(val grammar: Grammar, val automaton: Automaton) {

    // Constants
    private val numberOfStates = automaton.states.size
    private val numberOfTerminals = grammar.tokens.size
    private val nonTerminals = grammar.rules.map { it.name }.toList()
    private val numberOfNonTerminals = nonTerminals.size
    private val namesToNonTerminalIndex = nonTerminals.mapIndexed { index, ruleName ->
        ruleName to index
    }.associate { it }

    // Tables
    val actionTable: List<MutableList<Action?>> = List(numberOfStates) { MutableList(numberOfTerminals) { null } }
    val goToTable: List<MutableList<Action?>> = List(numberOfStates) { MutableList(numberOfNonTerminals) { null } }

    fun generate(): ParseTable {
        automaton.states.forEachIndexed { stateIndex, state ->
            val edges = automaton.edges[state.index] ?: emptyList()
            edges.forEach { edgeTarget ->
                when (val edgeSymbol = edgeTarget.edge) {
                    // Add Shifts
                    is TerminalReference -> {
                        actionTable[stateIndex][edgeSymbol.type]?.let { foundAction ->
                            when (foundAction) {
                                is Action.Shift -> throw RuntimeException("Shift-shift conflict!")
                                is Action.Reduce -> throw RuntimeException("Shift-reduce conflict!")
                                else -> TODO()
                            }
                        }
                        actionTable[stateIndex][edgeSymbol.type] = Action.Shift(edgeTarget.targetState)
                    }
                    // Add GoTo
                    is RuleReference -> {
                        val nonTerminalIndex = namesToNonTerminalIndex[edgeSymbol.name]
                            ?: error("Couldn't find index of ${edgeSymbol.name}")
                        goToTable[stateIndex][nonTerminalIndex]?.let { foundAction ->
                            when (foundAction) {
                                is Action.Shift -> throw RuntimeException("Shift-shift conflict!")
                                is Action.Reduce -> throw RuntimeException("Shift-reduce conflict!")
                                else -> TODO()
                            }
                        }
                        goToTable[stateIndex][nonTerminalIndex] = Action.Shift(edgeTarget.targetState)
                    }
                }
            }

            // Add Reduce and Accept
            state.rules.forEach { rule ->
                if (rule.position <= rule.plainRule.items.lastIndex) return@forEach
                val ruleIndex = grammar.rules.flatMap { it.variants }.indexOfFirst { it == rule.plainRule }
                rule.lookahead.forEach { lookaheadToken ->
                    when (rule.plainRule.parentName == grammar.options.start.name) {
                        true -> {
                            actionTable[stateIndex][lookaheadToken] = Action.Accept(lookaheadToken)
                        }
                        false -> {
                            actionTable[stateIndex][lookaheadToken]?.let { foundAction ->
                                when (foundAction) {
                                    is Action.Shift -> throw RuntimeException("Shift-reduce conflict! For ${grammar.tokens[lookaheadToken]}")
                                    is Action.Reduce -> throw RuntimeException("Reduce-reduce conflict!")
                                    else -> TODO()
                                }
                            }
                            actionTable[stateIndex][lookaheadToken] = Action.Reduce(ruleIndex)
                        }
                    }
                }
            }
        }
        return ParseTable(actionTable, goToTable)
    }
}
