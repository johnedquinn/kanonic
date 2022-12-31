package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.TokenType

class TableGenerator(val grammar: Grammar, val automaton: Automaton) {

    // Constants
    private val numberOfStates = automaton.states.size
    private val numberOfTerminals = TokenType.values().size
    private val nonTerminals = grammar.rules.map { it.name }.toSet().toList()
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
                        actionTable[stateIndex][edgeSymbol.type.ordinal]?.let { foundAction ->
                            when (foundAction) {
                                is ShiftAction -> throw RuntimeException("Shift-shift conflict!")
                                is ReduceAction -> throw RuntimeException("Shift-reduce conflict!")
                                else -> TODO()
                            }
                        }
                        actionTable[stateIndex][edgeSymbol.type.ordinal] = ShiftAction(edgeTarget.targetState)
                    }
                    // Add GoTo
                    is RuleReference -> {
                        val nonTerminalIndex = namesToNonTerminalIndex[edgeSymbol.name]!!
                        goToTable[stateIndex][nonTerminalIndex]?.let { foundAction ->
                            when (foundAction) {
                                is ShiftAction -> throw RuntimeException("Shift-shift conflict!")
                                is ReduceAction -> throw RuntimeException("Shift-reduce conflict!")
                                else -> TODO()
                            }
                        }
                        goToTable[stateIndex][nonTerminalIndex] = ShiftAction(edgeTarget.targetState)
                    }
                }
            }

            // Add Reduce and Accept
            state.rules.forEach { rule ->
                if (rule.position <= rule.plainRule.items.lastIndex) return@forEach
                val ruleIndex = grammar.rules.indexOfFirst { it == rule.plainRule }
                rule.lookahead.forEach { lookaheadToken ->
                    when (rule.plainRule.name == grammar.options.start.name) {
                        true -> {
                            actionTable[stateIndex][lookaheadToken.ordinal] = AcceptAction(lookaheadToken)
                        }
                        false -> {
                            actionTable[stateIndex][lookaheadToken.ordinal]?.let { foundAction ->
                                when (foundAction) {
                                    is ShiftAction -> throw RuntimeException("Shift-reduce conflict!")
                                    is ReduceAction -> throw RuntimeException("Reduce-reduce conflict!")
                                    else -> TODO()
                                }
                            }
                            actionTable[stateIndex][lookaheadToken.ordinal] = ReduceAction(ruleIndex)
                        }
                    }

                }
            }
        }
        return ParseTable(actionTable, goToTable, nonTerminals)
    }
}