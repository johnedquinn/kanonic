package io.johnedquinn.kanonic.tool

import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.RuleVariant
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.dsl.GrammarBuilder
import io.johnedquinn.kanonic.parse.TerminalNode
import io.johnedquinn.kanonic.syntax.generated.KanonicBaseVisitor
import io.johnedquinn.kanonic.syntax.generated.KanonicNode

internal object AstConverter : KanonicBaseVisitor<Any, GrammarBuilder>() {

    override fun visitConfigDefinition(node: KanonicNode.ConfigDefNode.ConfigDefinitionNode, ctx: GrammarBuilder) {
        val lhs = node.IDENT_CAMEL_CASE()[0].token.content
        val rhs = node.IDENT_CAMEL_CASE()[1].token.content
        when (lhs.lowercase()) {
            "name" -> ctx.name = rhs
            "package" -> ctx.packageName(rhs)
            "root" -> ctx.start = rhs
            else -> error("Unrecognized")
        }
    }

    override fun visitToken(node: KanonicNode.TokenDefNode.TokenNode, ctx: GrammarBuilder) {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val definition = node.LITERAL_STRING()[0].token.content
        ctx.addToken(name, definition, false)
    }

    override fun visitRule(node: KanonicNode.RuleDefNode.RuleNode, ctx: GrammarBuilder) {
        val name = node.IDENT_CAMEL_CASE()[0].token.content
        val items = node.rule_variant().map {
            it as KanonicNode.RuleVariantNode.VariantNode
            val items = it.rule_item().map { item ->
                visitRuleItem(item, ctx)
            }
            val alias = node.IDENT_CAMEL_CASE()[0].token.content
            RuleVariant(alias, name, items)
        }
        val rule = Rule(name, items, false)
        ctx.add(rule = rule)
        super.visitRule(node, ctx)
    }

    override fun visitRuleItem(node: KanonicNode.RuleItemNode, ctx: GrammarBuilder): SymbolReference {
        return super.visitRuleItem(node, ctx) as SymbolReference
    }

    override fun visitTokenReference(node: KanonicNode.RuleItemNode.TokenReferenceNode, ctx: GrammarBuilder): TerminalReference {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val index = ctx.tokens.find { it.name == name }?.index ?: error("Couldn't find token reference")
        return TerminalReference(index)
    }

    override fun visitRuleReference(node: KanonicNode.RuleItemNode.RuleReferenceNode, ctx: GrammarBuilder): RuleReference {
        val name = node.IDENT_CAMEL_CASE()[0].token.content
        return RuleReference(name)
    }

    override fun visitTerminal(node: TerminalNode, ctx: GrammarBuilder) { /** SKIP **/ }

    override fun defaultReturn(node: KanonicNode, ctx: GrammarBuilder) { /** SKIP **/ }
}
