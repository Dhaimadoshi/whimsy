package debugplugin.ui.Views

import debugplugin.ui.Model
import debugplugin.ui.*
import javafx.scene.control.TabPane
import javafx.scene.paint.Color
import tornadofx.*

class DebugApp(): App(MasterView::class)

class MasterView: View() {
    val controller: DebugManager by inject()

    override val root = drawer {
        item("STree", expanded = true) {
            borderpane {
                top = find(BreakpointSearchField::class).root

                left = find(Buttons::class).root

                center =  tabpane { tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                    tab("Screen 1", find(STTableView::class).root)
                    tab("Screen 2", find(STTreeView::class).root)
                }
            }
        }

        item("Rule") {
            if (controller.model.PLUGIN)
            {
                borderpane {
                    top = text {
                        //                    isEditable = false
                        subscribe<RuleEvent> { event ->
                            text = event.code + "\n" +
                                    "---------------------------------------------------------------------------------" +
                                    "\n"
                        }
                    }
                    center = find(MatchedInputField::class).root
    //                        textarea("matched input") {
    //                    isEditable = false
    //                    subscribe<MatchedInputEvent> { event ->
    //                        text = event.input
    //                    }
    //                }
    //                bottom = textarea("Log") { isEditable = false }
                }
            }
            else label("This view is disactivated in app mode")
        }

    }
}

class MatchedInputField: View() {
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