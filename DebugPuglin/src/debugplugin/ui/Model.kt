package debugplugin.ui

import debugplugin.SimpleAction
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import norswap.autumn.Grammar
import norswap.autumn.debugger.debug
import norswap.autumn.model.STNode
import norswap.autumn.naive.Parser
import norswap.lang.java8.GraphGrammar
import norswap.utils.read_file
import tornadofx.observable

class Model() {
    val config = Config.instance
    val syntax_tree: ObservableList<STNode>
        get() = config.grammar.syntax_tree.observable()

    val undone_node: ArrayList<STNode> = arrayListOf()
    val simple_action = SimpleAction()

    val filter_nothing_matched = SimpleBooleanProperty()
    val filter_backtracked = SimpleBooleanProperty()

    var search_field: String = ""
    var search_validity: Boolean = false

    var filter_named: Boolean = false
    var PLUGIN: Boolean = true

    val parse: Boolean get() = parse()
    fun parse(): Boolean {
        config.reset()
        config.setInput()

        preparse_logic()
        postparse_logic()

        val result = config.debug

        return result
    }

    fun preparse_logic() {
//        DebugAction.pre_parse.add {}
    }

    fun postparse_logic() {}

    //TODO: have a display for parsing result
//    fun out(result: Boolean, msg: String = "") {}

    companion object {
        val instance = Model()
    }
}

data class Config(var grammar: Grammar, val path: String) {
    init {
        grammar.DEBUG = true
    }

    val file = read_file(path)
    val grammarName = grammar.javaClass.kotlin.simpleName?: ""

    fun parse(): Boolean {
        return grammar.parse(file)
    }

    val debug: Boolean
        get() = grammar.debug()

    fun debug(allow_prefix: Boolean, parser: Parser): Boolean =
            grammar.debug(allow_prefix, parser)

    fun reset() {
//        DebugAction.clear()
        grammar.reset()
    }

    fun setInput() {
        grammar.setInput(file)
    }


    companion object{
        val instance = init()

        private fun init(): Config {
            //TODO: if config exist load it else create config file
            return Config(GraphGrammar(),
                    path = "/Users/dhai/Dropbox/thesis/test/src/test/SimpleWordCounter.java")
                    .apply { setInput() }
        }
    }

    fun replaceGrammar(g: GraphGrammar) {
        grammar = g
        setInput()
    }
}