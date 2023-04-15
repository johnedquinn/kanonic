package io.johnedquinn.kanonic.tests.ion

import io.johnedquinn.kanonic.runtime.ast.NodeVisitor
import io.johnedquinn.kanonic.tests.ion.IonNode.AnnotationNode.AnnotationNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.AnnotatedExprNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.NumberNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.SexpNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.StringNode
import io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode.SymbolNode

public interface IonVisitor<R, C> : NodeVisitor<R, C> {
  public fun visitAnnotatedExpr(node: AnnotatedExprNode, ctx: C): R

  public fun visitNumber(node: NumberNode, ctx: C): R

  public fun visitSexp(node: SexpNode, ctx: C): R

  public fun visitSymbol(node: SymbolNode, ctx: C): R

  public fun visitString(node: StringNode, ctx: C): R

  public fun visitExpr(node: ExprNode, ctx: C): R

  public fun visitAnnotation(node: AnnotationNode, ctx: C): R

  public fun visitAnnotation(node: io.johnedquinn.kanonic.tests.ion.IonNode.AnnotationNode, ctx: C):
      R
}
