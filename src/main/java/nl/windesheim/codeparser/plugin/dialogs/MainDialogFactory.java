package nl.windesheim.codeparser.plugin.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for the main dialog.
 */
public class MainDialogFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(new MainDialog().getToolPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
