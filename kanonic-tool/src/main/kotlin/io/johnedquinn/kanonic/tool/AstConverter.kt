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
        // println(KanonicNodeFormatter.format(node))
        visit(node, context)
        return builder.build()
    }

    internal data class Context(
        val grammarBuilder: GrammarBuilder,
        var generatedRule: Int
    )

    override fun visitConfigDef(node: KanonicNode.ConfigDefNode.ConfigDefNode, ctx: Context) {
        val lhs = node.config_key().token.content
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

    override fun visitToken(node: KanonicNode.TokenNode.TokenNode, ctx: Context) {
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

    override fun visitRule(node: KanonicNode.RuleNode.RuleNode, ctx: Context) {
        val name = node.IDENT_LOWER_CASE()[0].token.content
        if (node.variant().size > 1) {
            val count = node.variant().any {
                it as KanonicNode.VariantNode.VariantNode
                it.IDENT_LOWER_CASE().getOrNull(0)?.token?.content == null
            }
            if (count) {
                throw RuntimeException("Some variants are missing aliases for $name")
            }
        }
        val allVariants = node.variant() + node.alternative().map { it as KanonicNode.AlternativeNode.AlternativeNode }.flatMap { it.variant() }
        val items = allVariants.map {
            it as KanonicNode.VariantNode.VariantNode
            val items = it.item().map { item ->
                visitItem(item, ctx)
            }
            val alias = it.IDENT_LOWER_CASE().getOrNull(0)?.token?.content ?: name
            RuleVariant(alias, name, items, null)
        }
        val rule = Rule(name, items, false)
        ctx.grammarBuilder.add(rule = rule)
    }

    override fun visitItem(node: KanonicNode.ItemNode, ctx: Context): SymbolReference {
        return super.visitItem(node, ctx) as SymbolReference
    }

    override fun visitItem(node: KanonicNode.ItemNode.ItemNode, ctx: Context): SymbolReference {
        return super.visitItem(node, ctx) as SymbolReference
    }

    override fun visitItemAliased(node: KanonicNode.ItemNode.ItemAliasedNode, ctx: Context): SymbolReference {
        val alias = node.IDENT_LOWER_CASE()[0].token.content
        val baseItem = visitBaseItem(node.baseItem()[0], ctx)
        val type = when (val base = node.baseItem()[0]) {
            is KanonicNode.BaseItemNode.ItemGroupNode -> {
                val baseChildClass = base.item()[0].getBaseItem()
                val conformant = base.item().all {
                    it.getBaseItem()::class == baseChildClass::class
                }
                when (conformant) {
                    true -> baseItem
                    false -> throw RuntimeException("Not all are equals")
                }
            }
            is KanonicNode.BaseItemNode.ItemFlaggedNode,
            is KanonicNode.BaseItemNode.ItemRuleNode,
            is KanonicNode.BaseItemNode.ItemTokenNode -> baseItem
        }
        val generatedName = getGeneratedRuleName(ctx)
        val newVariant = RuleVariant(alias, generatedName, listOf(type), alias)
        val newRule = Rule(generatedName, listOf(newVariant), true, alias = alias)
        ctx.grammarBuilder.add(newRule)
        return RuleReference(generatedName)
    }

    private fun KanonicNode.ItemNode.getBaseItem() = when (this) {
        is KanonicNode.ItemNode.ItemNode -> this.baseItem()[0]
        is KanonicNode.ItemNode.ItemAliasedNode -> this.baseItem()[0]
    }

    override fun visitItemToken(node: KanonicNode.BaseItemNode.ItemTokenNode, ctx: Context): TerminalReference {
        val name = node.IDENT_UPPER_CASE()[0].token.content
        val index = ctx.grammarBuilder.tokens.find { it.name == name }?.index ?: error("Couldn't find token reference: $name")
        return TerminalReference(index)
    }

    override fun visitItemRule(node: KanonicNode.BaseItemNode.ItemRuleNode, ctx: Context): RuleReference {
        val name = node.IDENT_LOWER_CASE()[0].token.content
        return RuleReference(name)
    }

    override fun visitItemGroup(node: KanonicNode.BaseItemNode.ItemGroupNode, ctx: Context): SymbolReference {
        val name = getGeneratedRuleName(ctx)
        val appendedVariants = node.appendedItems().map {
            val items = visitAppendedItems(it, ctx)
            RuleVariant(name, name, items, null)
        }
        val items = node.item().map { visitItem(it, ctx) }
        val variant = RuleVariant(name, name, items, null)
        val rule = Rule(name, listOf(variant) + appendedVariants, true)
        ctx.grammarBuilder.add(rule)
        return RuleReference(name)
    }

    override fun visitAppendedItems(node: KanonicNode.AppendedItemsNode, ctx: Context): List<SymbolReference> {
        return super.visitAppendedItems(node, ctx) as List<SymbolReference>
    }

    override fun visitAppendedItems_0(node: KanonicNode.AppendedItemsNode.AppendedItems_0Node, ctx: Context): List<SymbolReference> {
        val items = node.item().map {
            visitItem(it, ctx)
        }
        return items
    }

    override fun visitAppendedItems_1(node: KanonicNode.AppendedItemsNode.AppendedItems_1Node, ctx: Context): List<SymbolReference> {
        return emptyList()
    }

    override fun visitItemFlagged(node: KanonicNode.BaseItemNode.ItemFlaggedNode, ctx: Context): SymbolReference {
        val name = getGeneratedRuleName(ctx)
        val variantEmptyName = getGeneratedRuleName(ctx)
        val variantName = getGeneratedRuleName(ctx)
        val nodeRef = visitBaseItem(node.baseItem()[0], ctx)
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
        val variant1 = RuleVariant(variantEmptyName, name, variantItems1, null)
        val variant2 = RuleVariant(variantName, name, items, null)
        ctx.grammarBuilder.add(Rule(name, listOf(variant1, variant2), true))
        return RuleReference(name)
    }

    override fun visitBaseItem(node: KanonicNode.BaseItemNode, ctx: Context): SymbolReference {
        return super.visitBaseItem(node, ctx) as SymbolReference
    }

    override fun visitTerminal(node: TerminalNode, ctx: Context) { /** SKIP **/ }

    override fun defaultVisit(node: KanonicNode, ctx: Context): Any {
        var value: Any = Unit
        for (child in node.children) {
            value = child.accept(this, ctx)
        }
        return value
    }

    override fun defaultReturn(node: KanonicNode, ctx: Context) { /** SKIP **/ }

    private fun getGeneratedRuleName(ctx: Context) = "_generated_${ctx.generatedRule++}"
}
