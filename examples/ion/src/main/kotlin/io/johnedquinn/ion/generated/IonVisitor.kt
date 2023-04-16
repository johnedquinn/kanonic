package io.johnedquinn.ion.generated

import io.johnedquinn.ion.generated.IonNode.AnnotationNode.AnnotationNode
import io.johnedquinn.ion.generated.IonNode.ExprNode
import io.johnedquinn.ion.generated.IonNode.ExprNode.AnnotatedExprNode
import io.johnedquinn.ion.generated.IonNode.ExprNode.NumberNode
import io.johnedquinn.ion.generated.IonNode.ExprNode.SexpNode
import io.johnedquinn.ion.generated.IonNode.ExprNode.StringNode
import io.johnedquinn.ion.generated.IonNode.ExprNode.SymbolNode
import io.johnedquinn.kanonic.runtime.ast.NodeVisitor

public interface IonVisitor<R, C> : NodeVisitor<R, C> {
  public fun visitAnnotatedExpr(node: AnnotatedExprNode, ctx: C): R

  public fun visitNumber(node: NumberNode, ctx: C): R

  public fun visitSexp(node: SexpNode, ctx: C): R

  public fun visitSymbol(node: SymbolNode, ctx: C): R

  public fun visitString(node: StringNode, ctx: C): R

  public fun visitExpr(node: ExprNode, ctx: C): R

  public fun visitAnnotation(node: AnnotationNode, ctx: C): R

  public fun visitAnnotation(node: io.johnedquinn.ion.generated.IonNode.AnnotationNode, ctx: C): R
}
