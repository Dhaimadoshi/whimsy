package norswap.autumn.model

import norswap.autumn.naive.Parser

/**
 * Created by dhai on 2017-06-24.
 */

class STNode(val parser: Parser, val log_size: Int, val pos0: Int, var pos: Int = 0) {
    val type = parser::class.simpleName?: ""
    val name = parser.rule

    private val children = arrayListOf<STNode>()

    var parent: STNode? = null
    var backtracked: Boolean = false

    var expanded: Boolean = false
    var id: Int = 0

    /*
    *   [descendent] is used to display the tree, this value is needed so that we can manipulate the tree
    *   without losing information
    */
    var descendent = arrayListOf<STNode>()
    fun init_descendents() {
        navigateTree {
            if(it.parent != null) {
                it.parent!!.descendent.add(it)
            }
        }
    }
    fun clear_descendents() {
        navigateTree { it.descendent.clear() }
    }

    fun addChild(node: STNode) {
        children.add(node)
        node.id = id + children.size
        node.parent = this
    }

    /*
    *   Navigate the tree and execute [effect] on every child
    */
    fun navigateTree(effect: (STNode)-> Unit) {
        effect(this)
        children.forEach {
            it.navigateTree(effect)
        }
    }

    override fun toString(): String {
        return toString(false, false)
    }

    fun toString(child: Boolean = false, descendents: Boolean = false,
                 parent: Boolean = false, full: Boolean = false): String {
        var str = "|- ${name.toUpperCase()}    -- $type"
        if(full)
            str +=  "--  log.size = $log_size \t pos0 = $pos0\t pos = $pos --  backtracked: $backtracked"
        if(child || descendents || full) {

            if (descendents) {
                str += "\n\t\t\t\t|-descendents : - "
                descendent.forEach { str += "\n\t\t\t\t ||=\t${it.name.toUpperCase()} -|- ${it.parser::class.simpleName}" }
            }
            else {
                str += "\n\t\t\t\t|-children : - "
                children.forEach { str += "\n\t\t\t\t ||=\t${it.name.toUpperCase()} -|- ${it.parser::class.simpleName}" }
            }
        }
        if (parent || full)
            str += "\n\t\t\t\t|-parent : - ${this.parent}"
        return str
    }

    companion object {
        val DUMMY: STNode = STNode(DUMMY(), 0, 0)
        val LINK: STNode
            get() = STNode(LINK(), 0, 0)
    }
}

class DUMMY(): Parser()
class LINK(): Parser() {
    init {
        rule = "ROOT"
    }
}
