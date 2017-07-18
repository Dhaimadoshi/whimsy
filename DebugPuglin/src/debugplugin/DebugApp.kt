package debugplugin

import debugplugin.ui.Model
import debugplugin.ui.Views.DebugApp
import javafx.application.Application


fun main(args: Array<String>) {
    Model.instance.PLUGIN = false
    Application.launch(DebugApp::class.java)
}