package norswap.autumn.naive

import norswap.autumn.Grammar
import norswap.autumn.Parser
import norswap.autumn.parsers.*

// -------------------------------------------------------------------------------------------------
/*

This file contains parser combinators that act on [Grammar.stack].

 */
// -------------------------------------------------------------------------------------------------

/**
 * Matches [syntax], then call [effect], passing it an array containing everything pushed on the
 * stack since the parser's invocation, to which [backlog] items of backlog have been prepended.
 * All these items are removed from the stack.
 *
 * Insufficient items to satisfy the backlog requirement will the cause the parser to fail with
 * an execption.
 */
class Affect (g: Grammar,
              val backlog: Int = 0,
              val syntax: Parser,
              val effect: Grammar.(Array<Any?>) -> Unit)
              : NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.affect(backlog, syntax, effect)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [syntax], then calls [effect], passing it a string containing the matched text.
 */
class AffectStr (g: Grammar,
                 val syntax: Parser,
                 val effect: Grammar.(String) -> Unit)
                 : NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.affect_str(syntax, effect)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [syntax], then calls [effect], passing it an array containing everything pushed on the
 * stack since the parser's invocation, to which [backlog] items of backlog have been prepended.
 * All these items are removed from the stack. The return value of [effect] is itself pushed on the
 * stack.
 *
 * Insufficient items to satisfy the backlog requirement will the cause the parser to fail with
 * an execption.
 */
class Build (g: Grammar,
             val backlog: Int = 0,
             val syntax: Parser,
             val effect: Grammar.(Array<Any?>) -> Any)
             : NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.build(backlog, syntax, effect)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [syntax], then calls [value], passing it a string containing the matched text.
 * The return value of [value] is pushed on the stack.
 */
class BuildStr (g: Grammar,
                val syntax: Parser,
                val value: Grammar.(String) -> Any = {it})
                : NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.build_str(syntax, value)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [p] or, if [p] fails, pushes `null` on the stack.
 * Always succeeds.
 */
class Maybe (g: Grammar, val p: Parser): NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.maybe(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Attempts to match [p], then pushes `true` on the stack if successful, `false` otherwise.
 * Also discards its stack frame.
 * Always suceeds.
 */
class AsBool (g: Grammar, val p: Parser): NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.as_bool(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [p] then pushes [value] on the stack if successful.
 */
class AsVal (g: Grammar, val value: Any?, val p: Parser): NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.as_val(value, p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches all characters until [terminator] (also matched).
 *
 * All characters matched in this manner (excluding [terminator]) are collected in a string
 * which is pushed on the value stack.
 */
class Gobble (g: Grammar, val terminator: Parser): NaiveParser()
{
    init { grammar = g }
    override fun invoke() = grammar.gobble(terminator)
}

// -------------------------------------------------------------------------------------------------