package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.SymbolReference

data class State(val index: Int, val rules: List<StateRule>, var children: Map<SymbolReference, State> = emptyMap())

// TODO: Determine if we need a state body and state core
// data class StateBody(val core: List<StateRule>, val lookaheads: List<TokenType>)
