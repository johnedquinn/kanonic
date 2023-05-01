package io.johnedquinn.partiql.generated

import io.johnedquinn.kanonic.runtime.ast.NodeVisitor
import io.johnedquinn.partiql.generated.PartiQLNode.DqlNode
import io.johnedquinn.partiql.generated.PartiQLNode.DqlNode.DqlExprNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode.ExprArrayNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode.ExprIdentNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode.ExprWrappedNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprMultipleNode.ExprMultipleNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprNode.ExprSfwNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode.ExprSelectFallbackNode
import io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode.ExprSelectNode
import io.johnedquinn.partiql.generated.PartiQLNode.OptionalAsNode.OptionalAsNode
import io.johnedquinn.partiql.generated.PartiQLNode.StatementNode
import io.johnedquinn.partiql.generated.PartiQLNode.StatementNode.StatementDqlNode

public interface PartiQLVisitor<R, C> : NodeVisitor<R, C> {
  public fun visitStatementDql(node: StatementDqlNode, ctx: C): R

  public fun visitStatement(node: StatementNode, ctx: C): R

  public fun visitDqlExpr(node: DqlExprNode, ctx: C): R

  public fun visitDql(node: DqlNode, ctx: C): R

  public fun visitExprSfw(node: ExprSfwNode, ctx: C): R

  public fun visitExpr(node: ExprNode, ctx: C): R

  public fun visitExprSelect(node: ExprSelectNode, ctx: C): R

  public fun visitExprSelectFallback(node: ExprSelectFallbackNode, ctx: C): R

  public fun visitExprSelect(node: io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode,
      ctx: C): R

  public fun visitOptionalAs(node: OptionalAsNode, ctx: C): R

  public fun visitOptionalAs(node: io.johnedquinn.partiql.generated.PartiQLNode.OptionalAsNode,
      ctx: C): R

  public fun visitExprIdent(node: ExprIdentNode, ctx: C): R

  public fun visitExprWrapped(node: ExprWrappedNode, ctx: C): R

  public fun visitExprArray(node: ExprArrayNode, ctx: C): R

  public fun visitExprAtom(node: ExprAtomNode, ctx: C): R

  public fun visitExprMultiple(node: ExprMultipleNode, ctx: C): R

  public fun visitExprMultiple(node: io.johnedquinn.partiql.generated.PartiQLNode.ExprMultipleNode,
      ctx: C): R
}
