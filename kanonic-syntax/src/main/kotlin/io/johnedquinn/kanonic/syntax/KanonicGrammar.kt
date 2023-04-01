package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.dsl.GrammarBuilder.Companion.buildGrammar

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
        packageName("io.johnedquinn.kanonic.runtime.generated")
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
        }

        // TOP RULE
        file eq config - expressions alias "Root"

        // CONFIG
        config eq IDENT_CAMEL_CASE - COLON - CURLY_BRACE_LEFT - configDefs - CURLY_BRACE_RIGHT - COLON_SEMI alias "ConfigStruct"

        configDefs eq EPSILON alias "EmptyConfigDefinition"
        configDefs eq configDefs - configDef alias "MultipleConfigDefinitions"
        configDef eq IDENT_CAMEL_CASE - COLON - IDENT_CAMEL_CASE - COLON_SEMI alias "ConfigDefinition"

        // EXPRESSIONS
        expressions eq EPSILON alias "EmptyExpressions"
        expressions eq expressions - tokenDef alias "TokenAdded"
        expressions eq expressions - ruleDef alias "RuleAdded"

        // TOKENS
        tokenDef eq IDENT_UPPER_CASE - COLON - LITERAL_STRING - COLON_SEMI alias "Token"

        // RULE
        ruleDef eq IDENT_CAMEL_CASE - COLON - ruleVariants - COLON_SEMI alias "Rule"

        // VARIANTS
        ruleVariants eq ruleVariant alias "SingleVariant"
        ruleVariants eq ruleVariants - ruleVariant alias "MultipleVariants"

        // VARIANT
        ruleVariant eq ruleItems - DASH - DASH - CARROT_RIGHT - IDENT_CAMEL_CASE alias "Variant"

        // ITEMS
        ruleItems eq ruleItem alias "SingleRule"
        ruleItems eq ruleItems - ruleItem alias "MultipleRules"

        // ITEM
        ruleItem eq IDENT_CAMEL_CASE alias "RuleReference"
        ruleItem eq IDENT_UPPER_CASE alias "TokenReference"
        ruleItem eq LINE_VERTICAL alias "LineReference" // TODO
    }
}
