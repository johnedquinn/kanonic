package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.*

class GrammarDsl(private val name: String, private val start: String) {
    private val rules = mutableListOf<Rule>()

    fun toGrammar(): Grammar = Grammar(rules, Grammar.Options(name, RuleReference(start)))

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

    fun add(name: String, def: TokenType): Rule {
        val rule = Rule(name, listOf(TerminalReference(def)))
        this@GrammarDsl.rules.add(rule)
        return rule
    }

    fun add(name: String, def: String): Rule {
        val rule = Rule(name, listOf(RuleReference(def)))
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
}

fun grammar(name: String, start: String, f: GrammarDsl.() -> Unit): GrammarDsl {
    val grammar = GrammarDsl(name, start)
    grammar.f()
    return grammar
}
