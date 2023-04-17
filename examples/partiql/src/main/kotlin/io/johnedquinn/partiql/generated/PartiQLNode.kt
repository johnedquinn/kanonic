package io.johnedquinn.partiql.generated

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.NodeVisitor
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public sealed class PartiQLNode(
  state: Int,
  children: List<Node>,
  parent: Node?,
  alias: String?,
) : Node(state, children, parent, alias) {
  public sealed class StatementNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : PartiQLNode(state, children, parent, alias) {
    public data class StatementDqlNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : StatementNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun dql(): List<DqlNode> =
          this.children.filterIsInstance<io.johnedquinn.partiql.generated.PartiQLNode.DqlNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitStatementDql(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class DqlNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : PartiQLNode(state, children, parent, alias) {
    public data class DqlExprNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : DqlNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.partiql.generated.PartiQLNode.ExprNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitDqlExpr(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class ExprNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : PartiQLNode(state, children, parent, alias) {
    public data class ExprSfwNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun exprSelect(): List<ExprSelectNode> =
          this.children.filterIsInstance<io.johnedquinn.partiql.generated.PartiQLNode.ExprSelectNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitExprSfw(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class ExprSelectNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : PartiQLNode(state, children, parent, alias) {
    public data class ExprSelectNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : PartiQLNode.ExprSelectNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun SELECT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 2
      }

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.partiql.generated.PartiQLNode.ExprNode>()

      public fun FROM(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 3
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitExprSelect(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class ExprSelectFallbackNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : PartiQLNode.ExprSelectNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun exprAtom(): List<ExprAtomNode> =
          this.children.filterIsInstance<io.johnedquinn.partiql.generated.PartiQLNode.ExprAtomNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitExprSelectFallback(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class ExprAtomNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : PartiQLNode(state, children, parent, alias) {
    public data class ExprIdentNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprAtomNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun SYMBOL(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 8
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitExprIdent(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class ExprWrappedNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprAtomNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun PAREN_LEFT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 5
      }

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.partiql.generated.PartiQLNode.ExprNode>()

      public fun PAREN_RIGHT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 6
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is PartiQLVisitor -> visitor.visitExprWrapped(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }
}
