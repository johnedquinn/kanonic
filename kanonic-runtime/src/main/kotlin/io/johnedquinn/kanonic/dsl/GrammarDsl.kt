package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.TerminalReference
import io.johnedquinn.kanonic.TokenType
import io.johnedquinn.kanonic.parse.TokenDefinition

class GrammarDsl(private val name: String, private val start: String) {
    private val rules = mutableListOf<Rule>()
    private var tokens: List<TokenDefinition> = emptyList()

    fun toGrammar(): Grammar = Grammar(rules, Grammar.Options(name, RuleReference(start)), tokens)

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
        val rule = Rule(this, listOf(RuleReference(other)))
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    infix fun Rule.alias(other: String): Rule {
        this.alias = other
        return this
    }

    infix fun String.eq(other: TokenType): Rule {
        val rule = Rule(this, listOf(TerminalReference(other)))
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    operator fun String.minus(other: String): List<SymbolReference> {
        return listOf(RuleReference(this), RuleReference(other))
    }

    operator fun String.minus(other: TokenType): List<SymbolReference> {
        return listOf(RuleReference(this), TerminalReference(other))
    }

    operator fun String.minus(other: List<SymbolReference>): List<SymbolReference> {
        return listOf(RuleReference(this)) + other
    }

    operator fun TokenType.minus(other: TokenType): List<SymbolReference> {
        return listOf(TerminalReference(this), TerminalReference(other))
    }

    operator fun TokenType.minus(other: String): List<SymbolReference> {
        return listOf(TerminalReference(this), RuleReference(other))
    }

    operator fun TokenType.minus(other: List<SymbolReference>): List<SymbolReference> {
        return listOf(TerminalReference(this)) + other
    }

    operator fun List<SymbolReference>.minus(other: String): List<SymbolReference> {
        return this + listOf(RuleReference(other))
    }

    operator fun List<SymbolReference>.minus(other: TokenType): List<SymbolReference> {
        return this + listOf(TerminalReference(other))
    }

    operator fun TokenType.unaryPlus(): List<SymbolReference> {
        return listOf(TerminalReference(this))
    }

    operator fun String.unaryPlus(): List<SymbolReference> {
        return listOf(RuleReference(this))
    }

    fun tokens(f: LexerDsl.() -> Unit): GrammarDsl {
        val l = LexerDsl()
        l.f()
        tokens = l.build()
        return this
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
        tokens.add(TokenDefinition(tokens.size, "EOF", ""))
    }
    operator fun String.minus(other: String) {
        val token = TokenDefinition(tokens.size, this, other)
        tokens.add(token)
    }

    fun build() = tokens.toList()
}

