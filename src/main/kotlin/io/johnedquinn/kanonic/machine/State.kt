package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.SymbolReference

data class State(val rules: List<StateRule>, var children: Map<SymbolReference, State> = emptyMap())