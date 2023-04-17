package io.johnedquinn.partiql.generated

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.partiql.generated.PartiQLNode.DqlNode
import io.johnedquinn.partiql.generated.PartiQLNode.DqlNode.DqlExprNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode.ExprIdentNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode.ExprWrappedNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprNode.ExprSfwNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode.ExprSelectFallbackNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode.ExprSelectNode
import io.johnedquinn.partiql.generated.PartiQLNode.StatementNode
import io.johnedquinn.partiql.generated.PartiQLNode.StatementNode.StatementDqlNode

public abstract class PartiQLBaseVisitor<R, C> : PartiQLVisitor<R, C> {
  public abstract fun defaultReturn(node: PartiQLNode, ctx: C): R

  public open fun defaultVisit(node: PartiQLNode, ctx: C): R {
    for (child in node.children) {
      child.accept(this, ctx)
    }
    return defaultReturn(node, ctx)
  }

  public open override fun visit(node: Node, ctx: C): R = when (node) {
    is PartiQLNode -> defaultVisit(node, ctx)
    else -> node.accept(this, ctx)
  }

  public open override fun visitStatementDql(node: StatementDqlNode, ctx: C): R = defaultVisit(node,
      ctx)

  public open override fun visitStatement(node: StatementNode, ctx: C): R = when (node) {
    is StatementDqlNode -> visitStatementDql(node, ctx)
  }

  public open override fun visitDqlExpr(node: DqlExprNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitDql(node: DqlNode, ctx: C): R = when (node) {
    is DqlExprNode -> visitDqlExpr(node, ctx)
  }

  public open override fun visitExprSfw(node: ExprSfwNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitExpr(node: ExprNode, ctx: C): R = when (node) {
    is ExprSfwNode -> visitExprSfw(node, ctx)
  }

  public open override fun visitExprSelect(node: ExprSelectNode, ctx: C): R = defaultVisit(node,
      ctx)

  public open override fun visitExprSelectFallback(node: ExprSelectFallbackNode, ctx: C): R =
      defaultVisit(node, ctx)

  public open override
      fun visitExprSelect(node: io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode,
      ctx: C): R = when (node) {
    is ExprSelectNode -> visitExprSelect(node, ctx)
    is ExprSelectFallbackNode -> visitExprSelectFallback(node, ctx)
  }

  public open override fun visitExprIdent(node: ExprIdentNode, ctx: C): R = defaultVisit(node, ctx)

  public open override fun visitExprWrapped(node: ExprWrappedNode, ctx: C): R = defaultVisit(node,
      ctx)

  public open override fun visitExprAtom(node: ExprAtomNode, ctx: C): R = when (node) {
    is ExprIdentNode -> visitExprIdent(node, ctx)
    is ExprWrappedNode -> visitExprWrapped(node, ctx)
  }
}
