package norswap.autumn.model

import norswap.autumn.AppliedSideEffect
import norswap.autumn.Grammar
import norswap.autumn.naive.Parser
import java.util.*

/**
 * Created by dhai on 2017-06-24.
 */

class STNode(val parser: Parser, val log_size: Int, val pos0: Int, var pos: Int = 0) {
    val type = parser::class.simpleName?: ""
    val name = parser.rule?: ""
    val children = parser.children()

    var side_effects: Stack<AppliedSideEffect> = Stack()

    override fun toString(): String {
        var str = "|- ${name.toUpperCase()} \t-- $type --  --  log.size = $log_size \t pos0 = $pos0\t pos = $pos"
        if (children.size != 0) str += "\n\t\t\t\t|-children : - "
        children.forEach { str += "\n\t\t\t\t ||=\t${it.rule?.toUpperCase() ?: ""} -|- ${it::class.simpleName}" }
        return str
    }

    fun toString(child: Boolean = false): String {
        var str = "|- ${name.toUpperCase()} \t-- $type --  log.size = $log_size \t pos0 = $pos0\t pos = $pos"
        if(child) {
            if (children.size != 0) str += "\n\t\t\t\t|-children : - "
            children.forEach { str += "\n\t\t\t\t ||=\t${it.rule?.toUpperCase() ?: ""} -|- ${it::class.simpleName}" }
        }
        return "$str"
    }
    companion object {
        val DUMMY = STNode(DUMMY(), 0, 0)
    }
}

fun Grammar.buildST() {
    try{ STNode(root!!, 0, 0).apply { syntax_tree.push(this); buildST(root!!) } }
    catch(e: Exception) { println("Grammar.root not initialized") }
}

fun Grammar.buildST(p: Parser) {
    // TODO: change Graphgrammar so the names are extracted reflectively to get rid of the rule field in parser
    p.children().forEach {
        STNode(it, 0, 0).apply { syntax_tree.push(this) }
        buildST(it)
    }
}

class DUMMY(): Parser()