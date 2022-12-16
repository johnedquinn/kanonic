package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.TokenType

data class Automaton(val states: List<State>, val edges: Map<Int, List<EdgeTarget>>) {

    data class EdgeTarget(
        val edge: SymbolReference,
        val targetState: Int
    )

    internal fun printInfo() {
        println("GENERAL INFORMATION")
        println("\t- Number of States: ${states.size}")
        println("STATES")
        states.forEach { state ->
            println("\tState ${state.index}:")
            state.rules.forEachIndexed { ruleIndex, stateRule ->
                print("\t\tRule $ruleIndex: ")
                printStateRule(stateRule)
                println()
            }
        }

        println("EDGES")
        edges.forEach { (src, children) ->
            println("\tState $src -> $children")
        }
    }

    private fun printStateRule(stateRule: StateRule) {
        print("${stateRule.plainRule.name} ---> ")
        stateRule.plainRule.items.forEachIndexed { index, item ->
            if (stateRule.position == index) {
                print("⚬")
            }
            when (item) {
                is RuleReference -> print("\"${item.name}\"")
                is TerminalReference -> print(item.type)
            }
            print(" ")
        }

        print("--- Lookahead: ${stateRule.lookahead}")
    }
}
