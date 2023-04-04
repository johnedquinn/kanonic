package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral
import io.johnedquinn.kanonic.utils.Logger

class GrammarBuilder(var name: String, var start: String) {
    private val rules = mutableListOf<Rule>()
    public var tokens: MutableList<TokenDefinition> = mutableListOf()
    private var packageName: String? = null

    init {
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EOF, "EOF", "", false))
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EPSILON, "EPSILON", "", false))
    }

    companion object {
        @JvmStatic
        public fun buildGrammar(name: String, start: String, f: GrammarBuilder.() -> Unit): Grammar {
            val grammar = GrammarBuilder(name, start)
            grammar.f()
            return grammar.build()
        }
    }

    public fun build(): Grammar {
        Logger.debug(rules.toString())
        return Grammar(rules, Grammar.Options(name, RuleReference(start), packageName), tokens)
    }

    fun add(name: String, def: List<SymbolReference>): Rule {
        val rule = Rule(name, def)
        this@GrammarBuilder.rules.add(rule)
        return rule
    }

    fun add(rule: Rule) = this.apply {
        this.rules.add(rule)
    }

    fun add(name: String, f: GrammarBuilder.() -> List<SymbolReference>): Rule {
        val rule = Rule(name, this.f())
        this@GrammarBuilder.rules.add(rule)
        return rule
    }

    infix fun String.eq(other: List<SymbolReference>): Rule {
        val rule = Rule(this, other)
        this@GrammarBuilder.rules.add(rule)
        return rule
    }

    infix fun String.eq(other: String): Rule {
        val rule = Rule(this, listOf(getReference(other)))
        this@GrammarBuilder.rules.add(rule)
        return rule
    }

    infix fun Rule.alias(other: String): Rule {
        this.alias = other
        return this
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

    operator fun String.unaryPlus(): List<SymbolReference> {
        return listOf(RuleReference(this))
    }

    fun addToken(name: String, def: String, hidden: Boolean): GrammarBuilder = this.apply {
        tokens.add(TokenDefinition(tokens.size, name, def, hidden))
    }

    fun tokens(f: LexerBuilder.() -> Unit): GrammarBuilder {
        val l = LexerBuilder()
        l.f()
        l.build().forEach {
            val toAdd = TokenDefinition(tokens.size, it.name, it.def, it.hidden)
            tokens.add(toAdd)
        }
        Logger.debug(tokens.toString())
        return this
    }

    fun packageName(name: String) {
        this.packageName = name
    }

    private fun isRuleReference(str: String): Boolean {
        return str.uppercase().equals(str).not()
    }

    private fun getTerminalReference(ref: String) = tokens.firstOrNull {
        it.name == ref
    }?.let { TerminalReference(it.index) } ?: error("Unable to find token reference for $ref.")

    private fun getReference(ref: String) = when (isRuleReference(ref)) {
        true -> RuleReference(ref)
        false -> getTerminalReference(ref)
    }
}

