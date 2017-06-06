package norswap.autumn.naive
import norswap.autumn.Grammar
import norswap.autumn.Parser
import norswap.autumn.parsers.*
import norswap.autumn.parsers.Longest     as Longest0
import norswap.autumn.parsers.LongestPure as LongestPure0
import norswap.utils.cast

// -------------------------------------------------------------------------------------------------
/*

This file contains parsers that perform a choice between their sub-parsers.

*/
// -------------------------------------------------------------------------------------------------

/**
 * Matches the same things as the first parser in the list that matches, or fails if none succeeds.
 */
class Choice (g: Grammar, val ps: Parser): NaiveParser()
{
    init { grammar = g }
    override fun invoke(): Boolean
    {
        return grammar.choice { ps() }
    }
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches the same thing as the parser in [ps] that matches the most input.
 *
 * Side effects are retained only for the parser that is selected.
 */
class Longest (g: Grammar, vararg val ps: Parser): NaiveParser()
{
    init { grammar = g }
    val longest = Longest0(g, ps.cast())
    override fun invoke() = longest()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches the same thing as the parser in [ps] that matches the most input.
 *
 * Side effects are retained only for the parser that is selected.
 */
class LongestPure (g: Grammar, vararg val ps: Parser): NaiveParser()
{
    init { grammar = g }
    val longest_pure = LongestPure0(g, ps.cast())
    override fun invoke() = longest_pure()
}

// -------------------------------------------------------------------------------------------------