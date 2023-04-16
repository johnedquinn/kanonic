package io.johnedquinn.ion.generated

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

public object IonSpecification : ParserSpecification {
  public override val grammar: Grammar = buildGrammar("IonNode", "expr") {
    packageName("io.johnedquinn.ion.generated")
    tokens {
      "COLON" - ":"
      "PAREN_LEFT" - "\\("
      "PAREN_RIGHT" - "\\)"
      "SYMBOL_QUOTED" - "'((\\')|[^'])*'"
      "SYMBOL" - "[a-z][A-Z]+"
      "NUMBER" - "[0-9]+"
      "LITERAL_STRING" - "\\\"((\\\")|[^\\\"])*\\\""
    }
    "_generated_0" eq buildGeneratedRule(this, "_generated_0") {
      "_generated_1" eq "EPSILON"
      "_generated_2" eq "annotation"
    }
    "_generated_3" eq buildGeneratedRule(this, "_generated_3") {
      "_generated_4" eq "expr"
      "_generated_5" eq "_generated_3" - "expr"
    }
    "_generated_6" eq buildGeneratedRule(this, "_generated_6") {
      "_generated_6" eq "SYMBOL"
      "_generated_6" eq "SYMBOL_QUOTED"
    }
    "expr" eq buildRule(this, "expr") {
      "annotated_expr" eq "_generated_0" - "expr"
      "number" eq "NUMBER"
      "sexp" eq "PAREN_LEFT" - "_generated_3" - "PAREN_RIGHT"
      "symbol" eq "_generated_6"
      "string" eq "LITERAL_STRING"
    }
    "_generated_7" eq buildGeneratedRule(this, "_generated_7") {
      "_generated_7" eq "SYMBOL"
      "_generated_7" eq "SYMBOL_QUOTED"
    }
    "annotation" eq buildRule(this, "annotation") {
      "annotation" eq "_generated_7" - "COLON" - "COLON"
    }
  }


  private val nodeLambdaList: List<NodeCreator> = initializeLambdas()

  public override fun getTable(): String = """,S26,,S4,,S29,S28,S3,S25,S1,,S24,,S30,S27
  ,S26,,S4,,S29,S28,S3,S25,S1,,S24,S2,S30,S27
  A0,,,,,,,,,,,,,,
  A0,,,,,,,,,,,,,,
  ,S16,,S11,,S19,S18,S10,S15,S8,S5,S14,S23,S20,S17
  ,S16,,S11,S6,S19,S18,S10,S15,S8,,S14,S7,S20,S17
  A0,,,,,,,,,,,,,,
  ,R3,,R3,R3,R3,R3,R3,R3,,,,,,
  ,S16,,S11,,S19,S18,S10,S15,S8,,S14,S9,S20,S17
  ,A1,,A3,A4,A5,A6,A7,A8,,,,,,
  ,A1,,A3,A4,A5,A6,A7,A8,,,,,,
  ,S16,,S11,,S19,S18,S10,S15,S8,S12,S14,S23,S20,S17
  ,S16,,S11,S13,S19,S18,S10,S15,S8,,S14,S7,S20,S17
  ,A1,,A3,A4,A5,A6,A7,A8,,,,,,
  ,A1,,A3,A4,A5,A6,A7,A8,,,,,,
  ,A1,,A3,A4,A5,A6,A7,A8,,,,,,
  ,R0,,R0,R0,R0,R0,R0,R0,,,,,,
  ,R1,,R1,R1,R1,R1,R1,R1,,,,,,
  ,R4,R11,R4,R4,R4,R4,R4,R4,,,,,,
  ,R5,R12,R5,R5,R5,R5,R5,R5,,,,,,
  ,,S21,,,,,,,,,,,,
  ,,S22,,,,,,,,,,,,
  ,R13,,R13,R13,R13,R13,R13,R13,,,,,,
  ,R2,,R2,R2,R2,R2,R2,R2,,,,,,
  A0,,,,,,,,,,,,,,
  A0,,,,,,,,,,,,,,
  R0,R0,,R0,,R0,R0,R0,R0,,,,,,
  R1,R1,,R1,,R1,R1,R1,R1,,,,,,
  R4,,R11,,,,,,,,,,,,
  R5,,R12,,,,,,,,,,,,
  ,,S31,,,,,,,,,,,,
  ,,S32,,,,,,,,,,,,
  R13,R13,,R13,,R13,R13,R13,R13,,,,,,"""

  private fun initializeLambdas(): List<NodeCreator> = buildList {
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> IonNode.ExprNode.AnnotatedExprNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> IonNode.ExprNode.NumberNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> IonNode.ExprNode.SexpNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> IonNode.ExprNode.SymbolNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> IonNode.ExprNode.StringNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> IonNode.AnnotationNode.AnnotationNode(state, children, parent, "null") })
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
