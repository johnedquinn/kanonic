package io.johnedquinn.kanonic.gen.impl

import io.johnedquinn.kanonic.runtime.grammar.RuleVariant

data class StateRule(val plainRule: RuleVariant, val position: Int, val lookahead: MutableSet<Int>)
