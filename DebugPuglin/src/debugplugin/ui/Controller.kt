package debugplugin.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import javafx.collections.ObservableList
import norswap.autumn.Grammar
import norswap.autumn.model.STNode
import tornadofx.Controller
import tornadofx.observable
import kotlin.collections.ArrayList

/**
 * Created by dhai on 2017-07-09.
 *
 * Intermediate between the view and the data model
 */

class DebugManager: Controller() {
    val model = Model.instance
    val _config = Config.instance

    // -------------------------------------------------------------------------------------------------
    // Syntax sugar

    val grammar: Grammar get() = model.config.grammar
    val parse: Boolean get() = model.parse
    val STroot: STNode get() = model.syntax_tree.first()

    // -------------------------------------------------------------------------------------------------

    /*
    *   Create a list representation of the syntax tree
     */
    fun syntaxTreeToList(): ObservableList<STNode>
    {
        val list: ArrayList<STNode> = arrayListOf()

        STroot.navigateTree { list.add(it) }
        return list.observable()
    }

    fun loadSTTree(): STNode {
        STroot.clear_descendents()
        STroot.init_descendents()

        val toRemove = arrayListOf<STNode>()
        STroot.navigateTree {

            if(model.filter_nothing_matched.value && it.pos0 == it.pos)
                toRemove.add(it)

            if (model.filter_backtracked.value && it.backtracked)
                toRemove.add(it)
        }

        if (model.search_validity) {
            val condition = { it: STNode ->
                it.name.toLowerCase().contains(model.search_field.toLowerCase()) ||
                        it.type.toLowerCase().contains(model.search_field.toLowerCase())
            }

            filterTree(null, STroot, condition ).forEach { it() }

            return STroot
        }

        toRemove.forEach {
            it.parent!!.descendent.remove(it)
        }

        if(model.filter_named) {
            filterTree(null, STroot) { it.name != "" }.forEach { it() }
            STroot.navigateTree {
                it.descendent.sortBy { it.log_size }
            }
        }

        return STroot
    }

    /*
    *   Generate data for the views to display the syntax tree
     */
    fun loadSTTable(): List<STNode> {
        var st = syntaxTreeToList()
        // can be optimized

        if(model.filter_nothing_matched.value)
            st = remove_not_matched(st)!!

        if(model.filter_backtracked.value)
            st = remove_backtracked(st)!!

        if(model.search_validity) {
            val subst: ArrayList<STNode> =
                    st.filter {
                        it.name.toLowerCase().contains(model.search_field.toLowerCase()) ||
                        it.type.toLowerCase().contains(model.search_field.toLowerCase()) }
                            as ArrayList

            if (subst.isEmpty())
                set_search_validity(false)
            else
                return subst.observable().also { fire(ResetFilter) }
        }

        if(model.filter_named)
            st = (st.filter { it.name != "" } as ArrayList<STNode>).observable()

        return st
    }

    fun remove_not_matched(items: ObservableList<STNode>?): ObservableList<STNode>? =
            (items?.filter{ it.pos0 != it.pos } as ArrayList).observable()

    fun remove_backtracked(items: ObservableList<STNode>?): ObservableList<STNode>? =
            (items?.filter { !it.backtracked } as ArrayList).observable()

    fun filterTree(father: STNode?, node: STNode, condition: (STNode)->Boolean): ArrayList<()->Unit> {
        var list = arrayListOf<()->Unit>()

        if(condition(node) || node.name == "root")
        {
            if(father != null && father != node.parent)
                list.add({
                    father.descendent.add(node)
                })
            node.descendent.forEach { list.addAll(filterTree(node, it, condition)) }
        }
        else
        {
            if(father != null)
                list.add({
                    father.descendent.remove(node)
                })
            node.descendent.forEach {
                list.addAll(filterTree(father, it, condition))
            }

        }
        return list
    }

    fun getCode(rule: String): String {
        var app = ApplicationManager.getApplication()

        var code: String? = null

        app.runReadAction {
            val project = ProjectManager.getInstance().getOpenProjects()[0];
            val file = PsiShortNamesCache.getInstance(project)
                    .getClassesByName(Model.instance.config.grammarName, GlobalSearchScope.allScope(project))

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

    /*
    subscriptions to events
     */
    init {

        subscribe<SyntaxTableRequest> { fire(SyntaxTableEvent(loadSTTable())) }
        subscribe<SyntaxTreeRequest> { fire(SyntaxTreeEvent(loadSTTree())) }

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
    }
}