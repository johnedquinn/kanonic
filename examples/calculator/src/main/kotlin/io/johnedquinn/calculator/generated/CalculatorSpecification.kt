package io.johnedquinn.calculator.generated

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

public object CalculatorSpecification : ParserSpecification {
  public override val grammar: Grammar = buildGrammar("CalculatorNode", "root") {
    packageName("io.johnedquinn.calculator.generated")
    tokens {
      "INTEGER" - "[0-9]+"
      "PLUS" - "\\+"
      "MINUS" - "-"
      "ASTERISK" - "\\*"
      "SLASH_FORWARD" - "/"
      "CARROT" - "\\^"
      "PAREN_LEFT" - "\\("
      "PAREN_RIGHT" - "\\)"
      "WS" - ("[\\s]" channel "hidden")
    }
    "root" eq buildRule(this, "root") {
      "root" eq "expr"
    }
    "_generated_0" eq buildGeneratedRule(this, "_generated_0") {
      "lhs" eq "expr"
    }
    "_generated_1" eq buildGeneratedRule(this, "_generated_1") {
      "_generated_1" eq "PLUS"
      "_generated_1" eq "MINUS"
    }
    "_generated_2" eq buildGeneratedRule(this, "_generated_2") {
      "op" eq "_generated_1"
    }
    "_generated_3" eq buildGeneratedRule(this, "_generated_3") {
      "rhs" eq "math_op"
    }
    "expr" eq buildRule(this, "expr") {
      "expr_plus" eq "_generated_0" - "_generated_2" - "_generated_3"
      "expr_atomic" eq "math_op"
    }
    "_generated_4" eq buildGeneratedRule(this, "_generated_4") {
      "lhs" eq "math_op"
    }
    "_generated_5" eq buildGeneratedRule(this, "_generated_5") {
      "_generated_5" eq "SLASH_FORWARD"
      "_generated_5" eq "ASTERISK"
    }
    "_generated_6" eq buildGeneratedRule(this, "_generated_6") {
      "op" eq "_generated_5"
    }
    "_generated_7" eq buildGeneratedRule(this, "_generated_7") {
      "rhs" eq "math_op_second"
    }
    "math_op" eq buildRule(this, "math_op") {
      "expr_div" eq "_generated_4" - "_generated_6" - "_generated_7"
      "math_op_fallback" eq "math_op_second"
    }
    "_generated_8" eq buildGeneratedRule(this, "_generated_8") {
      "lhs" eq "math_op_second"
    }
    "_generated_9" eq buildGeneratedRule(this, "_generated_9") {
      "rhs" eq "atomic"
    }
    "math_op_second" eq buildRule(this, "math_op_second") {
      "math_op_second" eq "_generated_8" - "CARROT" - "_generated_9"
      "expr_atom" eq "atomic"
    }
    "_generated_10" eq buildGeneratedRule(this, "_generated_10") {
      "exp" eq "expr"
    }
    "atomic" eq buildRule(this, "atomic") {
      "int" eq "INTEGER"
      "paren" eq "PAREN_LEFT" - "_generated_10" - "PAREN_RIGHT"
    }
  }


  private val nodeLambdaList: List<NodeCreator> = initializeLambdas()

