package io.johnedquinn.kanonic.dsl

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.Rule
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.SymbolReference
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

    fun add(rule: Rule) = this.apply {
        this.rules.add(rule)
    }

    infix fun String.eq(other: Rule): Rule {
        this@GrammarBuilder.rules.add(other)
        return other
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
}
