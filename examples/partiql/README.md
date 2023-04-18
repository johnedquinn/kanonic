# PartiQL Example

This example aims to use Kanonic to generate a parser for a subset of [PartiQL](www.partiql.org), an open-source query
language.

## Benchmarks

On top of parsing, this example aims to benchmark several other parser implementations:
1. The Kanonic generated parser using the Kanonic generated lexer
2. The Kanonic generated parser using the ANTLR generated lexer
3. The ANTLR generated parser using the ANTLR generated lexer
4. Kanonic generated parser and Kanonic generated lexer with a visitor to translate to the PartiQL AST
5. Kanonic generated parser and ANTLR generated lexer with a visitor to translate to the PartiQL AST
6. The PartiQL official Parser (uses ANTLR and visitor to translate to the PartiQL AST)

Below are some preliminary results:

### The PartiQL Official Parser (ANTLR Parser & ANTLR Lexer & Visitor)
```text
Benchmark                                                                                                                            (inputIndex)  Mode  Cnt    Score    Error  Units
PartiQLParserBenchmark.parseUsingPartiQLParser                                                                                    SELECT a FROM b  avgt   20   11.199 ±  0.382  us/op
PartiQLParserBenchmark.parseUsingPartiQLParser                                                                    SELECT (SELECT a FROM b) FROM b  avgt   20   19.511 ±  1.136  us/op
PartiQLParserBenchmark.parseUsingPartiQLParser                    SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20   42.930 ±  2.404  us/op
```

### Kanonic Parser & ANTLR Lexer & Visitor
```text
PartiQLParserBenchmark.parseUsingThisParserWithAntlrLexer                                                                         SELECT a FROM b  avgt   20    5.110 ±  0.121  us/op
PartiQLParserBenchmark.parseUsingThisParserWithAntlrLexer                                                         SELECT (SELECT a FROM b) FROM b  avgt   20    9.813 ±  0.375  us/op
PartiQLParserBenchmark.parseUsingThisParserWithAntlrLexer         SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20   23.732 ±  0.899  us/op
```

### Kanonic Parser & Kanonic Lexer & Visitor
```text
PartiQLParserBenchmark.parseUsingThisParser                                                                                       SELECT a FROM b  avgt   20   22.801 ±  1.526  us/op
PartiQLParserBenchmark.parseUsingThisParser                                                                       SELECT (SELECT a FROM b) FROM b  avgt   20   54.496 ±  6.664  us/op
PartiQLParserBenchmark.parseUsingThisParser                       SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20  143.957 ± 15.846  us/op
```

### Kanonic Parser & ANTLR Lexer
```text
PartiQLParserBenchmark.parseUsingThisKanonicParserWithAntlrLexer                                                                  SELECT a FROM b  avgt   20    4.640 ±  0.088  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParserWithAntlrLexer                                                  SELECT (SELECT a FROM b) FROM b  avgt   20    8.712 ±  0.164  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParserWithAntlrLexer  SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20   22.971 ±  1.836  us/op
```

### ANTLR Parser & ANTLR Lexer
```text
PartiQLParserBenchmark.parseUsingThisAntlrParser                                                                                  SELECT a FROM b  avgt   20    1.359 ±  0.052  us/op
PartiQLParserBenchmark.parseUsingThisAntlrParser                                                                  SELECT (SELECT a FROM b) FROM b  avgt   20    2.366 ±  0.067  us/op
PartiQLParserBenchmark.parseUsingThisAntlrParser                  SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20    5.277 ±  0.202  us/op
```

### Kanonic Parser & Kanonic Lexer
```text
PartiQLParserBenchmark.parseUsingThisKanonicParser                                                                                SELECT a FROM b  avgt   20   21.777 ±  1.407  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParser                                                                SELECT (SELECT a FROM b) FROM b  avgt   20   51.118 ±  1.625  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParser                SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20  134.060 ±  4.659  us/op
```

### ANTLR Lexer
```text
PartiQLParserBenchmark.tokenizeUsingThisAntlrLexer                                                                                SELECT a FROM b  avgt   20    0.546 ±  0.016  us/op
PartiQLParserBenchmark.tokenizeUsingThisAntlrLexer                                                                SELECT (SELECT a FROM b) FROM b  avgt   20    0.891 ±  0.020  us/op
PartiQLParserBenchmark.tokenizeUsingThisAntlrLexer                SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20    1.732 ±  0.065  us/op
```

### Kanonic Lexer
```text
PartiQLParserBenchmark.tokenizeUsingThisKanonicLexer                                                                              SELECT a FROM b  avgt   20   18.171 ±  0.557  us/op
PartiQLParserBenchmark.tokenizeUsingThisKanonicLexer                                                              SELECT (SELECT a FROM b) FROM b  avgt   20   42.057 ±  1.137  us/op
PartiQLParserBenchmark.tokenizeUsingThisKanonicLexer              SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20  109.069 ±  5.223  us/op
```

## Changing from Stack to List

```text
Benchmark                                                                                                                            (inputIndex)  Mode  Cnt   Score   Error  Units
PartiQLParserBenchmark.parseUsingThisAntlrParser                                                                                  SELECT a FROM b  avgt   20   1.292 ± 0.020  us/op
PartiQLParserBenchmark.parseUsingThisAntlrParser                                                                  SELECT (SELECT a FROM b) FROM b  avgt   20   2.340 ± 0.109  us/op
PartiQLParserBenchmark.parseUsingThisAntlrParser                  SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20   5.271 ± 0.114  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParserWithAntlrLexer                                                                  SELECT a FROM b  avgt   20   3.443 ± 0.089  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParserWithAntlrLexer                                                  SELECT (SELECT a FROM b) FROM b  avgt   20   6.796 ± 0.278  us/op
PartiQLParserBenchmark.parseUsingThisKanonicParserWithAntlrLexer  SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)  avgt   20  16.236 ± 0.706  us/op
```