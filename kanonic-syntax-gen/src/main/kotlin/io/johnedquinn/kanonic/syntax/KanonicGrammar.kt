package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder.Companion.buildGrammar
import io.johnedquinn.kanonic.runtime.grammar.RuleBuilder.Companion.buildGeneratedRule
import io.johnedquinn.kanonic.runtime.grammar.RuleBuilder.Companion.buildRule

internal object KanonicGrammar {

    // TOKEN NAMES
    private const val IDENT_UPPER_CASE = "IDENT_UPPER_CASE"
    private const val IDENT_LOWER_CASE = "IDENT_LOWER_CASE"
    private const val EPSILON = "EPSILON"
    private const val COLON = "COLON"
    private const val COLON_SEMI = "COLON_SEMI"
    private const val BRACE_LEFT = "BRACE_LEFT"
    private const val BRACE_RIGHT = "BRACE_RIGHT"
    private const val LINE_VERTICAL = "LINE_VERTICAL"
    private const val LITERAL_STRING = "LITERAL_STRING"
    private const val DASH = "DASH"
    private const val CARROT_RIGHT = "CARROT_RIGHT"
    private const val COMMENT_SINGLE = "COMMENT_SINGLE"
    private const val COMMENT_BLOCK = "COMMENT_BLOCK"
    private const val PAREN_LEFT = "PAREN_LEFT"
    private const val PAREN_RIGHT = "PAREN_RIGHT"
    private const val QUESTION_MARK = "QUESTION_MARK"
    private const val ASTERISK = "ASTERISK"
    private const val PLUS = "PLUS"

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
    private const val tokenOrRule = "_1"
    private const val ruleItems = "_2"
    private const val ruleVariants = "_3"

