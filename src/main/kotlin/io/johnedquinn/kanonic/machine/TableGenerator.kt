package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.TokenType

internal class TableGenerator(val grammar: Grammar, val automaton: Automaton) {

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

    internal fun generate() {
        automaton.states.forEachIndexed { stateIndex, state ->
            val edges = automaton.edges[state.index] ?: return@forEachIndexed
            edges.forEach { edgeTarget ->
                when (val edgeSymbol = edgeTarget.edge) {
                    // Add Shifts
                    is TerminalReference -> {
                        actionTable[stateIndex][edgeSymbol.type.ordinal]?.let { foundAction ->
                            when (foundAction) {
                                is ShiftAction -> throw RuntimeException("Shift-shift conflict!")
                                is ReduceAction -> throw RuntimeException("Shift-reduce conflict!")
                            }
                        }
                        actionTable[stateIndex][edgeSymbol.type.ordinal] = ShiftAction(edgeTarget.targetState)
                    }
                    // Add GoTo
                    is RuleReference -> {
                        val nonTerminalIndex = namesToNonTerminalIndex[edgeSymbol.name]!!
                        actionTable[stateIndex][nonTerminalIndex]?.let { foundAction ->
                            when (foundAction) {
                                is ShiftAction -> throw RuntimeException("Shift-shift conflict!")
                                is ReduceAction -> throw RuntimeException("Shift-reduce conflict!")
                            }
                        }
                        actionTable[stateIndex][nonTerminalIndex] = ShiftAction(edgeTarget.targetState)
                    }
                }
            }
        }
        val parseTable = ParseTable(actionTable, goToTable)
        println(parseTable)
    }
}