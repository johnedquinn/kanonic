# Kanonic

*This project is a work-in-progress and should be considered experimental.*

Kanonic is a fast LR(1) parser generator operating on the JVM.

While Java is understood to be the language of the software industry, there hasn't been an adequate parser generator
operating on the JVM that accomplishes one of the main pre-requisites for companies: speed. This project aims to tackle
this problem by leveraging the speed and expressiveness of LR(1) grammars.

## Grammar

To illustrate the Kanonic syntax, see the following example:
```knc
// File: SimpleLang.knc
// Author: Kanonic Team
// Description: An example kanonic file

/** CONFIG -- this is a comment **/

kanonic: {
  name: "SimpleLang";
  root: expr;
  package: "io.johnedquinn.simple";
};

/** TOKENS -- this is a comment **/

PLUS: "\+";
MINUS: "-";
IDENTIFIER: "[a-zA-Z]+";
INTEGER: "[0-9]+"; // This is also a comment

/** RULES -- this is a comment **/

expr
  : expr PLUS atomic --> exprPlus
  | expr MINUS atomic --> exprMinus
  ;

atomic
  : IDENTIFIER --> ident
  | INTEGER --> int
  ;
```

### Parser Configuration

Each Kanonic file (`*.knc`) contains a configuration object named `kanonic`. See the below
allowable attributes:

- `name`: the name of the language
- `root`: the root rule to use when parsing
- `package`: the package where the generated code will be placed

### Tokens

Each token name has the syntax of "[A-Z][A-Z_]+". Its values are regular expressions in the form of strings.

### Rules

Each top-level rule holds named variants of that rule. For example:
```knc
atomic
  : IDENTIFIER --> ident
  | INTEGER --> int
  ;
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

### LR(0) to LR(1)

During generation, the states of the machine are merged to create the LR(1) automaton.

### LR(1) Parse Table

Given the LR(1) automaton, the Kanonic tool creates an LR(1) Parse Table using the following algorithm:

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

## Project Structure & Flow

For most users, the only project relevant to them will be `kanonic-tool`.
- `kanonic-tool`: generates a parser for a user's input Kanonic file
  - Internally parses an input Kanonic file into a Kanonic AST using `:kanonic-syntax`'s parser
  - Converts the Kanonic AST into a Grammar
  - Generates the appropriate files using the parsed Grammar and `:kanonic-gen`

For developers, below are descriptions for the remaining sub-projects.
- `kanonic-gen`
  - `KanonicGenerator`: given a grammar, generates the Nodes, Visitors, and Metadata
- `kanonic-runtime`
  - `KanonicParser`: given a generated metadata, parses the input string into the generated nodes (AST)
- `kanonic-syntax`: holds the official, generated Kanonic Parser/Metadata
  - Internally calls `kanonic-syntax-gen`'s application to generate the source code and place within the Kotlin source set
- `kanonic-syntax-gen`: generates Kanonic's parser and places the source in `kanonic-syntax`
  - Holds Kanonic's official grammar in Kotlin form
  - Very similar to `kanonic-tool`, but it solely used for internal purposes
