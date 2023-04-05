package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.RuleVariant
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference

class RuleBuilder(var grammarBuilder: GrammarBuilder, var ruleName: String, var generated: Boolean = false) {
    private val variants = mutableListOf<RuleVariant>()
    public fun build() = Rule(ruleName, variants, generated)

    companion object {
        @JvmStatic
        public fun buildRule(grammarBuilder: GrammarBuilder, name: String, f: RuleBuilder.() -> Unit): Rule {
            val ruleBuilder = RuleBuilder(grammarBuilder, name, false)
            ruleBuilder.f()
            return ruleBuilder.build()
        }

        @JvmStatic
        public fun buildGeneratedRule(grammarBuilder: GrammarBuilder, name: String, f: RuleBuilder.() -> Unit): Rule {
            val ruleBuilder = RuleBuilder(grammarBuilder, name, true)
            ruleBuilder.f()
            return ruleBuilder.build()
        }
    }

    infix fun String.eq(other: List<SymbolReference>): RuleVariant {
        val rule = RuleVariant(this, ruleName, other)
        variants.add(rule)
        return rule
    }

    infix fun String.eq(other: String): RuleVariant {
        val rule = RuleVariant(this, ruleName, listOf(getReference(other)))
        variants.add(rule)
        return rule
    }

    fun add(name: String, f: RuleBuilder.() -> List<SymbolReference>): RuleVariant {
        val rule = RuleVariant(name, ruleName, this.f())
        this.variants.add(rule)
        return rule
    }

    fun add(name: String, def: List<SymbolReference>): RuleVariant {
        val rule = RuleVariant(name, ruleName, def)
        this.variants.add(rule)
        return rule
    }

    infix fun Rule.generated(other: Boolean): Rule {
        this.generated = other
        return this
    }

    operator fun String.minus(other: String): List<SymbolReference> {
        return listOf(getReference(this), getReference(other))
    }

    operator fun String.minus(other: List<SymbolReference>): List<SymbolReference> {
        return listOf(getReference(this)) + other
    }

    operator fun List<SymbolReference>.minus(other: String): List<SymbolReference> {
        return this + listOf(getReference(other))
    }

    public fun addVariant(variant: RuleVariant) = this.apply {
        variants.add(variant)
    }

    private fun isRuleReference(str: String): Boolean {
        return str.uppercase().equals(str).not()
    }

    private fun getTerminalReference(ref: String) = grammarBuilder.tokens.firstOrNull {
        it.name == ref
    }?.let { TerminalReference(it.index) }

    private fun getReference(ref: String) = getTerminalReference(ref) ?: RuleReference(ref)
}
