package io.johnedquinn.calculator.generated

import io.johnedquinn.calculator.generated.CalculatorNode.AtomicNode
import io.johnedquinn.calculator.generated.CalculatorNode.AtomicNode.IntNode
import io.johnedquinn.calculator.generated.CalculatorNode.AtomicNode.ParenNode
import io.johnedquinn.calculator.generated.CalculatorNode.ExprNode
import io.johnedquinn.calculator.generated.CalculatorNode.ExprNode.ExprAtomicNode
import io.johnedquinn.calculator.generated.CalculatorNode.ExprNode.ExprPlusNode
import io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode
import io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode.ExprDivNode
import io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode.MathOpFallbackNode
import io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode.ExprAtomNode
import io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode.MathOpSecondNode
import io.johnedquinn.calculator.generated.CalculatorNode.RootNode.RootNode
import io.johnedquinn.kanonic.runtime.ast.Node

public abstract class CalculatorBaseVisitor<R, C> : CalculatorVisitor<R, C> {
  public abstract fun defaultReturn(node: CalculatorNode, ctx: C): R

  public open fun defaultVisit(node: CalculatorNode, ctx: C): R {
    for (child in node.children) {
      child.accept(this, ctx)
    }
    return defaultReturn(node, ctx)
  }

  public open override fun visit(node: Node, ctx: C): R = when (node) {
    is CalculatorNode -> defaultVisit(node, ctx)
    else -> node.accept(this, ctx)
  }

  public open override fun visitRoot(node: RootNode, ctx: C): R = defaultVisit(node, ctx)

  public open override
      fun visitRoot(node: io.johnedquinn.calculator.generated.CalculatorNode.RootNode, ctx: C): R =
      when (node) {
    is RootNode -> visitRoot(node, ctx)
  }

  public open override fun visitExprPlus(node: ExprPlusNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitExprAtomic(node: ExprAtomicNode, ctx: C): R = defaultVisit(node,
      ctx)

  public open override fun visitExpr(node: ExprNode, ctx: C): R = when (node) {
    is ExprPlusNode -> visitExprPlus(node, ctx)
    is ExprAtomicNode -> visitExprAtomic(node, ctx)
  }

  public open override fun visitExprDiv(node: ExprDivNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitMathOpFallback(node: MathOpFallbackNode, ctx: C): R =
      defaultVisit(node, ctx)

  public open override fun visitMathOp(node: MathOpNode, ctx: C): R = when (node) {
    is ExprDivNode -> visitExprDiv(node, ctx)
    is MathOpFallbackNode -> visitMathOpFallback(node, ctx)
  }

  public open override fun visitMathOpSecond(node: MathOpSecondNode, ctx: C): R = defaultVisit(node,
      ctx)

  public open override fun visitExprAtom(node: ExprAtomNode, ctx: C): R = defaultVisit(node, ctx)

  public open override
      fun visitMathOpSecond(node: io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode,
      ctx: C): R = when (node) {
    is MathOpSecondNode -> visitMathOpSecond(node, ctx)
    is ExprAtomNode -> visitExprAtom(node, ctx)
  }

  public open override fun visitInt(node: IntNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitParen(node: ParenNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitAtomic(node: AtomicNode, ctx: C): R = when (node) {
    is IntNode -> visitInt(node, ctx)
    is ParenNode -> visitParen(node, ctx)
  }
}
