package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.parse.TokenDefinition
import io.johnedquinn.kanonic.parse.TokenLiteral

class GrammarDsl(private val name: String, private val start: String) {
    private val rules = mutableListOf<Rule>()
    private var tokens: List<TokenDefinition> = emptyList()

    fun toGrammar(): Grammar {
        println(rules)
        return Grammar(rules, Grammar.Options(name, RuleReference(start)), tokens)
    }

    fun add(name: String, def: List<SymbolReference>): Rule {
        val rule = Rule(name, def)
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    fun add(name: String, f: GrammarDsl.() -> List<SymbolReference>): Rule {
        val rule = Rule(name, this.f())
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    infix fun String.eq(other: List<SymbolReference>): Rule {
        val rule = Rule(this, other)
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    infix fun String.eq(other: String): Rule {
        val rule = Rule(this, listOf(getReference(other)))
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    infix fun Rule.alias(other: String): Rule {
        this.alias = other
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

    fun tokens(f: LexerDsl.() -> Unit): GrammarDsl {
        val l = LexerDsl()
        l.f()
        tokens = l.build()
        println(tokens)
        return this
    }

    private fun isRuleReference(str: String): Boolean {
        return str.lowercase().equals(str)
    }

    private fun getTerminalReference(ref: String) = tokens.firstOrNull {
        it.name == ref
    }?.let { TerminalReference(it.index) } ?: error("Unable to find token reference for $ref.")

    private fun getReference(ref: String) = when (isRuleReference(ref)) {
        true -> RuleReference(ref)
        false -> getTerminalReference(ref)
    }
}

public fun grammar(name: String, start: String, f: GrammarDsl.() -> Unit): GrammarDsl {
    val grammar = GrammarDsl(name, start)
    grammar.f()
    return grammar
}

class LexerDsl {
    private val tokens = mutableListOf<TokenDefinition>()
    init {
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EOF, "EOF", ""))
        tokens.add(TokenDefinition(TokenLiteral.ReservedTypes.EPSILON, "EPSILON", ""))
    }
    operator fun String.minus(other: String) {
        val token = TokenDefinition(tokens.size, this, other)
        tokens.add(token)
    }

    fun build() = tokens.toList()
}

