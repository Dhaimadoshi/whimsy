package norswap.autumn.naive

import norswap.autumn.Grammar
import norswap.autumn.parsers.ahead
import norswap.autumn.parsers.ahead_pure
import norswap.autumn.parsers.not


// -------------------------------------------------------------------------------------------------

/**
 * Succeeds if [p] succeeds, but does not advance the input position (all other side effects of
 * [p] are retained).
 */
class Ahead (g: Grammar, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.ahead(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Succeeds if [p] succeeds, but does produce any side effect (does not even change the input
 * position).
 */
class AheadPure (g: Grammar, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.ahead_pure(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Succeeds only if [p] fails.
 */
class NotAhead(g: Grammar, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.not(p)
}

// -------------------------------------------------------------------------------------------------
