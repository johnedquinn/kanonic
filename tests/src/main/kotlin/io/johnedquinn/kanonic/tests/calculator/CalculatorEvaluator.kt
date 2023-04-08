package io.johnedquinn.kanonic.tests.calculator

import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.parse.KanonicParser

object CalculatorEvaluator : CalculatorBaseVisitor<Int, Unit>() {
    private val parser = KanonicParser.Builder.standard().withSpecification(CalculatorSpecification).build()
    public fun evaluate(input: String): Int {
        val ast = parser.parse(input)
        return visit(ast, Unit)
    }

    override fun visitExprPlus(node: CalculatorNode.ExprNode.ExprPlusNode, ctx: Unit): Int {
        return visitExpr(node.expr()[0], ctx) + visitAtomic(node.atomic()[0], ctx)
    }

    override fun visitExprMinus(node: CalculatorNode.ExprNode.ExprMinusNode, ctx: Unit): Int {
        return visitExpr(node.expr()[0], ctx) - visitAtomic(node.atomic()[0], ctx)
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

    override fun defaultReturn(node: CalculatorNode, ctx: Unit): Int {
        return 0
    }
}
