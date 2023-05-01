# PartiQL Example

This example aims to use Kanonic to generate a parser for a subset of [PartiQL](www.partiql.org), an open-source query
language.

## Benchmarks

While the implemented subset of PartiQL is quite small, this project has added support for
simple SFW queries, literal arrays, identifiers, and parenthesized expressions. To test the performance of Kanonic's
generated parser, we've written an equivalent grammar, using ANTLR to use for benchmarking analysis.

**Note**: The Kanonic Lexer is quite slow, and therefore, as both the ANTLR and Kanonic Grammars were equivalent, this benchmark
actually uses a combination of the ANTLR Lexer and Kanonic Parser to compare the parse time only. In order to do this,
the Lexer needed to be overriden to transform ANTLR Tokens into equivalent Kanonic tokens. We will include the time difference in a subsequent table.

For 5 different PartiQL queries, see the result below:

|Query Number|ANTLR Parse Time | Kanonic Parse Time |Query|
|------------|-------------------|---------------------|----|
|0| 1.435 | 1.985 |SELECT a FROM b|
|1|4.876|4.380|SELECT (SELECT a FROM b) FROM b AS x|
|2|5.631|8.200|SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)|
|3|18.384|10.835|SELECT (SELECT a FROM (SELECT a FROM b AS d) AS c) FROM (SELECT (SELECT a FROM b AS g) FROM c AS f) AS e|
|4|18.099|19.389|[a, b, [c, d, [e, (f)]], (SELECT a FROM (SELECT (SELECT [a, b, (c)] FROM d AS e) FROM f AS k) AS x)]|
|5|30.521|47.918|((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((a))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))|

Here are the benchmarked lex times:

|Query Number|ANTLR Lex Time|Kanonic Lex Time|
|------------|--------------|----------------|
|0|0.436|0.629|
|1|0.953|1.343|
|2|1.738|2.792|
|3|2.598|3.678|
|4|2.942|4.547|
|5|7.105|13.237|

## Conclusion

The parsing benchmarks showed that ANTLR performed better than Kanonic in 4 out of the 6 tests for a subset of the PartiQL
language. However, we can see that the difference in some queries can potentially be attributed to speedier lexing times
(see query #4 in the parsing and lexing benchmarks).
