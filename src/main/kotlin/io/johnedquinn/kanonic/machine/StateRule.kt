package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.TokenType

data class StateRule(val plainRule: Rule, val position: Int, val lookahead: Set<TokenType>)
