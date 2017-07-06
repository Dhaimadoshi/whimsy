package norswap.autumn.naive
import norswap.autumn.Grammar
import norswap.autumn.debugger.Debug
import norswap.autumn.parsers.transact
import norswap.lang.java8.ast.File

abstract class Parser : () -> Boolean
{
    lateinit var grammar: Grammar
    lateinit var parser: ()-> Boolean
    var rule: String? = null

    override fun invoke(): Boolean {
        if(grammar.DEBUG)
            return Debug(this, grammar, parser)()
        else
            return parser()
    }
}