    public val grammar = buildGrammar("Kanonic", "file") {
        packageName("io.johnedquinn.kanonic.syntax.generated")
        tokens {
            // Constants
            ASTERISK - "\\*"
            BRACE_LEFT - "\\{"
            BRACE_RIGHT - "\\}"
            CARROT_RIGHT - ">"
            COLON - ":"
            COLON_SEMI - ";"
            DASH - "-"
            LINE_VERTICAL - "\\|"
            PAREN_LEFT - "\\("
            PAREN_RIGHT - "\\)"
            PLUS - "\\+"
            QUESTION_MARK - "\\?"

            // Identifiers
            IDENT_UPPER_CASE - "[A-Z][A-Z_]*"
            IDENT_LOWER_CASE - "[a-z][a-z_]*"
            LITERAL_STRING - "\\\"([^\\\"\\\\]|\\\\.)*\\\""
            COMMENT_SINGLE - ("//[^\\r\\n]*?\\r?\\n?" channel "hidden")
            COMMENT_BLOCK - ("/\\*(?s).*?\\*/" channel "hidden")
        }

        // file
        // : config ( token | rule )+ EOF
        // ;
        file eq buildRule(this, file) {
            "root" eq config - "tokensOrRules"
        }

        // config
        // : IDENT_CAMEL_CASE COLON BRACE_LEFT configDef* BRACE_RIGHT COLON_SEMI
        // ;
        config eq buildRule(this, config) {
            "config" eq IDENT_LOWER_CASE - COLON - BRACE_LEFT - configDefs - BRACE_RIGHT - COLON_SEMI
        }

        // config_def
        // : IDENT_CAMEL_CASE COLON text COLON_SEMI
        // ;
        configDef eq buildRule(this, configDef) {
            "config_definition" eq IDENT_LOWER_CASE - COLON - "text" - COLON_SEMI
        }

        // text
        // : (IDENT_CAMEL_CASE | IDENT_LOWER_CASE | LITERAL_STRING)
        // ;
        "text" eq buildRule(this, "text") {
            "text" eq "text_hidden"
        }

        "text_hidden" eq buildGeneratedRule(this, "text_hidden") {
            "text_hidden_0" eq IDENT_LOWER_CASE
            "text_hidden_1" eq IDENT_UPPER_CASE
            "text_hidden_2" eq LITERAL_STRING
        }

        // token
        // : IDENT_UPPER_CASE COLON LITERAL_STRING (DASH DASH CARROT_RIGHT IDENT_LOWER_CASE)? COLON_SEMI
        // ;
        tokenDef eq buildRule(this, tokenDef) {
            "token_def" eq IDENT_UPPER_CASE - COLON - LITERAL_STRING - "optional_channel" - COLON_SEMI
        }

        +generateRule("optional_channel") {
            "optional_channel_0" eq EPSILON
            "optional_channel_1" eq DASH - DASH - CARROT_RIGHT - IDENT_LOWER_CASE
        }

        // rule
        // : IDENT_CAMEL_CASE COLON variant (LINE_VERTICAL variant)* COLON_SEMI
        // ;
        ruleDef eq buildRule(this, ruleDef) {
            ruleDef eq IDENT_LOWER_CASE - COLON - ruleVariant - ruleVariants - COLON_SEMI
        }

        +buildRule("alternative") {
            "alternative" eq LINE_VERTICAL - ruleVariant
        }

        // GENERATED
        // ruleVariants : (LINE_VERTICAL variant)*
        +generateRule(name = ruleVariants) {
            "SingleVariant" eq EPSILON
            "MultipleVariants" eq ruleVariants - "alternative"
        }

        // variant
        // : item+ (DASH DASH CARROT_RIGHT IDENT_CAMEL_CASE)?
        // ;
        ruleVariant eq buildRule(this, ruleVariant) {
            "variant" eq ruleItems - "optional_alias"
        }

        // GENERATED
        // ruleItems: ruleItem+
        ruleItems eq buildGeneratedRule(this, ruleItems) {
            "SingleRule" eq ruleItem
            "MultipleRules" eq ruleItems - ruleItem
        }

        // GENERATED
        // optional_alias : (DASH DASH CARROT_RIGHT IDENT_CAMEL_CASE)?
        "optional_alias" eq buildGeneratedRule(this, "optional_alias") {
            "optional_alias" eq DASH - DASH - CARROT_RIGHT - IDENT_LOWER_CASE
            "optional_alias" eq EPSILON
        }

        // item
        // : PAREN_LEFT item+ ( LINE_VERTICAL item+ )* PAREN_RIGHT    --> item_group
        // | IDENT_CAMEL_CASE                                         --> item_rule
        // | IDENT_UPPER_CASE                                         --> item_token
        // | item item_flag                                           --> item_flagged
        // ;
        ruleItem eq buildRule(this, ruleItem) {
            "item_group" eq PAREN_LEFT - ruleItems - "appended_items" - PAREN_RIGHT
            "item_rule" eq IDENT_LOWER_CASE
            "item_token" eq IDENT_UPPER_CASE
            "item_flagged" eq ruleItem - "item_flag"
        }

        // item_flag
        // : ( QUESTION_MARK | ASTERISK | PLUS )
        // ;
        +buildRule("item_flag") {
            "item_flag_asterisk" eq ASTERISK
            "item_flag_question" eq QUESTION_MARK
            "item_flag_plus" eq PLUS
        }

        // GENERATED
        // appendedItems: (LINE_VERTICAL item+)*
        +generateRule("appended_items") {
            "appended_items_1" eq EPSILON
            "appended_items_0" eq "appended_items" - "appended_items_unit"
        }

        // GENERATED
        // appendedItems: (LINE_VERTICAL item+)
        +generateRule("appended_items_unit") {
            "appended_items_0" eq LINE_VERTICAL - ruleItems
        }

        //
        //
        // GENERATED
        //
        //

        configDefs eq buildGeneratedRule(this, configDefs) {
            "EmptyConfigDefinition" eq EPSILON
            "MultipleConfigDefinitions" eq configDefs - configDef
        }

        +generateRule(name = "tokensOrRules") {
            "tokensOrRules_01" eq tokenOrRule
            "tokensOrRules_02" eq "tokensOrRules" - tokenOrRule
        }

        // EXPRESSIONS
        tokenOrRule eq buildGeneratedRule(this, tokenOrRule) {
            "TokenAdded" eq tokenDef
            "RuleAdded" eq ruleDef
        }
    }
}
