package nl.windesheim.codeparser.plugin.action_listeners;

import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.treeStructure.Tree;
import nl.windesheim.codeparser.plugin.services.CodeParser;
import nl.windesheim.reporting.components.CodeReport;
import nl.windesheim.reporting.components.TreeNode;
import nl.windesheim.reporting.components.TreePresentation;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
    private JTree patternsList;

    private DefaultMutableTreeNode defaultMutableTreeNode;

    /**
     * Get the required Swing object.
     * @param lastUpdateText Label to update when refreshing.
     * @param patternsList Text area to update when refreshing.
     */
    public MainDialogActionListener(final JLabel lastUpdateText, final JTree patternsList) {
        this.defaultMutableTreeNode = new DefaultMutableTreeNode("Found Design Patterns");

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
        this.lastUpdateLabel.setText(LAST_UPDATE_TEXT + this.getFormattedTimestamp());
    }

    /**
     * Update the found patterns text area by getting
     * the patterns from the CodeParser wrapper.
     */
    protected void updateFoundPatterns() {
        CodeReport codeReport = this.codeParser.findPatternForCurrentFile();

        TreeNode root = codeReport.getTreePresentation().getRoot();
        this.patternsList.clearSelection();
        this.patternsList.removeAll();
        if(root != null){
            this.defaultMutableTreeNode.removeAllChildren();
            this.fillTreeWithPatterns(codeReport.getTreePresentation().getRoot());
            this.patternsList = new Tree(this.defaultMutableTreeNode);
        }
    }

    /**
     * Put the logic to fill the JTree here.
     * @param codeReport generated codereport
     */
    protected void fillTreeWithPatterns(TreeNode node) {
        while(node.hasNextSibling()){
            DefaultMutableTreeNode category = new DefaultMutableTreeNode(node.toString());

            TreeNode children = node;
            if(children.hasChildren()){
                while(children.hasChildren()){
                    children = children.getFirstChild();
                    category.add(new DefaultMutableTreeNode(children.toString()));
                }
            }

            this.defaultMutableTreeNode.add(category);
        }
    }

    /**
     * Get a formatted date string that can be used to fill the last updated label text.
     * @return String
     */
    protected String getFormattedTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
