package nl.windesheim.codeparser.plugin.action_listeners;

import com.intellij.openapi.project.ProjectManager;
import nl.windesheim.codeparser.plugin.services.CodeParser;
import nl.windesheim.reporting.components.CodeReport;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Action listener for the main dialog refresh button.
 */
public class MainDialogActionListener implements ActionListener {

    /**
     * Message to provide the user with a last updated at timestamp.
     */
    private final JLabel lastUpdateLabel;

    /**
     * Default last update label text.
     */
    private static final String LAST_UPDATE_TEXT = "Last updated: ";

    /**
     * A CodeParser wrapper object.
     */
    private final CodeParser codeParser;

    /**
     * A text area to show the current patterns.
     */
    private final JTextArea patternsList;

    /**
     * Get the required Swing object.
     * @param lastUpdateText Label to update when refreshing.
     * @param patternsList Text area to update when refreshing.
     */
    public MainDialogActionListener(final JLabel lastUpdateText, final JTextArea patternsList) {
        // I'm not sure which project we should get, but for now lets take the first project.
        this.codeParser = new CodeParser(ProjectManager.getInstance().getOpenProjects()[0]);

        this.patternsList = patternsList;
        this.lastUpdateLabel = lastUpdateText;
        this.updateLastUpdatedLabel();
        this.updateFoundPatterns();
    }

    /**
     * A click happens, refresh the label and found patterns.
     * @param event action event given by the button.
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        this.updateLastUpdatedLabel();
        this.updateFoundPatterns();
    }

    /**
     * Update the last updated label text.
     */
    protected void updateLastUpdatedLabel() {
        this.lastUpdateLabel.setText(this.LAST_UPDATE_TEXT + this.getFormattedTimestamp());
    }

    /**
     * Update the found patterns text area by getting
     * the patterns from the CodeParser wrapper.
     */
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
