package norswap.autumn.debugger

import norswap.autumn.*
import norswap.autumn.model.STNode
import norswap.autumn.naive.Parser
import norswap.lang.java8.GraphGrammar
import org.testng.internal.Graph

// todo : transform en fun
fun debugNode(grammar: Grammar, parser_node: Parser): Boolean
{
    val node = STNode(parser_node, grammar.log.size, grammar.pos)

    // syntax tree will be empty only when parsing root
    if(parser_node.rule != "root")
        try {
            grammar.syntax_tree.peek().also {
                it.addChild(node)
            }
        }
        catch (e: Exception) {
            println("Syntax tree is not supposed to be empty, \nrule requesting :\n$node")
        }

    grammar.syntax_tree.push(node)

    val result = parser_node.parser()

    if (!result) node.backtracked = true

    if(grammar.syntax_tree.size > 1)
        grammar.syntax_tree.pop()

    node.pos = grammar.pos
    return result
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
//(this as GraphGrammar).dummy)//