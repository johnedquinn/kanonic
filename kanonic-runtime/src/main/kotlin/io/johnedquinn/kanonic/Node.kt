package io.johnedquinn.kanonic

abstract class Node {
    sealed class Expression : Node() {
        data class Identifier(val text: String) : Expression()
        data class Integer(val number: Int): Expression()
        data class Symbol(val text: String): Expression()
        data class Plus(val lhs: Node, val rhs: Node) : Expression()
        data class Minus(val lhs: Node, val rhs: Node) : Expression()
    }
}