  public override fun getTable(): String = """,,S14,,,,,,S15,,,,S2,,,,S1,S6,,,,S46,S10,,S45,,S44
  A0,,,R1,R1,,,,,,,,,,,,,,,,,,,,,,
  ,,,S42,S43,,,,,,,,,S41,S3,,,,,,,,,,,,
  ,,S14,,,,,,S15,,,,,,,S4,,S6,,,,S5,S10,,S45,,S44
  R6,,,R6,R6,,,,,,,,,,,,,,,,,,,,,,
  R5,,,R5,R5,R8,R8,,,,,,,,,,,,,,,,,,,,
  ,,,,,S40,S39,,,,,,,,,,,,S38,S7,,,,,,,
  ,,S14,,,,,,S15,,,,,,,,,,,,S8,,S10,,S9,,S44
  R13,,,R13,R13,R13,R13,,,,,,,,,,,,,,,,,,,,
  R12,,,R12,R12,R12,R12,R15,,,,,,,,,,,,,,,,,,,
  ,,,,,,,S11,,,,,,,,,,,,,,,,,,,
  ,,S14,,,,,,S15,,,,,,,,,,,,,,,S12,,,S13
  R17,,,R17,R17,R17,R17,R17,,,,,,,,,,,,,,,,,,,
  R16,,,R16,R16,R16,R16,R16,,,,,,,,,,,,,,,,,,,
  R20,,,R20,R20,R20,R20,R20,,,,,,,,,,,,,,,,,,,
  ,,S31,,,,,,S32,,,,S19,,,,S18,S23,,,,S35,S27,,S36,S16,S37
  ,,,,,,,,,S17,,,,,,,,,,,,,,,,,
  R21,,,R21,R21,R21,R21,R21,,,,,,,,,,,,,,,,,,,
  ,,,R1,R1,,,,,R19,,,,,,,,,,,,,,,,,
  ,,,S42,S43,,,,,,,,,S41,S20,,,,,,,,,,,,
  ,,S31,,,,,,S32,,,,,,,S21,,S23,,,,S22,S27,,S36,,S37
  ,,,R6,R6,,,,,R6,,,,,,,,,,,,,,,,,
  ,,,R5,R5,R8,R8,,,R5,,,,,,,,,,,,,,,,,
  ,,,,,S40,S39,,,,,,,,,,,,S38,S24,,,,,,,
  ,,S31,,,,,,S32,,,,,,,,,,,,S25,,S27,,S26,,S37
  ,,,R13,R13,R13,R13,,,R13,,,,,,,,,,,,,,,,,
  ,,,R12,R12,R12,R12,R15,,R12,,,,,,,,,,,,,,,,,
  ,,,,,,,S28,,,,,,,,,,,,,,,,,,,
  ,,S31,,,,,,S32,,,,,,,,,,,,,,,S29,,,S30
  ,,,R17,R17,R17,R17,R17,,R17,,,,,,,,,,,,,,,,,
  ,,,R16,R16,R16,R16,R16,,R16,,,,,,,,,,,,,,,,,
  ,,,R20,R20,R20,R20,R20,,R20,,,,,,,,,,,,,,,,,
  ,,S31,,,,,,S32,,,,S19,,,,S18,S23,,,,S35,S27,,S36,S33,S37
  ,,,,,,,,,S34,,,,,,,,,,,,,,,,,
  ,,,R21,R21,R21,R21,R21,,R21,,,,,,,,,,,,,,,,,
  ,,,R7,R7,R8,R8,,,R7,,,,,,,,,,,,,,,,,
  ,,,R14,R14,R14,R14,R15,,R14,,,,,,,,,,,,,,,,,
  ,,,R18,R18,R18,R18,R18,,R18,,,,,,,,,,,,,,,,,
  ,,R11,,,,,,R11,,,,,,,,,,,,,,,,,,
  ,,R9,,,,,,R9,,,,,,,,,,,,,,,,,,
  ,,R10,,,,,,R10,,,,,,,,,,,,,,,,,,
  ,,R4,,,,,,R4,,,,,,,,,,,,,,,,,,
  ,,R2,,,,,,R2,,,,,,,,,,,,,,,,,,
  ,,R3,,,,,,R3,,,,,,,,,,,,,,,,,,
  R18,,,R18,R18,R18,R18,R18,,,,,,,,,,,,,,,,,,,
  R14,,,R14,R14,R14,R14,R15,,,,,,,,,,,,,,,,,,,
  R7,,,R7,R7,R8,R8,,,,,,,,,,,,,,,,,,,,"""

  private fun initializeLambdas(): List<NodeCreator> = buildList {
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.RootNode.RootNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "lhs") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "op") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "rhs") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.ExprNode.ExprPlusNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.ExprNode.ExprAtomicNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "lhs") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "op") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "rhs") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.MathOpNode.ExprDivNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.MathOpNode.MathOpFallbackNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "lhs") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "rhs") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.MathOpSecondNode.MathOpSecondNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.MathOpSecondNode.ExprAtomNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> GeneratedNode(state, children, parent, "exp") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.AtomicNode.IntNode(state, children, parent, "null") })
    add(NodeCreator { state, children, parent, alias -> CalculatorNode.AtomicNode.ParenNode(state, children, parent, "null") })
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
