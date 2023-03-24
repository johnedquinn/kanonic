package io.johnedquinn.kanonic.gen.impl

import io.johnedquinn.kanonic.dsl.grammar

// TODO: Make internal once grammar is serialized
public object KanonicGrammar {
    public val grammar = grammar("Kanonic", "file") {
        packageName("io.johnedquinn.kanonic.generated")
        tokens {
            "IDENT_TOKEN" - "[A-Z][A-Z_]*"
            "IDENT_CAMEL_CASE" - "[a-z][a-zA-Z]*"
            "COLON" - ":"
            "COLON_SEMI" - ";"
            "LINE_VERTICAL" - "|"
            "LITERAL_STRING" - "\".*\""
            "DOLLAR_SIGN" - "\".*\""
        }
        "file" eq "expressions" alias "Root"
        "expressions" eq "EPSILON" alias "EmptyExpressions"
        "expressions" eq "expressions" - "expr" alias "Expressions"
        "expr" eq "IDENT_TOKEN" - "COLON" - "COLON" alias "TokenDef"
    }.toGrammar()
}
