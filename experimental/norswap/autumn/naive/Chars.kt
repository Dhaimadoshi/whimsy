package norswap.autumn.naive

import norswap.autumn.parsers.*
import norswap.autumn.Grammar

// -------------------------------------------------------------------------------------------------
/*

This file contains parsers that match at the character level.

 */
// -------------------------------------------------------------------------------------------------

/**
 * Matches any character that satisfied [pred].
 */
class CharPred (g: Grammar, val pred: (Char) -> Boolean): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.char_pred(pred)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches any character.
 * Only fails when the end of the input (represented by the null byte) is reached.
 */
class CharAny(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.char_any()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches any character in the range between [start] and [end].
 */
class CharRng(g: Grammar, val start: Char, val end: Char): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.char_range(start, end)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches any of the characters in [chars].
 */
class CharSet (g: Grammar, val chars: String): Parser()
{
    init { grammar = g }
    constructor (g: Grammar, vararg c: Char): this(g, String(c))
    override fun invoke() = grammar.char_set(chars)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches [str].
 */
class Str (g: Grammar, val str: String): Parser()
{

    init { grammar = g }
    override fun invoke(): Boolean
    {
        dosomestuff()
        return grammar.string(str)
    }

    fun dosomestuff() = println("I'm hooked and I do some cool stuff")
}
// -------------------------------------------------------------------------------------------------

/**
 * Matches [str], and any trailing whitespace (as defined by [Grammar.whitespace]).
 */
class WordString (g: Grammar, val str: String): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.word(str)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches the same thing as [p], and any trailing whitespace (as defined by [Grammar.whitespace]).
 */
class WordParser (g: Grammar, val p: Parser): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.word(p)
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches an alphabetic character (the ranges a-z and A-Z).
 */
class Alpha(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.alpha()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches an alphanumeric character (the ranges a-z, A-Z and 0-9).
 */
class Alphanum(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.alphanum()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches a digit (the range 0-9).
 */
class Digit(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.digit()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches an hexadecimal digit (the ranges a-f, A-F and 0-9).
 */
class HexDigit(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.hex_digit()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches an octal digit (the range 0-7).
 */
class OctalDigit(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.octal_digit()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches a whitespace character, as defined by [Char.isWhitespace].
 */
class SpaceChar(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.space_char()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches a java identifier (as defined by JLS 3.8).
 */
class JavaIden(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.java_iden()
}

// -------------------------------------------------------------------------------------------------

/**
 * Matches a java identifier that consists (as defined by JLS 3.8) that consists only of
 * ASCII characters.
 */
class AsciiJavaIden(g: Grammar): Parser()
{
    init { grammar = g }
    override fun invoke() = grammar.ascii_java_iden()
}

// -------------------------------------------------------------------------------------------------
