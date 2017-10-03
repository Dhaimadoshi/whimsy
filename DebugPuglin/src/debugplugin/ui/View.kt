package debugplugin.ui

import com.intellij.openapi.actionSystem.DefaultActionGroup
import javafx.scene.Parent
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import norswap.autumn.model.STNode
import norswap.autumn.naive.Parser
import tornadofx.*

class DebugApp(): App(MasterView::class)

class MasterView: View() {
    val controller: DebugManager by inject()
//    val model = controller.rule

//    var debug_tree = controller.syntax_tree_to_list
    override val root =
        drawer {
        item("Main", expanded = true) {
            borderpane {
                top = find(BreakpointSearchField::class).root
//                        hbox {
//                    find(Menu::class).root
//
//                }

                left = find(Buttons::class).root

                center = find(STTable::class).root
            }
        }
        item("Text") {
            borderpane {
                top = text {
//                    isEditable = false
                    subscribe<RuleEvent> { event ->
                        text = event.code + "\n" +
                                "---------------------------------------------------------------------------------" +
                                "\n"
                    }
                }
                center = find(MatchedInputTable::class).root
//                        textarea("matched input") {
//                    isEditable = false
//                    subscribe<MatchedInputEvent> { event ->
//                        text = event.input
//                    }
//                }
//                bottom = textarea("Log") { isEditable = false }
            }
        }
    }

    override fun onDock() {}

    init {
    }

    init {
//        subscribe<BreakpointEvent> { openInternalWindow(RuleDebugView::class) }
    }
}

class Menu: View() {
    override val root = menubar {
        menu("File") {
            menu("Connect") {
                item("Facebook")
                item("Twitter")
            }
            item("Save")
            item("Quit")
        }
        menu("Edit") {
            item("Copy")
            item("Paste")
        }
    }
}
class STTable: View() {
    val controller = find(DebugManager::class)

    override val root = tableview<STNode> {
        //            isEditable = true
        column("rule", STNode::name)
        column("type", STNode::type).remainingWidth()
        column("log size", STNode::log_size)
        column("pos0", STNode::pos0)
        column("pos", STNode::pos)
        contextmenu {
            item("Undo up to this rule").action {
                if(selectedItem != null)
                    fire(UndoEvent(selectedItem!!))
            }
//            item("Display matched input").action {
//                selectedItem?.apply { println("Displaying matched input for $name") }
//            }
        }
//                    selectionModel.selectionMode = SelectionMode.MULTIPLE
        columnResizePolicy = SmartResize.POLICY

        subscribe<SyntaxTreeEvent> { event ->
            items.setAll(event.syntax_tree) }

        subscribe<RemoveNotMatched> {
            items.setAll(controller.remove_not_matched(items))
        }

        onUserSelect {
            fire(RuleRequest(it.name))
            fire(MatchedInputRequest(it))
        }

        rowExpander(expandOnDoubleClick = false) {
            tableview(it.children.observable()) {
                column("Name", Parser::rule)
                column("Type", Parser::type)
                onUserSelect {println("selected $it")}
            }
        }
    }
}

class MatchedInputTable: View() {
    override val root = textflow {
        val f1 = text {
        }
        val f2 = text {
            fill = Color.RED
        }
        val f3 = text {

        }
        subscribe<MatchedInputRequest> { event ->
            val input = Model.instance.config.grammar.input
            val txt = Model.instance.config.grammar.text

            val frame = 5
            var min = input.line_from(event.node.pos0)
            if (min < frame)
                min = 0 else min = input.offset_from(min - frame)

            var max = input.line_from(event.node.pos)
            if (max + frame > input.line_from(txt.length-1))
                max = txt.length-1 else max = input.offset_from(max + frame)

            f1.text = txt.substring(min, event.node.pos0)
            f2.text = txt.substring(event.node.pos0, event.node.pos)
            f3.text = txt.substring(event.node.pos, max)
        }
    }
}

// start a new window or replace temporarily the view to set and display breakpoints
class BreakPointView(override val root: Parent) : View() {
}

class test: DefaultActionGroup() {

}