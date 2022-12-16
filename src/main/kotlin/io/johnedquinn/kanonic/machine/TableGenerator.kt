package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.TokenType

internal class TableGenerator(val grammar: Grammar, val automaton: Automaton) {

    // Constants
    private val numberOfStates = automaton.states.size
    private val numberOfTerminals = TokenType.values().size
    private val nonTerminals = grammar.rules.map { it.name }.toSet().toList()
    private val numberOfNonTerminals = nonTerminals.size

    // Tables
    val actionTable: List<List<Action?>> = List(numberOfStates) { List(numberOfTerminals) { null } }
    val goToTable: List<List<Action?>> = List(numberOfStates) { List(numberOfNonTerminals) { null } }

    internal fun generate() {
        automaton.states.forEach { state ->
            TODO()
        }
    }
}