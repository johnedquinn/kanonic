package io.johnedquinn.kanonic.runtime.grammar

import io.johnedquinn.kanonic.runtime.parse.TokenLiteral
import io.johnedquinn.kanonic.runtime.utils.KanonicLogger

class GrammarBuilder(var name: String, var start: String) {
    public val rules = mutableListOf<Rule>()
    public var tokens: MutableList<TokenDefinition> = mutableListOf()
    private var packageName: String? = null

    private val logger = KanonicLogger.getLogger()

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
        logger.fine("RULES: $rules")
        return Grammar(rules, Grammar.Options(name, RuleReference(start), packageName), tokens)
    }

    fun add(rule: Rule) = this.apply {
        this.rules.add(rule)
    }

    operator fun Rule.unaryPlus() = this.apply {
        rules.add(this)
    }

    fun buildRule(name: String, f: RuleBuilder.() -> Unit) = RuleBuilder.buildRule(this, name, f)

    fun generateRule(name: String, alias: String? = null, f: RuleBuilder.() -> Unit) = RuleBuilder.buildGeneratedRule(this, name, alias, f)

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
        logger.fine("TOKENS: $tokens")
        return this
    }

    fun packageName(name: String) {
        this.packageName = name
    }
}
