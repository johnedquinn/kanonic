package io.johnedquinn.partiql.generated

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.grammar.Grammar
import io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder.Companion.buildGrammar
import io.johnedquinn.kanonic.runtime.grammar.RuleBuilder.Companion.buildRule
import io.johnedquinn.kanonic.runtime.parse.NodeCreator
import io.johnedquinn.kanonic.runtime.parse.ParserSpecification
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public object PartiQLSpecification : ParserSpecification {
  public override val grammar: Grammar = buildGrammar("PartiQLNode", "statement") {
    packageName("io.johnedquinn.partiql.generated")
    tokens {
      "SELECT" - "SELECT"
      "FROM" - "FROM"
      "PAREN_LEFT" - "\\("
      "PAREN_RIGHT" - "\\)"
      "SYMBOL" - "[a-zA-Z]+"
      "WS" - ("[\\s]" channel "hidden")
    }
    "statement" eq buildRule(this, "statement") {
      "statement_dql" eq "dql"
    }
    "dql" eq buildRule(this, "dql") {
      "dql_expr" eq "expr"
    }
    "expr" eq buildRule(this, "expr") {
      "expr_sfw" eq "expr_select"
    }
    "expr_select" eq buildRule(this, "expr_select") {
      "expr_select" eq "SELECT" - "expr" - "FROM" - "expr"
      "expr_select_fallback" eq "expr_atom"
    }
    "expr_atom" eq buildRule(this, "expr_atom") {
      "expr_ident" eq "SYMBOL"
      "expr_wrapped" eq "PAREN_LEFT" - "expr" - "PAREN_RIGHT"
    }
  }


  private val nodeLambdaList: List<NodeCreator> = initializeLambdas()

  public override fun getTable(): String = """,,S4,,S10,,S9,,,S1,S2,S3,S8
  A0,,,,,,,,,,,,
  R1,,,,,,,,,,,,
  R2,,,,,,,,,,,,
  ,,S24,,S30,,S29,,,,S5,S23,S28
  ,,,S6,,,,,,,,,
  ,,S4,,S10,,S9,,,,S7,S3,S8
  R3,,,,,,,,,,,,
  R4,,,,,,,,,,,,
  R5,,,,,,,,,,,,
  ,,S14,,S20,,S19,,,,S11,S13,S18
  ,,,,,S12,,,,,,,
  R6,,,,,,,,,,,,
  ,,,,,R2,,,,,,,
  ,,S24,,S30,,S29,,,,S15,S23,S28
  ,,,S16,,,,,,,,,
  ,,S14,,S20,,S19,,,,S17,S13,S18
  ,,,,,R3,,,,,,,
  ,,,,,R4,,,,,,,
  ,,,,,R5,,,,,,,
  ,,S14,,S20,,S19,,,,S21,S13,S18
  ,,,,,S22,,,,,,,
  ,,,,,R6,,,,,,,
  ,,,R2,,,,,,,,,
  ,,S24,,S30,,S29,,,,S25,S23,S28
  ,,,S26,,,,,,,,,
  ,,S24,,S30,,S29,,,,S27,S23,S28
  ,,,R3,,,,,,,,,
  ,,,R4,,,,,,,,,
  ,,,R5,,,,,,,,,
  ,,S14,,S20,,S19,,,,S31,S13,S18
  ,,,,,S32,,,,,,,
  ,,,R6,,,,,,,,,"""

  private fun initializeLambdas(): List<NodeCreator> = buildList {
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.StatementNode.StatementDqlNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.DqlNode.DqlExprNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprNode.ExprSfwNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprSelectNode.ExprSelectNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprSelectNode.ExprSelectFallbackNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprAtomNode.ExprIdentNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprAtomNode.ExprWrappedNode(state, children, parent, "null") })
  }

  public override fun createRuleNode(
    index: Int,
    state: Int,
    children: List<Node>,
    parent: Node?,
    alias: String?,
  ): Node {
    val nodeCreator = nodeLambdaList[index]
    return nodeCreator.create(state, children, parent, alias)
  }
}
