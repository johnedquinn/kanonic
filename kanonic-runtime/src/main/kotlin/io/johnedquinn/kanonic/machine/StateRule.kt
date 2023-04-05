package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.RuleVariant

data class StateRule(val plainRule: RuleVariant, val position: Int, val lookahead: MutableSet<Int>)
