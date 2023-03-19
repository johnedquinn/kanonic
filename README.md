# Kanonic

Kanonic is an SLR (Simple LR) parser operating on the JVM.

## Action Items
- [ ] Finalize Grammar
- [ ] Add Grammar (serialized) to ParserMetadata
- [ ] Add optionality, repeating groups, etc
- [ ] Add pre-build task to generate for some grammars into the test directory.
- [ ] Add Gradle task
- [ ] Make sure all tests pass. Clean up.
- [ ] Simplify project structure
- [ ] Make token definitions dynamic (might be done).

## Grammar

To illustrate the Kanonic syntax, see the following example:
```ion
// File: SimpleLang.knc
// Author: Kanonic
// Description: An example kanonic file

kanonic::{
  name: "SimpleLang",
  top_rule: atomic
}

/** TOKENS -- this is a comment **/

PLUS::"\+"
MINUS::"-"
IDENTIFIER::"[a-zA-Z]+"
INTEGER::"[0-9]+" // This is also a comment

/** RULES -- this is a comment **/

expr::[
  base::( expr PLUS atomic )
]

atomic::[
  ident::IDENTIFIER,
  int::INTEGER
]
```

### Parser Configuration

Each Kanonic file (`*.knc`) contains a configuration object named `kanonic`. See the below
allowable attributes:

```text
name: String | Symbol
top_rule: String | Symbol
```

### Tokens

Each token name has the syntax of "[A-Z][A-Z_]+". Its values are regular expressions in the form of Strings.

### Rules

Each top-level rule holds variants of that rule. For example:
```ion
atomic::[
  ident::IDENTIFIER,
  int::INTEGER
]
```

In the above example, `atomic` will generate, in Kotlin, a sealed class named `AtomicNode`. Its named variants will extend
`AtomicNode` and will be named by their annotation. In this example, the variants will be named `AtomicIdentNode` and
`AtomicIntNode`.

All rules and their variants should be named using camel-case.

## Generation

### LR(0) Automaton

After parsing the Kanonic file, the Kanonic tool creates an LR(0) automaton using the collected tokens and rules.
According to the "Introduction to Compilers and Language Design" by Douglas Thain:

> The LR(0) automaton is also variously known as the canonical collection or the compact finite state
> machine of the grammar.
>
> **Introduction to Compilers and Language Design**, *Douglas Thain*

To create the LR(0) automaton, the following approach is taken:

> The automaton is constructed as follows. State 0 is created by taking the production for the start symbol (P → E)
> and adding a dot at the begin- ning of the right hand side. This indicates that we expect to see a complete program,
> but have not yet consumed any symbols. This is known as the kernel of the state.
>
> Then, we compute the closure of the state as follows. For each item in the state with a non-terminal X immediately to
> the right of the dot, we add all rules in the grammar that have X as the left hand side. The newly added items have a
> dot at the beginning of the right hand side.
>
> The procedure continues until no new items can be added
>
> **Introduction to Compilers and Language Design**, *Douglas Thain*

### SLR Parse Table

Given the LR(0) automaton, the Kanonic tool creates an SLR Parse Table using the following algorithm:

> Push S0 onto S.
> Let a be the first input token.
>
> Loop:
>   - Let s be the top of the stack.
>   - If ACTION[s, a] is accept:
>     - Parse complete.
>   - Else if ACTION[s, a] is shift t:
>     - Push state t on the stack.
>     - Let a be the next input token.
>   - Else if ACTION[s, a] is reduce A → β:
>     - Pop states corresponding to β from the stack.
>     - Let t be the top of stack.
>     - Push GOTO[t, A] onto the stack.
>   - Otherwise:
>     - Halt with a parse error.
>
> **Introduction to Compilers and Language Design**, *Douglas Thain*

## Project Structure

- `kanonic-gen`
  - `KanonicGenerator`: creates all files given a grammar
  - `NodeGenerator`: generates the file containing all nodes
  - `ParserInfoGenerator`: generates file to hold lambdas for creating nodes
- `kanonic-runtime`
  - `AutomatonGenerator`: given a grammar, generates an LR(1) automaton
  - `TableGenerator`: given a grammar and automaton, generates the parse table
  - `ParserInternal`: given a table, grammar, and lambdas, parses the input string into an AST
