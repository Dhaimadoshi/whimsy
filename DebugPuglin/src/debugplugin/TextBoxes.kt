package debugplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.sun.javafx.scene.CameraHelper.project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import norswap.autumn.debugger.debug_test


// If you register the action from Java code, this constructor is used to set the menu item name
// (optionally, you can specify the menu description and an icon to display next to the menu item).
// You can omit this constructor when registering the action in the plugin.xml file.
class TextBoxes : AnAction("Text _Boxes","Item description", IconLoader.getIcon("/debugplugin/ui/icon.png")) {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT)
//        if (project == null) throw exception
//        val vFiles = ProjectRootManager.getInstance(project!!).contentSourceRoots

        var txt = Messages.showInputDialog(project, "What file", "Input file path", Messages.getQuestionIcon())
        if (txt == null) txt = "/Users/dhai/Dropbox/thesis/debugplugin.test"
        val test = debug_test(txt!!).invoke()

        // Messages.showMessageDialog(project, "Hello, $txt!\n I am glad to see you.", "Information", Messages.getInformationIcon())

    }
}
// Set the menu item name, description and icon.
// super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
