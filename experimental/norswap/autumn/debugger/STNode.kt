package norswap.autumn.debugger

import norswap.autumn.naive.Parser

/**
 * Created by dhai on 2017-06-24.
 */


data class STNode(val parser: Parser, val pos0: Int, var pos: Int = 0) {
    val rule: String?
        get() = parser.rule

    override fun toString() =
        if (rule != null)
            "\n|+ RULE == $rule == -- matched from ${parser.grammar.input.range_string(pos0, pos)}"
            else
            "\t|+ ${parser::class.simpleName.toString()} == from: ${parser.grammar.input.range_string(pos0, pos)}"
}