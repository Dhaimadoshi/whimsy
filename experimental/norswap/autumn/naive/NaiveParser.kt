package norswap.autumn.naive
import norswap.autumn.Grammar

abstract class NaiveParser : () -> Boolean
{
    lateinit var grammar: Grammar
}