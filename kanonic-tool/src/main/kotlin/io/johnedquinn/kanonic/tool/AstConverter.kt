package io.johnedquinn.kanonic.tool

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.grammar.Grammar
import io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder
import io.johnedquinn.kanonic.runtime.grammar.Rule
import io.johnedquinn.kanonic.runtime.grammar.RuleReference
import io.johnedquinn.kanonic.runtime.grammar.RuleVariant
import io.johnedquinn.kanonic.runtime.grammar.SymbolReference
import io.johnedquinn.kanonic.runtime.grammar.TerminalReference
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
import io.johnedquinn.kanonic.syntax.generated.KanonicBaseVisitor
import io.johnedquinn.kanonic.syntax.generated.KanonicNode

internal object AstConverter : KanonicBaseVisitor<Any, AstConverter.Context>() {

    internal fun convert(node: Node): Grammar {
        val builder = GrammarBuilder("Placeholder", "Placeholder")
        val context = Context(builder, 0)
        visit(node, context)
        return builder.build()
    }

    internal data class Context(
        val grammarBuilder: GrammarBuilder,
        var generatedRule: Int
    )

    override fun visitConfigDefinition(node: KanonicNode.ConfigDefNode.ConfigDefinitionNode, ctx: Context) {
        val lhs = node.IDENT_LOWER_CASE()[0].token.content
        val rhs = visitText(node.text()[0], ctx)
        when (lhs.lowercase()) {
            "name" -> ctx.grammarBuilder.name = rhs
            "package" -> ctx.grammarBuilder.packageName(rhs)
            "root" -> ctx.grammarBuilder.start = rhs
            else -> error("Unrecognized")
        }
    }

    override fun visitText(node: KanonicNode.TextNode, ctx: Context): String {
        return when (node) {
            is KanonicNode.TextNode.TextNode -> visitText(node, ctx)
        }
    }

    override fun visitText(node: KanonicNode.TextNode.TextNode, ctx: Context): String {
        return node.IDENT_LOWER_CASE().getOrNull(0)?.token?.content
            ?: node.IDENT_UPPER_CASE().getOrNull(0)?.token?.content
            ?: node.LITERAL_STRING()[0].token.content.let {
                it.substring(1, it.lastIndex)
            }
    }

    override fun visitTokenDef(node: KanonicNode.TokenDefNode.TokenDefNode, ctx: Context) {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val definition = node.LITERAL_STRING()[0].token.content.let { it.substring(1, it.lastIndex) }
        val channel = node.IDENT_LOWER_CASE().getOrNull(0)?.token?.content ?: "main"
        val hidden = when (channel.lowercase()) {
            "hidden" -> true
            "main" -> false
            else -> error("Unsupported channel: $channel")
        }
        ctx.grammarBuilder.addToken(name, definition, hidden)
    }

    override fun visitRuleDef(node: KanonicNode.RuleDefNode.RuleDefNode, ctx: Context) {
        val name = node.IDENT_LOWER_CASE()[0].token.content
        if (node.ruleVariant().size > 1) {
            val count = node.ruleVariant().any {
                it as KanonicNode.RuleVariantNode.VariantNode
                it.IDENT_LOWER_CASE().getOrNull(0)?.token?.content == null
            }
            if (count) {
                throw RuntimeException("Some variants are missing aliases for $name")
            }
        }
        val allVariants = node.ruleVariant() + node.alternative().map { it as KanonicNode.AlternativeNode.AlternativeNode }.flatMap { it.ruleVariant() }
        println(allVariants)
        val items = allVariants.map {
            it as KanonicNode.RuleVariantNode.VariantNode
            val items = it.ruleItem().map { item ->
                visitRuleItem(item, ctx)
            }
            val alias = it.IDENT_LOWER_CASE().getOrNull(0)?.token?.content ?: name
            RuleVariant(alias, name, items)
        }
        val rule = Rule(name, items, false)
        ctx.grammarBuilder.add(rule = rule)
    }

    override fun visitRuleItem(node: KanonicNode.RuleItemNode, ctx: Context): SymbolReference {
        return super.visitRuleItem(node, ctx) as SymbolReference
    }

    override fun visitItemToken(node: KanonicNode.RuleItemNode.ItemTokenNode, ctx: Context): TerminalReference {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val index = ctx.grammarBuilder.tokens.find { it.name == name }?.index ?: error("Couldn't find token reference: $name")
        return TerminalReference(index)
    }

    override fun visitItemRule(node: KanonicNode.RuleItemNode.ItemRuleNode, ctx: Context): RuleReference {
        val name = node.IDENT_LOWER_CASE()[0].token.content
        return RuleReference(name)
    }

    override fun visitItemGroup(node: KanonicNode.RuleItemNode.ItemGroupNode, ctx: Context): SymbolReference {
        val name = getGeneratedRuleName(ctx)
        val items = node.ruleItem().map { visitRuleItem(it, ctx) }
        val variant = RuleVariant(name, name, items)
        val rule = Rule(name, listOf(variant), true)
        ctx.grammarBuilder.add(rule)
        return RuleReference(name)
    }

    override fun visitItemFlagged(node: KanonicNode.RuleItemNode.ItemFlaggedNode, ctx: Context): SymbolReference {
        val name = getGeneratedRuleName(ctx)
        val variantEmptyName = getGeneratedRuleName(ctx)
        val variantName = getGeneratedRuleName(ctx)
        val nodeRef = visitRuleItem(node.ruleItem()[0], ctx)
        val variantItems1 = when (node.itemFlag()[0]) {
            is KanonicNode.ItemFlagNode.ItemFlagQuestionNode,
            is KanonicNode.ItemFlagNode.ItemFlagAsteriskNode -> listOf(TerminalReference(TokenLiteral.ReservedTypes.EPSILON))
            is KanonicNode.ItemFlagNode.ItemFlagPlusNode -> listOf(nodeRef)
        }
        val items = when (node.itemFlag()[0]) {
            is KanonicNode.ItemFlagNode.ItemFlagAsteriskNode,
            is KanonicNode.ItemFlagNode.ItemFlagPlusNode -> listOf(RuleReference(name), nodeRef)
            is KanonicNode.ItemFlagNode.ItemFlagQuestionNode -> listOf(nodeRef)
        }
        val variant1 = RuleVariant(variantEmptyName, name, variantItems1)
        val variant2 = RuleVariant(variantName, name, items)
        ctx.grammarBuilder.add(Rule(name, listOf(variant1, variant2), true))
        return RuleReference(name)
    }

    override fun visitTerminal(node: TerminalNode, ctx: Context) { /** SKIP **/ }

    override fun defaultReturn(node: KanonicNode, ctx: Context) { /** SKIP **/ }

    private fun getGeneratedRuleName(ctx: Context) = "_generated_${ctx.generatedRule++}"
}
