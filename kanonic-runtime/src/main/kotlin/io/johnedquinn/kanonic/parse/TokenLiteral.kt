package io.johnedquinn.kanonic.parse

data class TokenLiteral(
    val type: Int,
    val index: Long,
    val content: String
) {
    public object ReservedTypes {
        const val EOF = 0
        const val EPSILON = 1
    }
}

