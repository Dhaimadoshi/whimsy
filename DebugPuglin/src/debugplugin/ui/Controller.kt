package debugplugin.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import javafx.collections.ObservableList
import norswap.autumn.Grammar
import norswap.autumn.debugger.incremental_undo
import norswap.autumn.model.STNode
import norswap.autumn.naive.Parser
import tornadofx.Controller
import tornadofx.observable

/**
 * Created by dhai on 2017-07-09.
 */

class DebugManager: Controller() {
    val model = Model.instance
    val syntax_tree_to_list =
            model.config.grammar.syntax_tree.observable()
    val grammar: Grammar
        get() = model.config.grammar

    val parse: Boolean
        get() = model.parse

    val undone_node = model.undone_node

    fun parse(parser: Parser) = model.parse(parser)

    fun loadST(): List<STNode> {
        var st = syntax_tree_to_list
        val search_field = model.search_field

        if(model.filter_nothing_matched.value)
            st = remove_not_matched(st)!!

        if(model.search_validity) {
            val subst: ArrayList<STNode> =
                    st.filter {
                        it.name.toLowerCase().contains(search_field.toLowerCase()) ||
                        it.type.toLowerCase().contains(search_field.toLowerCase()) }
                            as ArrayList

            if (subst.isEmpty())
                fire(NoSuchRule).also { set_search_validity(false) }
            else
                return subst.observable().also { fire(ResetFilter) }
        }

        if(model.filter_named)
            st = (st.filter { it.name != "" } as ArrayList<STNode>).observable()

        return st
    }

    fun getCode(rule: String): String {
        var app = ApplicationManager.getApplication()

        var code: String? = null
        app.runReadAction {
            val project = ProjectManager.getInstance().getOpenProjects()[0];
            val file = PsiShortNamesCache.getInstance(project)
                    .getClassesByName(Model.instance.config.grammarName!!, GlobalSearchScope.allScope(project))

            if (file.isNotEmpty()) {
                val rule = file[0].allFields.filter { it.name == rule }
                if (rule.isNotEmpty())
                    code = rule[0].text
            }
            else
            {
                code = "wrong name for grammar, please make sure that the name of the grammar is the same as the file"
            }
        }
        return code ?: "This is not a valid rule name"
    }

    inline fun set_search_field(str: String) { model.search_field = str }
    inline fun set_search_validity(validity: Boolean) {model.search_validity = validity}

    fun remove_not_matched(items: ObservableList<STNode>?): ObservableList<STNode>? =
            (items?.filter{ it.pos0 != it.pos } as ArrayList).observable()

    init {

        subscribe<SyntaxTreeRequest> { fire(SyntaxTreeEvent(loadST())) }

        subscribe<UndoEvent> { event->
            val node = event.node
//
            var node0 = grammar.incremental_undo().also { undone_node.add(it) }
            while (node0 !== node) {
                node0 = grammar.incremental_undo()
                undone_node.add(node0)
            }
//            grammar.syntax_tree.forEach { if (it.pos > grammar.pos ) it.pos = grammar.pos }
            fire(SyntaxTreeRequest)
        }

        subscribe<RuleRequest> { event ->
            val code = getCode(event.rule)
            fire(RuleEvent(code))
        }

        subscribe<MatchedInputRequest> { event ->
            val grammar = Model.instance.config.grammar
            val matched_input =
                    grammar.text.substring(event.node.pos0, event.node.pos)
            fire(MatchedInputEvent(matched_input))
        }

        subscribe<SetFilterEvent> { event-> model.filter_named = event.filter }

        subscribe<RedoEvent> {
            if (undone_node.isEmpty())
                println("nothing to redo")
            else {
                val node = undone_node.last()
                undone_node.remove(node)
                println("redo $node")
//                grammar.pos = node.pos
//
//                while(node.side_effects.isNotEmpty())
//                    node.side_effects.pop().side_effect(grammar)
//
//                fire(SyntaxTreeRequest)
            }
        }
    }
}