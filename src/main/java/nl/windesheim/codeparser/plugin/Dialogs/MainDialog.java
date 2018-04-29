package nl.windesheim.codeparser.plugin.Dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import nl.windesheim.codeparser.plugin.ActionListeners.MainDialogActionListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MainDialog implements ToolWindowFactory {
    private ToolWindow toolWindow;

    private JPanel toolPanel;

    // Button to refresh all the found design patterns.
    private JButton refreshButton;

    // Tree list that should show all the found design patterns.
    private JTextArea patternsList;

    // Message to provide the user with a last updated at timestamp
    private JLabel lastUpdateText;

    public MainDialog() {
        this.patternsList.setEditable(false);
        this.refreshButton.addActionListener(
                new MainDialogActionListener(this.lastUpdateText, this.patternsList)
        );
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(this.toolPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
