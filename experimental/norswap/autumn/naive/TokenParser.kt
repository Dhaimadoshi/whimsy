package norswap.autumn.naive
import norswap.autumn.Grammar
import norswap.autumn.TokenGrammar

abstract class TokenParser
{
    lateinit var grammar: Grammar
    abstract operator fun invoke(): TokenGrammar.TokenParser
}
