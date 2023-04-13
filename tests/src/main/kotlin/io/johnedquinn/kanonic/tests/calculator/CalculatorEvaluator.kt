package io.johnedquinn.kanonic.tests.calculator

import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import kotlin.math.pow

object CalculatorEvaluator : CalculatorBaseVisitor<Int, Unit>() {

    private val parser = KanonicParser.Builder.standard().withSpecification(CalculatorSpecification).build()

    public fun evaluate(input: String): Int {
        val ast = parser.parse(input)
        return visit(ast, Unit)
    }

    override fun visitExprPlus(node: CalculatorNode.ExprNode.ExprPlusNode, ctx: Unit): Int {
        // TODO: Add Tokens to Specification
        val op = when (node.op().type) {
            3 -> { a: Int, b: Int -> a + b }
            4 -> { a: Int, b: Int -> a - b }
            else -> error("Didn't understand op type: ${node.op().type}")
        }
        val lhs = visitExpr(node.lhs(), ctx)
        val rhs = visitMathOp(node.rhs(), ctx)
        return op(lhs, rhs)
    }

    override fun visitExprDiv(node: CalculatorNode.MathOpNode.ExprDivNode, ctx: Unit): Int {
        // TODO: Add Tokens to Specification
        val op = when (node.op().type) {
            5 -> { a: Int, b: Int -> a * b }
            6 -> { a: Int, b: Int -> a / b }
            else -> error("Didn't understand op type: ${node.op().type}")
        }
        val lhs = visitMathOp(node.lhs(), ctx)
        val rhs = visitMathOpSecond(node.rhs(), ctx)
        return op(lhs, rhs)
    }

    override fun visitMathOpSecond(node: CalculatorNode.MathOpSecondNode.MathOpSecondNode, ctx: Unit): Int {
        val lhs = visitMathOpSecond(node.lhs(), ctx)
        val rhs = visitAtomic(node.rhs(), ctx)
        return lhs.toDouble().pow(rhs.toDouble()).toInt()
    }

    override fun visitParen(node: CalculatorNode.AtomicNode.ParenNode, ctx: Unit): Int {
        return visitExpr(node.exp(), ctx)
    }

    override fun visitTerminal(node: TerminalNode, ctx: Unit): Int {
        return when (val int = node.token.content.toIntOrNull()) {
            null -> 0
            else -> int
        }
    }

    public override fun defaultVisit(node: CalculatorNode, ctx: Unit): Int {
        var result: Int = 0
        for (child in node.children) {
            result = child.accept(this, ctx)
        }
        return result
    }

    override fun defaultReturn(node: CalculatorNode, ctx: Unit): Int = 0
}
