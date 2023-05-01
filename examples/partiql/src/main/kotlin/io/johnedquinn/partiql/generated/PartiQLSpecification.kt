package io.johnedquinn.partiql.generated

import io.johnedquinn.kanonic.runtime.ast.GeneratedNode
import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.grammar.Grammar
import io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder.Companion.buildGrammar
import io.johnedquinn.kanonic.runtime.grammar.RuleBuilder.Companion.buildGeneratedRule
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
      "AS" - "AS"
      "SELECT" - "SELECT"
      "FROM" - "FROM"
      "PAREN_LEFT" - "\\("
      "PAREN_RIGHT" - "\\)"
      "BRACKET_LEFT" - "\\["
      "BRACKET_RIGHT" - "\\]"
      "COMMA" - ","
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
    "_generated_0" eq buildGeneratedRule(this, "_generated_0") {
      "_generated_1" eq "EPSILON"
      "_generated_2" eq "optional_as"
    }
    "expr_select" eq buildRule(this, "expr_select") {
      "expr_select" eq "SELECT" - "expr" - "FROM" - "expr" - "_generated_0"
      "expr_select_fallback" eq "expr_atom"
    }
    "optional_as" eq buildRule(this, "optional_as") {
      "optional_as" eq "AS" - "SYMBOL"
    }
    "_generated_3" eq buildGeneratedRule(this, "_generated_3") {
      "_generated_4" eq "EPSILON"
      "_generated_5" eq "_generated_3" - "expr_multiple"
    }
    "expr_atom" eq buildRule(this, "expr_atom") {
      "expr_ident" eq "SYMBOL"
      "expr_wrapped" eq "PAREN_LEFT" - "expr" - "PAREN_RIGHT"
      "expr_array" eq "BRACKET_LEFT" - "expr" - "_generated_3" - "BRACKET_RIGHT"
    }
    "expr_multiple" eq buildRule(this, "expr_multiple") {
      "expr_multiple" eq "COMMA" - "expr"
    }
  }


  private val nodeLambdaList: List<NodeCreator> = initializeLambdas()

  public override fun getTable(): String = """,,,S4,,S152,,S155,,,S151,,,S1,S2,,S3,,,S150,
  A0,,,,,,,,,,,,,,,,,,,,
  R1,,,,,,,,,,,,,,,,,,,,
  R2,,,,,,,,,,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S5,,S104,,,S133,
  ,,,,S6,,,,,,,,,,,,,,,,
  ,,,S14,,S25,,S146,,,S24,,,,S7,,S13,,,S23,
  ,S9,S11,,,,,,,,,,,,,S8,,S10,,,
  R5,,,,,,,,,,,,,,,,,,,,
  R3,,,,,,,,,,,,,,,,,,,,
  R4,,,,,,,,,,,,,,,,,,,,
  ,,,,,,,,,,S12,,,,,,,,,,
  R7,,,,,,,,,,,,,,,,,,,,
  R2,R2,R2,,,,,,,,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S15,,S104,,,S133,
  ,,,,S16,,,,,,,,,,,,,,,,
  ,,,S14,,S25,,S146,,,S24,,,,S17,,S13,,,S23,
  ,S19,S21,,,,,,,,,,,,,S18,,S20,,,
  R5,R5,R5,,,,,,,,,,,,,,,,,,
  R3,R3,R3,,,,,,,,,,,,,,,,,,
  R4,R4,R4,,,,,,,,,,,,,,,,,,
  ,,,,,,,,,,S22,,,,,,,,,,
  R7,R7,R7,,,,,,,,,,,,,,,,,,
  R6,R6,R6,,,,,,,,,,,,,,,,,,
  R10,R10,R10,,,,,,,,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S26,,S28,,,S53,
  ,,,,,,S27,,,,,,,,,,,,,,
  R11,R11,R11,,,,,,,,,,,,,,,,,,
  ,,,,,,R2,,,,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S30,,S104,,,S133,
  ,,,,S31,,,,,,,,,,,,,,,,
  ,,,S39,,S50,,S142,,,S49,,,,S32,,S38,,,S48,
  ,S34,S36,,,,,,,,,,,,,S33,,S35,,,
  ,,,,,,R5,,,,,,,,,,,,,,
  ,,,,,,R3,,,,,,,,,,,,,,
  ,,,,,,R4,,,,,,,,,,,,,,
  ,,,,,,,,,,S37,,,,,,,,,,
  ,,,,,,R7,,,,,,,,,,,,,,
  ,R2,R2,,,,R2,,,,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S40,,S104,,,S133,
  ,,,,S41,,,,,,,,,,,,,,,,
  ,,,S39,,S50,,S142,,,S49,,,,S42,,S38,,,S48,
  ,S44,S46,,,,,,,,,,,,,S43,,S45,,,
  ,R5,R5,,,,R5,,,,,,,,,,,,,,
  ,R3,R3,,,,R3,,,,,,,,,,,,,,
  ,R4,R4,,,,R4,,,,,,,,,,,,,,
  ,,,,,,,,,,S47,,,,,,,,,,
  ,R7,R7,,,,R7,,,,,,,,,,,,,,
  ,R6,R6,,,,R6,,,,,,,,,,,,,,
  ,R10,R10,,,,R10,,,,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S51,,S28,,,S53,
  ,,,,,,S52,,,,,,,,,,,,,,
  ,R11,R11,,,,R11,,,,,,,,,,,,,,
  ,,,,,,R6,,,,,,,,,,,,,,
  ,,,,,,R10,,,,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S56,,S28,,,S53,
  ,,,,,,S57,,,,,,,,,,,,,,
  ,,,,,,R11,,,,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S59,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S60,,
  ,,,,,,,,S61,S63,,,,,,,,,,,S62
  ,,,,,,R12,,,,,,,,,,,,,,
  ,,,,,,,,R9,R9,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S64,,S65,,,S95,
  ,,,,,,,,R13,R13,,,,,,,,,,,
  ,,,,,,,,R2,R2,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S67,,S104,,,S133,
  ,,,,S68,,,,,,,,,,,,,,,,
  ,,,S76,,S87,,S90,,,S86,,,,S69,,S75,,,S85,
  ,S71,S73,,,,,,,,,,,,,S70,,S72,,,
  ,,,,,,,,R5,R5,,,,,,,,,,,
  ,,,,,,,,R3,R3,,,,,,,,,,,
  ,,,,,,,,R4,R4,,,,,,,,,,,
  ,,,,,,,,,,S74,,,,,,,,,,
  ,,,,,,,,R7,R7,,,,,,,,,,,
  ,R2,R2,,,,,,R2,R2,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S77,,S104,,,S133,
  ,,,,S78,,,,,,,,,,,,,,,,
  ,,,S76,,S87,,S90,,,S86,,,,S79,,S75,,,S85,
  ,S81,S83,,,,,,,,,,,,,S80,,S82,,,
  ,R5,R5,,,,,,R5,R5,,,,,,,,,,,
  ,R3,R3,,,,,,R3,R3,,,,,,,,,,,
  ,R4,R4,,,,,,R4,R4,,,,,,,,,,,
  ,,,,,,,,,,S84,,,,,,,,,,
  ,R7,R7,,,,,,R7,R7,,,,,,,,,,,
  ,R6,R6,,,,,,R6,R6,,,,,,,,,,,
  ,R10,R10,,,,,,R10,R10,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S88,,S28,,,S53,
  ,,,,,,S89,,,,,,,,,,,,,,
  ,R11,R11,,,,,,R11,R11,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S91,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S92,,
  ,,,,,,,,S93,S63,,,,,,,,,,,S62
  ,R12,R12,,,,,,R12,R12,,,,,,,,,,,
  ,,,,,,,,R8,R8,,,,,,,,,,,
  ,,,,,,,,R6,R6,,,,,,,,,,,
  ,,,,,,,,R10,R10,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S98,,S28,,,S53,
  ,,,,,,S99,,,,,,,,,,,,,,
  ,,,,,,,,R11,R11,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S101,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S102,,
  ,,,,,,,,S103,S63,,,,,,,,,,,S62
  ,,,,,,,,R12,R12,,,,,,,,,,,
  ,,,,R2,,,,,,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S106,,S104,,,S133,
  ,,,,S107,,,,,,,,,,,,,,,,
  ,,,S115,,S126,,S129,,,S125,,,,S108,,S114,,,S124,
  ,S110,S112,,,,,,,,,,,,,S109,,S111,,,
  ,,,,R5,,,,,,,,,,,,,,,,
  ,,,,R3,,,,,,,,,,,,,,,,
  ,,,,R4,,,,,,,,,,,,,,,,
  ,,,,,,,,,,S113,,,,,,,,,,
  ,,,,R7,,,,,,,,,,,,,,,,
  ,R2,R2,,R2,,,,,,,,,,,,,,,,
  ,,,S105,,S135,,S138,,,S134,,,,S116,,S104,,,S133,
  ,,,,S117,,,,,,,,,,,,,,,,
  ,,,S115,,S126,,S129,,,S125,,,,S118,,S114,,,S124,
  ,S120,S122,,,,,,,,,,,,,S119,,S121,,,
  ,R5,R5,,R5,,,,,,,,,,,,,,,,
  ,R3,R3,,R3,,,,,,,,,,,,,,,,
  ,R4,R4,,R4,,,,,,,,,,,,,,,,
  ,,,,,,,,,,S123,,,,,,,,,,
  ,R7,R7,,R7,,,,,,,,,,,,,,,,
  ,R6,R6,,R6,,,,,,,,,,,,,,,,
  ,R10,R10,,R10,,,,,,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S127,,S28,,,S53,
  ,,,,,,S128,,,,,,,,,,,,,,
  ,R11,R11,,R11,,,,,,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S130,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S131,,
  ,,,,,,,,S132,S63,,,,,,,,,,,S62
  ,R12,R12,,R12,,,,,,,,,,,,,,,,
  ,,,,R6,,,,,,,,,,,,,,,,
  ,,,,R10,,,,,,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S136,,S28,,,S53,
  ,,,,,,S137,,,,,,,,,,,,,,
  ,,,,R11,,,,,,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S139,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S140,,
  ,,,,,,,,S141,S63,,,,,,,,,,,S62
  ,,,,R12,,,,,,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S143,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S144,,
  ,,,,,,,,S145,S63,,,,,,,,,,,S62
  ,R12,R12,,,,R12,,,,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S147,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S148,,
  ,,,,,,,,S149,S63,,,,,,,,,,,S62
  R12,R12,R12,,,,,,,,,,,,,,,,,,
  R6,,,,,,,,,,,,,,,,,,,,
  R10,,,,,,,,,,,,,,,,,,,,
  ,,,S29,,S55,,S58,,,S54,,,,S153,,S28,,,S53,
  ,,,,,,S154,,,,,,,,,,,,,,
  R11,,,,,,,,,,,,,,,,,,,,
  ,,,S66,,S97,,S100,,,S96,,,,S156,,S65,,,S95,
  ,S94,,,,,,,,,,,,,,,,,S157,,
  ,,,,,,,,S158,S63,,,,,,,,,,,S62
  R12,,,,,,,,,,,,,,,,,,,,"""

  private fun initializeLambdas(): List<NodeCreator> = buildList {
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.StatementNode.StatementDqlNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.DqlNode.DqlExprNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprNode.ExprSfwNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprSelectNode.ExprSelectNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprSelectNode.ExprSelectFallbackNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.OptionalAsNode.OptionalAsNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprAtomNode.ExprIdentNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprAtomNode.ExprWrappedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprAtomNode.ExprArrayNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> PartiQLNode.ExprMultipleNode.ExprMultipleNode(state, children, parent, "null") })
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
