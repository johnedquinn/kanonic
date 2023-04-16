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
  public override val grammar: Grammar = buildGrammar("IonNode", "file") {
    packageName("io.johnedquinn.ion.generated")
    tokens {
      "COLON" - ":"
      "PAREN_LEFT" - "\\("
      "PAREN_RIGHT" - "\\)"
      "SYMBOL_QUOTED" - "'((\\')|[^'])*'"
      "SYMBOL" - "[a-zA-Z]+"
      "NUMBER" - "[0-9]+"
      "LITERAL_STRING" - "\\\"((\\\")|[^\\\"])*\\\""
    }
    "file" eq buildRule(this, "file") {
      "expr" eq "expr"
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

  public override fun getTable(): String = """,S27,,S5,,S30,S29,S4,S26,,S2,,S25,S1,S31,S28
  A0,,,,,,,,,,,,,,,
  ,S27,,S5,,S30,S29,S4,S26,,S2,,S25,S3,S31,S28
  R7,,,,,,,,,,,,,,,
  R8,,,,,,,,,,,,,,,
  ,S17,,S12,,S20,S19,S11,S16,,S9,S6,S15,S24,S21,S18
  ,S17,,S12,S7,S20,S19,S11,S16,,S9,,S15,S8,S21,S18
  R9,,,,,,,,,,,,,,,
  ,R4,,R4,R4,R4,R4,R4,R4,,,,,,,
  ,S17,,S12,,S20,S19,S11,S16,,S9,,S15,S10,S21,S18
  ,R7,,R7,R7,R7,R7,R7,R7,,,,,,,
  ,R8,,R8,R8,R8,R8,R8,R8,,,,,,,
  ,S17,,S12,,S20,S19,S11,S16,,S9,S13,S15,S24,S21,S18
  ,S17,,S12,S14,S20,S19,S11,S16,,S9,,S15,S8,S21,S18
  ,R9,,R9,R9,R9,R9,R9,R9,,,,,,,
  ,R10,,R10,R10,R10,R10,R10,R10,,,,,,,
  ,R11,,R11,R11,R11,R11,R11,R11,,,,,,,
  ,R1,,R1,R1,R1,R1,R1,R1,,,,,,,
  ,R2,,R2,R2,R2,R2,R2,R2,,,,,,,
  ,R5,R12,R5,R5,R5,R5,R5,R5,,,,,,,
  ,R6,R13,R6,R6,R6,R6,R6,R6,,,,,,,
  ,,S22,,,,,,,,,,,,,
  ,,S23,,,,,,,,,,,,,
  ,R14,,R14,R14,R14,R14,R14,R14,,,,,,,
  ,R3,,R3,R3,R3,R3,R3,R3,,,,,,,
  R10,,,,,,,,,,,,,,,
  R11,,,,,,,,,,,,,,,
  R1,R1,,R1,,R1,R1,R1,R1,,,,,,,
  R2,R2,,R2,,R2,R2,R2,R2,,,,,,,
  R5,,R12,,,,,,,,,,,,,
  R6,,R13,,,,,,,,,,,,,
  ,,S32,,,,,,,,,,,,,
  ,,S33,,,,,,,,,,,,,
  R14,R14,,R14,,R14,R14,R14,R14,,,,,,,"""

  private fun initializeLambdas(): List<NodeCreator> = buildList {
    add(NodeCreator { state, children, parent, alias -> IonNode.FileNode.ExprNode(state, children, parent, "null") })
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
