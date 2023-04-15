package io.johnedquinn.kanonic.tests.ion

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.tests.ion.IonNode.AnnotationNode.AnnotationNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.AnnotatedExprNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.NumberNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.SexpNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.StringNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.SymbolNode

public abstract class IonBaseVisitor<R, C> : IonVisitor<R, C> {
  public abstract fun defaultReturn(node: IonNode, ctx: C): R

  public open fun defaultVisit(node: IonNode, ctx: C): R {
    for (child in node.children) {
      child.accept(this, ctx)
    }
    return defaultReturn(node, ctx)
  }

  public open override fun visit(node: Node, ctx: C): R = when (node) {
    is IonNode -> defaultVisit(node, ctx)
    else -> node.accept(this, ctx)
  }

  public open override fun visitAnnotatedExpr(node: AnnotatedExprNode, ctx: C): R =
      defaultVisit(node, ctx)

  public open override fun visitNumber(node: NumberNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitSexp(node: SexpNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitSymbol(node: SymbolNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitString(node: StringNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitExpr(node: ExprNode, ctx: C): R = when (node) {
    is AnnotatedExprNode -> visitAnnotatedExpr(node, ctx)
    is NumberNode -> visitNumber(node, ctx)
    is SexpNode -> visitSexp(node, ctx)
    is SymbolNode -> visitSymbol(node, ctx)
    is StringNode -> visitString(node, ctx)
  }

  public open override fun visitAnnotation(node: AnnotationNode, ctx: C): R = defaultVisit(node,
      ctx)

  public open override
      fun visitAnnotation(node: io.johnedquinn.kanonic.tests.ion.IonNode.AnnotationNode, ctx: C): R
      = when (node) {
    is AnnotationNode -> visitAnnotation(node, ctx)
  }
}
