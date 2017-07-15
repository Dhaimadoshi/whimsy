package debugplugin.ui

import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import tornadofx.*

/**
 * Created by dhai on 2017-07-11.
 */

class Buttons: View() {
    val controller : DebugManager by inject()

    private val toggleGroup = ToggleGroup()


    override val root = vbox {
        button("debug") { action {
            controller.parse
             fire(SyntaxTreeRequest)
            tooltip("Parse the file and display resulting syntax tree")
        }}
        button("redo") { action {
            fire(RedoEvent)
        }}
        button("reset grammar") { action {
            controller.model.config.grammar.reset()
            fire(SyntaxTreeRequest)
            tooltip("Reset the grammar and dump previous matching")
        }}
        checkbox("Filter no match", Model.instance.filter_nothing_matched) {
            action {
                if(isSelected) fire(RemoveNotMatched)
                else fire(SyntaxTreeRequest)
            }
        }

        vbox {
            val toggle_all = radiobutton("All nodes", toggleGroup) {
                selectedProperty().addListener { event->
                    controller.set_search_validity(false)
                    fire(SetFilterEvent(false))
                }
                action { fire(SyntaxTreeRequest) }
            }
            val toggle_named = radiobutton("Named rule", toggleGroup) {
                selectedProperty().addListener { event->
                    controller.set_search_validity(false)
                    fire(SetFilterEvent(true))
                }
                action { fire(SyntaxTreeRequest) }
            }
            val toggle_filtered = radiobutton("Filtered", toggleGroup) {
                selectedProperty().addListener { event->
                    controller.set_search_validity(true)
                    fire(SetFilterEvent(false))
                }
                action { fire(SyntaxTreeRequest) }
            }
            toggleGroup.selectToggle(toggle_all)

            subscribe<ResetFilter> {
                toggleGroup.selectToggle(toggle_filtered)
//                fire(SetFilterEvent(false))
            }
        }
        button("simple action") { action {
//            val project = ProjectManager.getInstance().getOpenProjects()[0];
//            val file = FilenameIndex.getFilesByName(project, "GraphGrammar.kt", GlobalSearchScope.allScope(project))
//            println(project.name)
//            println(file)
        } }

    }
}

class BreakpointSearchField: View() {
    val controller = find(DebugManager::class)

    override val root = textfield("Input something") {
//        onKeyPressed.apply { println(this) }.equals(KeyEvent.KEY_PRESSED.name)
        textProperty().addListener { obs, old, new ->
            println("You typed: " + new)

//            setOnKeyPressed { event ->
//                if(event.code.equals(KeyCode.ENTER))
                    controller.set_search_field(new)
                    controller.set_search_validity(true)
                    fire(SyntaxTreeRequest)
//            }

//            subscribe<NoSuchRule> {
//                text = "There is no rule that matched that name"
//            }
        }
    }
}
