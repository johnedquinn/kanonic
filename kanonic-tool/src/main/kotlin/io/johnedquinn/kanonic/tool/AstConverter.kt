package io.johnedquinn.kanonic.tool

import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.dsl.GrammarBuilder
import io.johnedquinn.kanonic.parse.TerminalNode
import io.johnedquinn.kanonic.syntax.generated.KanonicBaseVisitor
import io.johnedquinn.kanonic.syntax.generated.KanonicNode

internal object AstConverter : KanonicBaseVisitor<Any, GrammarBuilder>() {

    override fun visitConfigDefinition(node: KanonicNode.configDefNode.ConfigDefinitionNode, ctx: GrammarBuilder) {
        println(node)
        val lhs = node.IDENT_CAMEL_CASE()[0].token.content
        val rhs = node.IDENT_CAMEL_CASE()[1].token.content
        when (lhs.lowercase()) {
            "name" -> ctx.name = rhs
            "package" -> ctx.packageName(rhs)
            "root" -> ctx.start = rhs
            else -> error("Unrecognized")
        }
    }

    override fun visitToken(node: KanonicNode.tokenDefNode.TokenNode, ctx: GrammarBuilder) {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val definition = node.LITERAL_STRING()[0].token.content
        ctx.addToken(name, definition, false)
    }

    override fun visitRule(node: KanonicNode.ruleDefNode.RuleNode, ctx: GrammarBuilder) {
        val name = node.IDENT_CAMEL_CASE()[0].token.content
        node.ruleVariant().forEach {
            it as KanonicNode.ruleVariantNode.VariantNode
            val items = it.ruleItem().map { item ->
                visitruleItem(item, ctx)
            }
            val alias = it.IDENT_CAMEL_CASE()[0].token.content
            val rule = Rule(name, items, alias)
            ctx.add(rule = rule)
        }
        super.visitRule(node, ctx)
    }

    override fun visitruleItem(node: KanonicNode.ruleItemNode, ctx: GrammarBuilder): SymbolReference {
        return super.visitruleItem(node, ctx) as SymbolReference
    }

    override fun visitTokenReference(node: KanonicNode.ruleItemNode.TokenReferenceNode, ctx: GrammarBuilder): TerminalReference {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val index = ctx.tokens.find { it.name == name }?.index ?: error("Couldn't find token reference")
        return TerminalReference(index)
    }

    override fun visitRuleReference(node: KanonicNode.ruleItemNode.RuleReferenceNode, ctx: GrammarBuilder): RuleReference {
        val name = node.IDENT_CAMEL_CASE()[0].token.content
        return RuleReference(name)
    }

    override fun visitTerminal(node: TerminalNode, ctx: GrammarBuilder) { /** SKIP **/ }

    override fun defaultReturn(node: KanonicNode, ctx: GrammarBuilder) { /** SKIP **/ }
}
