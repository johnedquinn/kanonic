package io.johnedquinn.kanonic.tests.ion

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.NodeVisitor
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public sealed class IonNode(
  state: Int,
  children: List<Node>,
  parent: Node?,
  alias: String?,
) : Node(state, children, parent, alias) {
  public sealed class ExprNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : IonNode(state, children, parent, alias) {
    public data class AnnotatedExprNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode>()

      public fun `annotation`(): List<AnnotationNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.tests.ion.IonNode.AnnotationNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is IonVisitor -> visitor.visitAnnotatedExpr(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class NumberNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun NUMBER(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 7
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is IonVisitor -> visitor.visitNumber(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class SexpNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun PAREN_LEFT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 3
      }

      public fun PAREN_RIGHT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 4
      }

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.tests.ion.IonNode.ExprNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is IonVisitor -> visitor.visitSexp(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class SymbolNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun SYMBOL(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 6
      }

      public fun SYMBOL_QUOTED(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 5
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is IonVisitor -> visitor.visitSymbol(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class StringNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun LITERAL_STRING(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 8
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is IonVisitor -> visitor.visitString(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class AnnotationNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : IonNode(state, children, parent, alias) {
    public data class AnnotationNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : IonNode.AnnotationNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun COLON(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 2
      }

      public fun SYMBOL(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 6
      }

      public fun SYMBOL_QUOTED(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 5
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is IonVisitor -> visitor.visitAnnotation(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }
}
