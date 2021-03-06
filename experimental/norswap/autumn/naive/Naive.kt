package norswap.autumn.naive
import norswap.autumn.TokenGrammar
import norswap.autumn.Parser as PlainParser

// -------------------------------------------------------------------------------------------------

/**
 * Placeholder for forward references to parsers.
 * After all parsers have been defined, the grammar will set the [parser] field according to [name].
 */
class ReferenceParser (val name: String): Parser()

// -------------------------------------------------------------------------------------------------

/**
 * Wraps a token parser from [TokenGrammar].
 */
class Token (val token: () -> Boolean): Parser()
{
    init { parser =  token }
}

// -------------------------------------------------------------------------------------------------

/**
 * Wraps a choice token parser from [TokenGrammar].
 */
class TokenChoice (g: TokenGrammar, vararg val parsers: Parser): Parser()
{
    init {
        grammar = g
        val token_parsers = parsers.map { (it as Token).token }.toTypedArray()
        parser =  g.token_choice(*token_parsers)
    }
}

// -------------------------------------------------------------------------------------------------

/**
 * A parser that can wrap a `() -> Boolean` function (i.e. a non-naive parser).
 *
 * Can be used to convert non-naive parser to naive parser when a better solution is not
 * available and we do not care about the parser's children.
 */
class Wrap (val f: () -> Boolean): Parser()
{
    init { parser =  f }
}

// -------------------------------------------------------------------------------------------------