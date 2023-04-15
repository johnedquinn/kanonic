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
import io.johnedquinn.kanonic.runtime.ast.NodeVisitor

public interface CalculatorVisitor<R, C> : NodeVisitor<R, C> {
  public fun visitRoot(node: RootNode, ctx: C): R

  public fun visitRoot(node: io.johnedquinn.calculator.generated.CalculatorNode.RootNode, ctx: C): R

  public fun visitExprPlus(node: ExprPlusNode, ctx: C): R

  public fun visitExprAtomic(node: ExprAtomicNode, ctx: C): R

  public fun visitExpr(node: ExprNode, ctx: C): R

  public fun visitExprDiv(node: ExprDivNode, ctx: C): R

  public fun visitMathOpFallback(node: MathOpFallbackNode, ctx: C): R

  public fun visitMathOp(node: MathOpNode, ctx: C): R

  public fun visitMathOpSecond(node: MathOpSecondNode, ctx: C): R

  public fun visitExprAtom(node: ExprAtomNode, ctx: C): R

  public
      fun visitMathOpSecond(node: io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode,
      ctx: C): R

  public fun visitInt(node: IntNode, ctx: C): R

  public fun visitParen(node: ParenNode, ctx: C): R

  public fun visitAtomic(node: AtomicNode, ctx: C): R
}
