# Autumn Documentation

Autumn is a [Kotlin] parser combinator library written in with an unmatched feature set:

- Bundles pre-defined parsers and combinators for most common use cases
- Write your own parsers with regular Kotlin/Java code
- Scannerless, but with tokenization support
- Associativity & precedence support for operators
- Left-recursion support
- Context-sensitive parsing **!!**
- Pluggable error-reporting mechanism
- Reasonably fast (3x slower than ANTLR)
- Thoroughly documented
- Small & clean codebase

[Kotlin]: https://kotlinlang.org/

The latest version of this document is available online at  
https://github.com/norswap/whimsy/tree/master/doc/autumn/README.md

## Table of Contents

### [Why use Autumn?](faq/why.md)

### Guide

1. [Your First Grammar](guide/1-first-grammar.md)
1. [Transactionality](guide/2-transactionality.md)
1. [Using Basic Parsers](guide/3-basic-parsers.md)
1. [Writing Your Own Parsers](guide/4-own-parsers.md)
1. [Handling Left-Recursion](guide/5-left-recursion.md)
1. [Building an AST](guide/6-ast.md)
1. [Handling Side-Effects](guide/7-side-effects.md)

### Advanced

- [Optimizing Your Grammars](advanced/optimize.md)
- [Testing Your Grammars](advanced/test.md)
- [Debugging Your Grammars](advanced/debug.md)

### API

- [Autumn API Index](API/README.md)
- [Bundled Parsers Reference](API/parsers/README.md)

### Internals

- [Developer Guide](dev-guide.md)

### FAQ

- [Why use Autumn?](faq/why.md)
- [Hand-Written Parsers vs Parsing Tools](faq/hand-vs-tool.md)
- [What is the relationship between Autumn and PEGs?](faq/autumn-peg.md)
- [Is Autumn scannerless?](faq/scannerless.md)
- [Can I feed tokens to Autumn instead of text?](faq/feed-tokens.md)
- [Why is the DSL syntax so ugly/verbose?](faq/why-ugly.md)
- [Why write Autumn in Kotlin?](faq/kotlin.md)
- [Is Autumn compatible with Java?](faq/java-compat.md)
- [Is Autumn compatible with Kotlin's Javascript backend?](faq/js-compat.md)
- [Why are items in sequences / choices separated by `||` / `&&` ?](faq/seq-choice-syntax.md)

### Misc Notes

- [Publications about Autumn](publications/README.md)
- [List of Interesting Parsing Tools](notes/parsing-tools.md)
- [Parsing Expression Grammars](notes/peg.md)
- [List of Standard PEG Operations](notes/peg-ops.md)