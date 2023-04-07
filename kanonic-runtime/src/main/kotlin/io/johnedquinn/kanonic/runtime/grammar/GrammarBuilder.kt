package io.johnedquinn.kanonic.runtime.grammar

import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
import io.johnedquinn.kanonic.runtime.utils.Logger

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

    fun tokens(f: TokensBuilder.() -> Unit): GrammarBuilder {
        val l = TokensBuilder()
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
