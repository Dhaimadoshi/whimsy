package norswap.autumn.naive
import norswap.autumn.Grammar
import norswap.autumn.parsers.*

// -------------------------------------------------------------------------------------------------

/**
 * Matches all the parsers in a sequence.
 */
class Seq (g: Grammar, val ps: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.seq { ps() }
}

//class Seq (g: Grammar, val ps: List<Boolean>): Parser()
//{
//    init { grammar = g }
//    override fun invoke() = grammar.seq { ps.all({it}) }
//}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [p] if it suceeds, otherwise succeeds without consuming any input.
 */
class Opt (g: Grammar, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.opt(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 0 or more (sequential) repetition of [p].
 */
class Repeat0 (g: Grammar, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.repeat0(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 1 or more (sequential) repetition of [p].
 */
class Repeat1 (g: Grammar, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.repeat1(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches exactly [n] (sequential) repetitions of [p].
 */
class Repeat (g: Grammar, val n: Int, val p: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.repeat(n, p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 0 or more repetitions of [around], separated from one another by input matching [inside].
 */
class Around0 (g: Grammar, val around: ()-> Boolean, val inside: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.around0(around, inside)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 1 or more repetitions of [around], separated from one another by input matching [inside].
 */
class Around1 (g: Grammar, val around: ()-> Boolean, val inside: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.around1(around, inside)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 0 or more repetitions of [around], separated from one another by input matching [inside],
 * optionally followed by input matching [inside].
 */
class ListTerm0 (g: Grammar, val around: ()-> Boolean, val inside: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.list_term0(around, inside)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 1 or more repetitions of [around], separated from one another by input matching [inside],
 * optionally followed by input matching [inside].
 */
class ListTerm1 (g: Grammar, val around: ()-> Boolean, val inside: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.list_term1(around, inside)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 0 or more repetition of [repeat], followed by [terminator].
 *
 * In case of ambiguity, [terminator] is matched in preference to [repeat]
 * (this is what makes this different from `seq { repeat0(repeat) && terminator() }`).
 */
class Until0 (g: Grammar, val repeat: ()-> Boolean, val terminator: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.until0(repeat, terminator)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches 1 or more repetition of [repeat], followed by [terminator].
 *
 * In case of ambiguity, [terminator] is matched in preference to [repeat]
 * (this is what makes this different from `seq { repeat1(repeat) && terminator() }`).
 */
class Until1 (g: Grammar, val repeat: ()-> Boolean, val terminator: ()-> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.until1(repeat, terminator)
}

// -------------------------------------------------------------------------------------------------