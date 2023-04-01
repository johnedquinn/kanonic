package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.gen.impl.BaseVisitorGenerator
import io.johnedquinn.kanonic.gen.impl.GrammarUtils
import io.johnedquinn.kanonic.gen.impl.MetadataGenerator
import io.johnedquinn.kanonic.gen.impl.NodeGenerator
import io.johnedquinn.kanonic.gen.impl.VisitorGenerator

public object KanonicGenerator {

    /**
     * Generates all files
     */
    public fun generate(grammar: Grammar): List<FileSpec> {
        val spec = grammar.toSpec()
        return listOf(
            MetadataGenerator.generate(grammar, spec),
            NodeGenerator.generate(grammar, spec),
            VisitorGenerator.generate(spec),
            BaseVisitorGenerator.generate(spec)
        )
    }

    private fun Grammar.toSpec(): GrammarSpec {
        val grammarNodeName = GrammarUtils.getGrammarNodeName(this)
        val visitorName = GrammarUtils.getGeneratedVisitorName(this)
        val topNodeClassName = ClassName(this.options.packageName!!, grammarNodeName)
        val rules = this.rules.groupBy { it.name }.map { (rule, ruleVariants) ->
            val ruleName = GrammarUtils.getGeneratedClassName(rule)
            val ruleClassName = ClassName(topNodeClassName.canonicalName, ruleName)
            val variants = ruleVariants.map { variant ->
                val variantName = GrammarUtils.getGeneratedClassName(variant.alias)
                val className = ClassName(ruleClassName.canonicalName, variantName)
                VariantSpec(variant.alias, variantName, "visit${variant.alias}", variant.items, className)
            }
            RuleSpec(rule, ruleName, "visit$rule", variants, ruleClassName)
        }
        val visitorClassName = ClassName(this.options.packageName!!, "${this.options.grammarName}Visitor")
        return GrammarSpec(
            this.options.grammarName,
            visitorName,
            GrammarUtils.getGeneratedBaseVisitorName(this),
            this.options.packageName!!,
            rules,
            topNodeClassName,
            visitorClassName
        )
    }
}
