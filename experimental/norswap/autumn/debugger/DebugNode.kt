package norswap.autumn.debugger

import com.intellij.util.containers.Stack
import norswap.autumn.*
import norswap.autumn.model.STNode
import norswap.autumn.naive.Parser

class DebugNode(val grammar: Grammar, val parser_node: Parser)
{
    var called = false
    var callable = false
    val node = STNode(parser_node, grammar.log.size, grammar.pos)
            .apply {
//                DebugAction.syntax_tree.push(this) }
                grammar.syntax_tree.push(this) }

    operator fun invoke(): Boolean {
        // push sur stack etat + info parser
        // a posteriori je peux check back
        val result = parser_node.parser()
        node.pos = grammar.pos
        return result
    }

    constructor(g: Grammar): this(g, g.root!!)

    fun parse_debug() {

    }

    fun debug() {
        DebugAction.pre_parse(this)     // logique de debugger avant exec du parser

        val continuation: ()-> Boolean = {
//            if (!called) { parser(); called = true }
            val result = parser_node()
            DebugAction.post_parse(this)    // logique de debuger apres exec du parser
            result
        }

        if ( callable ) continuation()
        else DebugAction.pendingCall = continuation
    }
}

object DebugAction {
    var pre_parse: ArrayList<(debug: DebugNode)-> Unit> = arrayListOf()
    var post_parse: ArrayList<(debug: DebugNode)-> Unit> = arrayListOf()
    lateinit var pendingCall: ()->Boolean

    fun pre_parse(debug: DebugNode) { pre_parse.forEach { it(debug) } }
    fun post_parse(debug: DebugNode) { post_parse.forEach { it(debug) } }

    fun clear() {
        pre_parse.clear()
        post_parse.clear()
    }
}

/*
    precondition : input is already init
    Parse the file without reseting the tree in case of fail
 */
fun Grammar.debug (allow_prefix: Boolean, parser: Parser): Boolean
{
    var result =
            try { parser() }
            catch (e: Throwable) {
                fail_force(pos, UncaughtException(e))
                false
            }

    val partial_match = result && pos != input.size

    if (partial_match && !allow_prefix) {
        // If we matched up to pos < length, and no errors are available at the reached position
        // or beyond; use "partial match" as the failure.

        if (fail_pos < pos || fail_pos == -1)
            fail_force(pos, PartialMatch)

        result = false
    }

    if (result) {
        fail_pos = -1
        failure = null
    }
    else
    {
        // in case the failure wasn't set (bad!)
        if (failure == null)
            if (fail_pos >= 0)
                failure = UnspecifiedFailureAt(input.string(fail_pos))
            else
                failure = UnspecifiedFailure

        // in case the failure position wasn't set (bad!)
        if (fail_pos < 0) fail_pos = 0

        // in case the root wasn't transactional (bad!)
        // or the failure results from a partial match
        // undo(0, 0)
    }

    return result
}

fun Grammar.debug(): Boolean = debug(false, root!!)

fun Grammar.incremental_undo(): STNode {
    val node = syntax_tree.peek()
    pos = node.pos

    while (log.size > node.log_size) {
        val side_effect = log.removeAt(log.lastIndex)
        node.side_effects.push(side_effect)
        side_effect.undo(this)
    }
    return node
}