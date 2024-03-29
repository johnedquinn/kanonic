package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import io.johnedquinn.kanonic.gen.impl.AutomatonGenerator
import io.johnedquinn.kanonic.gen.impl.BaseVisitorGenerator
import io.johnedquinn.kanonic.gen.impl.GrammarSpec
import io.johnedquinn.kanonic.gen.impl.GrammarUtils
import io.johnedquinn.kanonic.gen.impl.MetadataGenerator
import io.johnedquinn.kanonic.gen.impl.NodeGenerator
import io.johnedquinn.kanonic.gen.impl.RuleSpec
import io.johnedquinn.kanonic.gen.impl.TableGenerator
import io.johnedquinn.kanonic.gen.impl.VariantSpec
import io.johnedquinn.kanonic.gen.impl.VisitorGenerator
import io.johnedquinn.kanonic.runtime.grammar.Grammar
import io.johnedquinn.kanonic.runtime.grammar.RuleReference
import io.johnedquinn.kanonic.runtime.grammar.SymbolReference
import io.johnedquinn.kanonic.runtime.grammar.TerminalReference
import io.johnedquinn.kanonic.runtime.parse.TokenLiteral

public object KanonicGenerator {

    /**
     * Generates all files
     */
    public fun generate(grammar: Grammar): List<FileSpec> {
        val spec = grammar.toSpec()
        return listOf(
            MetadataGenerator.generate(spec),
            NodeGenerator.generate(spec),
            VisitorGenerator.generate(spec),
            BaseVisitorGenerator.generate(spec)
        )
    }

    private fun Grammar.toSpec(): GrammarSpec {
        val automaton = AutomatonGenerator().generate(this)
        val table = TableGenerator(this, automaton).generate()

        val grammarNodeName = GrammarUtils.getGrammarNodeName(this)
        val visitorName = GrammarUtils.getGeneratedVisitorName(this)
        val topNodeClassName = ClassName(this.options.packageName!!, grammarNodeName)
        val rules = this.rules.map { rule ->
            val ruleName = GrammarUtils.getGeneratedNodeName(rule.name)
            val ruleClassName = ClassName(topNodeClassName.canonicalName, ruleName)
            val variants = rule.variants.map { variant ->
                val variantName = GrammarUtils.getGeneratedNodeName(variant.name)
                val className = ClassName(ruleClassName.canonicalName, variantName)
                val allItems = mutableSetOf<SymbolReference>()
                allItems.addAll(variant.items)
                var remaining = true
                while (remaining) {
                    remaining = false
                    val toAdd = mutableSetOf<SymbolReference>()
                    allItems.forEach { item ->
                        if (item is TerminalReference) return@forEach
                        val referencedRule = this.rules.find {
                            it.name == item.getName(this)
                        } ?: error("Could not find rule $item")
                        if (referencedRule.generated) {
                            referencedRule.variants.forEach {
                                toAdd.addAll(it.items)
                            }
                        }
                    }
                    val added = allItems.addAll(toAdd)
                    if (added) {
                        remaining = true
                    }
                }
                val implicitItems = allItems.filter {
                    when (it) {
                        is TerminalReference -> it.type != TokenLiteral.ReservedTypes.EPSILON
                        is RuleReference -> {
                            rules.find { rule -> rule.name == it.name }?.generated?.not() ?: false
                        }
                    }
                }
                VariantSpec(
                    variant.name,
                    variantName,
                    "visit${GrammarUtils.getNormalizedName(variant.name)}",
                    variant.items,
                    implicitItems,
                    className,
                    rule.generated,
                    rule.alias
                )
            }
            RuleSpec(
                rule.name,
                ruleName,
                "visit${GrammarUtils.getNormalizedName(rule.name)}",
                variants,
                ruleClassName,
                rule.generated,
                rule.alias
            )
        }
        val visitorClassName = ClassName(this.options.packageName!!, visitorName)
        return GrammarSpec(
            this.options.grammarName,
            GrammarUtils.getMetadataName(this),
            grammarNodeName,
            visitorName,
            GrammarUtils.getGeneratedBaseVisitorName(this),
            this.options.packageName!!,
            rules,
            this.options.start,
            this.tokens,
            topNodeClassName,
            visitorClassName,
            table
        )
    }
}
