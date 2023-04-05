package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.dsl.GrammarBuilder.Companion.buildGrammar
import io.johnedquinn.kanonic.dsl.RuleBuilder.Companion.buildGeneratedRule
import io.johnedquinn.kanonic.dsl.RuleBuilder.Companion.buildRule

// TODO: Make internal once grammar is serialized
public object KanonicGrammar {

    // TOKEN NAMES
    private const val IDENT_UPPER_CASE = "IDENT_UPPER_CASE"
    private const val IDENT_CAMEL_CASE = "IDENT_CAMEL_CASE"
    private const val EPSILON = "EPSILON"
    private const val COLON = "COLON"
    private const val COLON_SEMI = "COLON_SEMI"
    private const val CURLY_BRACE_LEFT = "CURLY_BRACE_LEFT"
    private const val CURLY_BRACE_RIGHT = "CURLY_BRACE_RIGHT"
    private const val LINE_VERTICAL = "LINE_VERTICAL"
    private const val LITERAL_STRING = "LITERAL_STRING"
    private const val DASH = "DASH"
    private const val CARROT_RIGHT = "CARROT_RIGHT"
    private const val COMMENT_SINGLE = "COMMENT_SINGLE"
    private const val COMMENT_BLOCK = "COMMENT_BLOCK"

    // RULE NAMES
    private const val file = "file"
    private const val config = "config"
    private const val configDef = "configDef"
    private const val configDefs = "configDefs"
    private const val tokenDef = "tokenDef"
    private const val ruleDef = "ruleDef"
    private const val expressions = "expressions"
    private const val ruleItems = "ruleItems"
    private const val ruleItem = "ruleItem"
    private const val ruleVariant = "ruleVariant"
    private const val ruleVariants = "ruleVariants"

    public val grammar = buildGrammar("Kanonic", "file") {
        packageName("io.johnedquinn.kanonic.syntax.generated")
        tokens {
            IDENT_UPPER_CASE - "[A-Z][A-Z_]*"
            IDENT_CAMEL_CASE - "[a-z][a-zA-Z]*"
            COLON - ":"
            COLON_SEMI - ";"
            CURLY_BRACE_LEFT - "\\{"
            CURLY_BRACE_RIGHT - "\\}"
            LINE_VERTICAL - "\\|"
            LITERAL_STRING - "\"((\\\")|[^\"])*\""
            DASH - "-"
            CARROT_RIGHT - ">"
            COMMENT_SINGLE - ("//[^\\r\\n]*?\\r?\\n?" channel "hidden")
            COMMENT_BLOCK - ("/\\*(?s).*?\\*/" channel "hidden")
        }

        // TOP RULE
        file eq buildRule(this, file) {
            "Root" eq config - expressions
        }

        // CONFIG
        config eq buildRule(this, config) {
            "ConfigStruct" eq IDENT_CAMEL_CASE - COLON - CURLY_BRACE_LEFT - configDefs - CURLY_BRACE_RIGHT - COLON_SEMI
        }

        configDefs eq buildGeneratedRule(this, configDefs) {
            "EmptyConfigDefinition" eq EPSILON
            "MultipleConfigDefinitions" eq configDefs - configDef
        }

        configDef eq buildRule(this, configDef) {
            "ConfigDefinition" eq IDENT_CAMEL_CASE - COLON - IDENT_CAMEL_CASE - COLON_SEMI
        }

        // EXPRESSIONS
        expressions eq buildGeneratedRule(this, expressions) {
            "EmptyExpressions" eq EPSILON
            "TokenAdded" eq expressions - tokenDef
            "RuleAdded" eq expressions - ruleDef
        }

        // TOKENS
        tokenDef eq buildRule(this, tokenDef) {
            "Token" eq IDENT_UPPER_CASE - COLON - LITERAL_STRING - COLON_SEMI
        }

        // RULE
        ruleDef eq buildRule(this, ruleDef) {
            "Rule" eq IDENT_CAMEL_CASE - COLON - ruleVariants - COLON_SEMI
        }

        // VARIANTS
        ruleVariants eq buildGeneratedRule(this, ruleVariants) {
            "SingleVariant" eq ruleVariant
            "MultipleVariants" eq ruleVariants - ruleVariant
        }

        // VARIANT
        ruleVariant eq buildRule(this, ruleVariant) {
            "Variant" eq ruleItems - DASH - DASH - CARROT_RIGHT - IDENT_CAMEL_CASE
        }

        // ITEMS
        ruleItems eq buildGeneratedRule(this, ruleItems) {
            "SingleRule" eq ruleItem
            "MultipleRules" eq ruleItems - ruleItem
        }

        // ITEM
        ruleItem eq buildRule(this, ruleItem) {
            "RuleReference" eq IDENT_CAMEL_CASE
            "TokenReference" eq IDENT_UPPER_CASE
            "LineReference" eq LINE_VERTICAL // TODO
        }
    }
}
