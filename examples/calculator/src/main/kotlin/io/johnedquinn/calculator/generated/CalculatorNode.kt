package io.johnedquinn.calculator.generated

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.NodeVisitor
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public sealed class CalculatorNode(
  state: Int,
  children: List<Node>,
  parent: Node?,
  alias: String?,
) : Node(state, children, parent, alias) {
  public sealed class RootNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : CalculatorNode(state, children, parent, alias) {
    public data class RootNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : CalculatorNode.RootNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.ExprNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitRoot(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class ExprNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : CalculatorNode(state, children, parent, alias) {
    public data class ExprPlusNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.ExprNode>()

      public fun mathOp(): List<MathOpNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode>()

      public fun PLUS(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 3
      }

      public fun MINUS(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 4
      }

      public fun lhs(): ExprNode = this.children.filter { it.alias == "lhs" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.ExprNode

      public fun op(): TerminalNode = this.children.filter { it.alias == "op" }.first() as
          io.johnedquinn.kanonic.runtime.ast.TerminalNode

      public fun rhs(): MathOpNode = this.children.filter { it.alias == "rhs" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitExprPlus(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class ExprAtomicNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : ExprNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun mathOp(): List<MathOpNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitExprAtomic(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class MathOpNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : CalculatorNode(state, children, parent, alias) {
    public data class ExprDivNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : MathOpNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun mathOp(): List<MathOpNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode>()

      public fun mathOpSecond(): List<MathOpSecondNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode>()

      public fun SLASH_FORWARD(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 6
      }

      public fun ASTERISK(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 5
      }

      public fun lhs(): MathOpNode = this.children.filter { it.alias == "lhs" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.MathOpNode

      public fun op(): TerminalNode = this.children.filter { it.alias == "op" }.first() as
          io.johnedquinn.kanonic.runtime.ast.TerminalNode

      public fun rhs(): MathOpSecondNode = this.children.filter { it.alias == "rhs" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitExprDiv(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class MathOpFallbackNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : MathOpNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun mathOpSecond(): List<MathOpSecondNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitMathOpFallback(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class MathOpSecondNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : CalculatorNode(state, children, parent, alias) {
    public data class MathOpSecondNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : CalculatorNode.MathOpSecondNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun CARROT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 7
      }

      public fun mathOpSecond():
          List<io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode>()

      public fun atomic(): List<AtomicNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.AtomicNode>()

      public fun lhs(): io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode =
          this.children.filter { it.alias == "lhs" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.MathOpSecondNode

      public fun rhs(): AtomicNode = this.children.filter { it.alias == "rhs" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.AtomicNode

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitMathOpSecond(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class ExprAtomNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : CalculatorNode.MathOpSecondNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun atomic(): List<AtomicNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.AtomicNode>()

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitExprAtom(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }

  public sealed class AtomicNode(
    public override val state: Int,
    public override val children: List<Node>,
    public override var parent: Node?,
    public override var alias: String?,
  ) : CalculatorNode(state, children, parent, alias) {
    public data class IntNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : AtomicNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun INTEGER(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 2
      }

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitInt(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }

    public data class ParenNode(
      public override val state: Int,
      public override val children: List<Node>,
      public override var parent: Node?,
      public override var alias: String?,
    ) : AtomicNode(state, children, parent, alias) {
      public override fun toString(): String = """${this::class.simpleName}(state: $state, children:
          $children, alias: $alias)"""

      public fun PAREN_LEFT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 8
      }

      public fun PAREN_RIGHT(): List<TerminalNode> =
          this.children.filterIsInstance<io.johnedquinn.kanonic.runtime.ast.TerminalNode>().filter {
        it.token.type == 9
      }

      public fun expr(): List<ExprNode> =
          this.children.filterIsInstance<io.johnedquinn.calculator.generated.CalculatorNode.ExprNode>()

      public fun exp(): ExprNode = this.children.filter { it.alias == "exp" }.first() as
          io.johnedquinn.calculator.generated.CalculatorNode.ExprNode

      public override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R = when (visitor) {
        is CalculatorVisitor -> visitor.visitParen(this, ctx)
        else -> visitor.visit(this, ctx)
      }
    }
  }
}
