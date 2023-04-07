package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder.Companion.buildGrammar
import io.johnedquinn.kanonic.runtime.grammar.RuleBuilder.Companion.buildGeneratedRule
import io.johnedquinn.kanonic.runtime.grammar.RuleBuilder.Companion.buildRule

public object KanonicGrammar {

    // TOKEN NAMES
    private const val IDENT_UPPER_CASE = "IDENT_UPPER_CASE"
    private const val IDENT_LOWER_CASE = "IDENT_CAMEL_CASE"
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
    private const val configDef = "config_def"
    private const val tokenDef = "token_def"
    private const val ruleDef = "rule_def"
    private const val ruleItem = "rule_item"
    private const val ruleVariant = "rule_variant"

    // GENERATED RULE NAMES
    private const val configDefs = "_0"
    private const val expressions = "_1"
    private const val ruleItems = "_2"
    private const val ruleVariants = "_3"

    public val grammar = buildGrammar("Kanonic", "file") {
        packageName("io.johnedquinn.kanonic.syntax.generated")
        tokens {
            IDENT_UPPER_CASE - "[A-Z][A-Z_]*"
            IDENT_LOWER_CASE - "[a-z][a-z_]*"
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
            "root" eq config - expressions
        }

        // config
        //   : IDENT_CAMEL_CASE ":" "{" configDef* "}" ";"
        //   ;
        config eq buildRule(this, config) {
            "config_struct" eq IDENT_LOWER_CASE - COLON - CURLY_BRACE_LEFT - configDefs - CURLY_BRACE_RIGHT - COLON_SEMI
        }

        // configDef
        //  : IDENT_CAMEL_CASE COLON IDENT_CAMEL_CASE COLON_SEMI
        //  ;
        configDef eq buildRule(this, configDef) {
            "config_definition" eq IDENT_LOWER_CASE - COLON - "text" - COLON_SEMI
        }

        "text" eq buildRule(this, "text") {
            "text" eq "text_hidden"
        }

        "text_hidden" eq buildGeneratedRule(this, "text_hidden") {
            "text_hidden_0" eq IDENT_LOWER_CASE
            "text_hidden_1" eq IDENT_UPPER_CASE
            "text_hidden_2" eq LITERAL_STRING
        }

        // token
        //  : IDENT_UPPER_CASE COLON LITERAL_STRING COLON_SEMI --> tokenDef
        //  ;
        tokenDef eq buildRule(this, tokenDef) {
            "token" eq IDENT_UPPER_CASE - COLON - LITERAL_STRING - COLON_SEMI
        }

        // rule
        //   : IDENT_CAMEL_CASE COLON variant (LINE_VERTICAL variant)* COLON_SEMI --> Rule
        //   ;
        ruleDef eq buildRule(this, ruleDef) {
            "rule" eq IDENT_LOWER_CASE - COLON - ruleVariants - COLON_SEMI
        }

        // variant
        //   : item+ "-->" IDENT_CAMEL_CASE --> Variant
        //   ;
        ruleVariant eq buildRule(this, ruleVariant) {
            "variant" eq ruleItems - "optional_alias"
        }

        // item
        //   : IDENT_CAMEL_CASE --> RuleReference
        //   | IDENT_UPPER_CASE --> TokenReference
        //   ;
        ruleItem eq buildRule(this, ruleItem) {
            "rule_reference" eq IDENT_LOWER_CASE
            "token_reference" eq IDENT_UPPER_CASE
            "line_reference" eq LINE_VERTICAL // TODO
        }

        //
        //
        // GENERATED
        //
        //

        "optional_alias" eq buildGeneratedRule(this, "optional_alias") {
            "optional_alias" eq DASH - DASH - CARROT_RIGHT - IDENT_LOWER_CASE
            "optional_alias" eq EPSILON
        }

        configDefs eq buildGeneratedRule(this, configDefs) {
            "EmptyConfigDefinition" eq EPSILON
            "MultipleConfigDefinitions" eq configDefs - configDef
        }

        // EXPRESSIONS
        expressions eq buildGeneratedRule(this, expressions) {
            "EmptyExpressions" eq EPSILON
            "TokenAdded" eq expressions - tokenDef
            "RuleAdded" eq expressions - ruleDef
        }

        // VARIANTS
        ruleVariants eq buildGeneratedRule(this, ruleVariants) {
            "SingleVariant" eq ruleVariant
            "MultipleVariants" eq ruleVariants - ruleVariant
        }

        // ITEMS
        ruleItems eq buildGeneratedRule(this, ruleItems) {
            "SingleRule" eq ruleItem
            "MultipleRules" eq ruleItems - ruleItem
        }
    }
}
