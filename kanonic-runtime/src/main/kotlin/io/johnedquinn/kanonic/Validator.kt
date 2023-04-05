package io.johnedquinn.kanonic

import io.johnedquinn.kanonic.machine.AutomatonGenerator
import io.johnedquinn.kanonic.machine.TableGenerator

public object Validator {
    public fun validate(grammar: Grammar) {
        val automaton = AutomatonGenerator().generate(grammar)
        val table = TableGenerator(grammar, automaton).generate()
    }
}
