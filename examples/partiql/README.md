# PartiQL Example

This example aims to use Kanonic to generate a parser for a subset of [PartiQL](www.partiql.org), an open-source query
language. On top of parsing, this example aims to benchmark several other parser implementations:
1. The Kanonic generated parser using the Kanonic generated lexer
2. The Kanonic generated parser using the ANTLR generated lexer
3. The ANTLR generated parser using the ANTLR generated lexer
4. Kanonic generated parser and Kanonic generated lexer with a visitor to translate to the PartiQL AST
5. Kanonic generated parser and ANTLR generated lexer with a visitor to translate to the PartiQL AST
6. The PartiQL official Parser (uses ANTLR and visitor to translate to the PartiQL AST)
