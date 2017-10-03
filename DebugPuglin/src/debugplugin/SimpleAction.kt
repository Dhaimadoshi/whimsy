package debugplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.util.FileContentUtil
import debugplugin.ui.DebugApp
import debugplugin.ui.Model
import javafx.application.Application
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import norswap.autumn.debugger.DebugNode
import norswap.autumn.debugger.DebugAction
import norswap.autumn.model.STNode
import norswap.autumn.debugger.print_stack
import norswap.autumn.undoable.UndoList
import norswap.lang.java8.GraphGrammar
import tornadofx.App
import tornadofx.View
import tornadofx.plusAssign

class SimpleAction : AnAction() {
//    init { init_config() }
//    lateinit var config: Config

    //Parameter anActionEvent carries information on the invocation place and data available.
    override fun actionPerformed(anActionEvent: AnActionEvent?) {

        val project = ProjectManager.getInstance().getOpenProjects()[0];
        println(project.name)
        val file = FilenameIndex.getFilesByName(project, "GraphGrammar.kt", GlobalSearchScope.allScope(project))
//        LocalFileSystem.getInstance().findFileByIoFile()

//        PsiManager.getInstance(project).
        println(file.size)
        file.forEach { println(it) }
//        FileContentUtil.reparseFiles(file.get(0).virtualFile)
        PsiShortNamesCache.getInstance(project)
                .getClassesByName("GraphGrammar", GlobalSearchScope.allScope(project))
                .get(0).allFields.filter { it.name == "root" }.forEach { println(it.text) }


//        Model.instance.config.replaceGrammar(GraphGrammar())
//        println(file.get(0).virtualFile)
//        Application.launch(DebugApp::class.java)
//        init_config()
//
//        println("an action")
//        preparse_logic()
//        postpase_logic()
//
//        val result = config.parse
//
//        print_stack(config.grammar.syntax_tree as UndoList<Any?>)
//        out(result, "the file has been parsed ${if(result)"succesfully" else "unsucessfully"}")
    }

    fun ebauche_de_recompile() {
        val project = ProjectManager.getInstance().getOpenProjects()[0];
        println(project.name)
        val file = FilenameIndex.getFilesByName(project, "GraphGrammar.kt", GlobalSearchScope.allScope(project))
//        LocalFileSystem.getInstance().findFileByIoFile()

//        PsiManager.getInstance(project).
        println(file.size)
        file.forEach { println(it) }
        FileContentUtil.reparseFiles(file.get(0).virtualFile)


        Model.instance.config.replaceGrammar(GraphGrammar())
    }

//    fun init_config() { config = Config.init() }
//
//    fun preparse_logic() =
//            DebugAction.pre_parse.add { if (is_breakpoint(it)) debug_window(it.node) }
//
//    fun postpase_logic() {}
//
//    fun out(result: Boolean, msg: String = "") { Messages.showDialog(result.toString() +
//            " ${msg}", "Parse success:", arrayOf("OK"), -1, null) }
//
//    fun is_breakpoint(debug: DebugNode) =
//        config.breakpoints.contains(debug.parser_node.rule) ||
//        config.breakpoints.contains(debug.parser_node::class.simpleName)
//
//    /*
//    TODO:
//        Open a window with several buttons: step into, step over, resume, backstep
//        step into : look at next parser
//        step over : look at next rule
//        resume : continue until meet another breakpoints
//        backstep : undo last rule
//     */
//    fun debug_window(node: STNode) {
//        Messages.showDialog("\n${node.toString()}",
//                "Breakpoint: Placeholder for debug logic", arrayOf("OK"), -1, null)
//    }
}