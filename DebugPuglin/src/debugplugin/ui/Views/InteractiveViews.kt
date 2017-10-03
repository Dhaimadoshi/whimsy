package debugplugin.ui.Views

import debugplugin.ui.Model
import debugplugin.ui.*
import javafx.scene.control.ToggleGroup
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
            fire(SyntaxTableRequest)
            fire(SyntaxTreeRequest)
            tooltip("Parse the file and display resulting syntax tree")
        }}
        checkbox("Filter no match", Model.instance.filter_nothing_matched) {
            action {
                fire(SyntaxTableRequest)
                fire(SyntaxTreeRequest)
            }
        }
        checkbox("Filter backtracked", Model.instance.filter_backtracked) {
            action {
                fire(SyntaxTableRequest)
                fire(SyntaxTreeRequest)
            }
        }

        vbox {
            val toggle_all = radiobutton("All nodes", toggleGroup) {
                selectedProperty().addListener { event->
                    controller.set_search_validity(false)
                    fire(SetFilterEvent(false))
                }
                action {
                    fire(SyntaxTableRequest)
                    fire(SyntaxTreeRequest)
                }
            }
            val toggle_named = radiobutton("Named rule", toggleGroup) {
                selectedProperty().addListener { event->
                    controller.set_search_validity(false)
                    fire(SetFilterEvent(true))
                }
                action {
                    fire(SyntaxTableRequest)
                    fire(SyntaxTreeRequest)
                }
            }
            val toggle_filtered = radiobutton("Filtered", toggleGroup) {
                selectedProperty().addListener { event->
                    controller.set_search_validity(true)
                    fire(SetFilterEvent(false))
                }
                action { fire(SyntaxTableRequest) }
            }
            toggleGroup.selectToggle(toggle_all)

            subscribe<ResetFilter> {
                toggleGroup.selectToggle(toggle_filtered)
//                fire(SetFilterEvent(false))
            }
        }
    }
}

class BreakpointSearchField: View() {
    val controller = find(DebugManager::class)

    override val root = textfield("Search") {
        textProperty().addListener { obs, old, new ->
            controller.set_search_field(new)
            controller.set_search_validity(true)
            fire(SyntaxTableRequest)
            fire(SyntaxTreeRequest)
        }
    }
}
