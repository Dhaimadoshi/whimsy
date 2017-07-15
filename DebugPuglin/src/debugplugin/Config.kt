package debugplugin

import norswap.autumn.Grammar
import norswap.autumn.debugger.DebugNode
import norswap.autumn.debugger.DebugAction
import norswap.autumn.debugger.debug
import norswap.autumn.naive.Parser
import norswap.lang.java8.GraphGrammar
import norswap.utils.read_file

data class Config(var grammar: Grammar, val path: String) {
    init {
        grammar.DEBUG = true
//        grammar.debug_root = DebugNode(grammar)
    }

    val file = read_file(path)
    var breakpoints: ArrayList<String> = arrayListOf()
    val grammarName = grammar.javaClass.kotlin.simpleName?: ""
//    val view = DebugView()

    fun parse(): Boolean {
        return grammar.parse(file)
    }

    val debug: Boolean
        get() = grammar.debug()

    fun debug(allow_prefix: Boolean, parser: Parser): Boolean =
            grammar.debug(allow_prefix, parser)

    fun reset() {
        DebugAction.clear()
        grammar.reset()

    }
//
//    fun setBreakpoint() {
//        breakpoints.addAll(
//                arrayOf("root",
//                        "import_decls"))
//    }
//
//    // Reset breakpoints
//    fun refreshBreakpoint() {
//        breakpoints = arrayListOf()
//        setBreakpoint()
//    }

    fun setInput() {
        grammar.setInput(file)
    }

    companion object{
        fun init(): Config {
            //TODO: if config exist load it else create config file
            return Config(GraphGrammar(),
                    path = "/Users/dhai/Dropbox/thesis/test/src/test/SimpleWordCounter.java")
                    .apply { setInput() }
//                    .apply { setBreakpoint() }

        }
    }

    fun  replaceGrammar(g: GraphGrammar) {
        grammar = g
        setInput()
    }
}

//fun getConfig() = debugplugin.Config(GraphGrammar(), test_file = "/Users/dhai/Dropbox/thesis/debugplugin.test/src/debugplugin.test/SimpleWordCounter.java")
