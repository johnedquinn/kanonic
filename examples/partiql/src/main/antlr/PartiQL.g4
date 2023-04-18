grammar PartiQL;

options {
    tokenVocab=PartiQLTokens;
    caseInsensitive = true;
}

statement
    : dql # StatementDql
    ;

dql
    : expr # DqlExpr
    ;

expr
    : expr_select # ExprSfw
    ;

expr_select
    : SELECT expr FROM expr # ExprSelect
    | expr_atom # ExprSelectFallback
    ;

expr_atom
    : SYMBOL # ExprIdent
    | PAREN_LEFT expr PAREN_RIGHT # ExprWrapped
    ;