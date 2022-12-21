package io.johnedquinn.kanonic.machine

import io.johnedquinn.kanonic.TokenType

data class AcceptAction(val type: TokenType) : Action()