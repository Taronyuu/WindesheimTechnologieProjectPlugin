package nl.windesheim.codeparser.plugin.ActionListeners;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import nl.windesheim.codeparser.plugin.Services.CodeParser;
import nl.windesheim.reporting.components.CodeReport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainDialogActionListener implements ActionListener {

    // Message to provide the user with a last updated at timestamp
    private JLabel lastUpdateText;
    private String lastUpdateDefaultText = "Last updated: ";

    private Project project;
    private CodeParser codeParser;
    private JTextArea patternsList;

    public MainDialogActionListener(JLabel lastUpdateText, JTextArea patternsList) {
        // I'm not sure which project we should get, but for now lets take the first project.
        this.project = ProjectManager.getInstance().getOpenProjects()[0];

        this.codeParser = new CodeParser(this.project);

        this.patternsList = patternsList;
        this.lastUpdateText = lastUpdateText;
        this.updateLastUpdatedLabel();
        this.updateFoundPatterns();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.updateLastUpdatedLabel();
        this.updateFoundPatterns();
    }

    /**
     * Update the last updated label text.
     */
    protected void updateLastUpdatedLabel() {
        this.lastUpdateText.setText(this.lastUpdateDefaultText + this.getFormattedTimestamp());
    }

    protected void updateFoundPatterns() {
        CodeReport codeReport = this.codeParser.findPatternForCurrentFile();
        this.patternsList.setText(codeReport.getReport());
    }

    /**
     * Get a formatted date string that can be used to fill the last updated label text.
     * @return String
     */
    protected String getFormattedTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
