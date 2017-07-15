package debugplugin.ui

import com.intellij.openapi.ui.Messages
import debugplugin.Config
import debugplugin.SimpleAction
import javafx.beans.property.SimpleBooleanProperty
import norswap.autumn.Grammar
import norswap.autumn.debugger.DebugNode
import norswap.autumn.debugger.DebugAction
import norswap.autumn.model.STNode
import norswap.autumn.naive.Parser
import tornadofx.FX

//val model = Model()

class Model() {
    val config = Config.init()
    val parse: Boolean
        get() = parse(config.grammar.root!!)

    val undone_node: ArrayList<STNode> = arrayListOf()
    val simple_action = SimpleAction()
    val filter_nothing_matched = SimpleBooleanProperty()

    var search_field: String = ""
    var search_validity: Boolean = false

    var  filter_named: Boolean = false

    fun parse(parser: Parser): Boolean {
        config.reset()
        config.setInput()

        preparse_logic()
        postparse_logic()

        val result = config.debug

        return result
//        print_stack(config.grammar.syntax_tree as UndoList<Any?>)
  //      out(result, "the file has been parsed ${if (result) "succesfully" else "unsucessfully"}")
    }

    fun preparse_logic() {
        DebugAction.pre_parse.add {
            if (is_breakpoint(it)) FX.find(DebugManager::class.java).fire(BreakpointEvent) }
    }

        fun postparse_logic() {}

        fun out(result: Boolean, msg: String = "") {}

        fun is_breakpoint(debug: DebugNode) =
                config.breakpoints.contains(debug.parser_node.rule) ||
                        config.breakpoints.contains(debug.parser_node::class.simpleName)

    companion object {
        val instance = Model()
    }
}

/*
TODO:
    Open a window with several buttons: step into, step over, resume, backstep
    step into : look at next parser
    step over : look at next rule
    resume : continue until meet another breakpoints
    backstep : undo last rule
 */
fun debug_window(node: STNode) {
    Messages.showDialog("\n${node.toString()}",
            "Breakpoint: Placeholder for debug logic", arrayOf("OK"), -1, null)
}