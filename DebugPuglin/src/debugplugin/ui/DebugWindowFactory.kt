package debugplugin.ui

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import debugplugin.ui.Views.MasterView
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import tornadofx.FX
import tornadofx.View
import tornadofx.action

class DebugWindowFactory : ToolWindowFactory {
    lateinit var debug_window: ToolWindow

    fun DebugWindowFactory() {
        // add listeners here
        /*
        example
            hideToolWindowButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                myToolWindow.hide(null);
              }
            });
         */



//        button.action {
//            ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                }
//            }
//        }
    }

    // Create the tool window content.
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        debug_window = toolWindow
        val fxPanel = JFXPanel()
//        button = Button()
//        val component = toolWindow.component

        Platform.setImplicitExit(false)
        Platform.runLater { initFX(fxPanel) }

        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(fxPanel, "", false)

        toolWindow.contentManager.addContent(content)
    }

    private fun initFX(fxPanel: JFXPanel) {
        fxPanel.scene = get_scene(FX.find(MasterView::class.java))
    }

    fun get_scene(view: View): Scene {
        val pane = //VBox()
                BorderPane()
//        val menuBar = MenuBar()
//        val menuFile = javafx.scene.control.Menu("file")
//        menuBar.getMenus().addAll(menuFile)
//
//        button.action {
//
//            Model.instance.simple_action
//                    .actionPerformed(AnActionEvent.createFromAnAction(
//                            Model.instance.simple_action,
//                    null,
//                    ActionPlaces.UNKNOWN,
//                    DataManager.getInstance().getDataContext())
//            )
//        }
//        pane.top = button
        pane.center = view.root
        return Scene(pane, 300.0, 250.0)
    }
}