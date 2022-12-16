package io.johnedquinn.kanonic.machine

data class ParseTable(
    val actionTable: List<List<Action?>>,
    val goToTable: List<List<Action?>>
)
