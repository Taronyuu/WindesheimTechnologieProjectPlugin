package nl.windesheim.codeparser.plugin.action_listeners;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.plugin.dialogs.PatternTreeNode;
import nl.windesheim.codeparser.plugin.services.CodeParser;
import nl.windesheim.reporting.components.TreeNode;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

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

    /**
     * Logger from IntelliJ.
     */
    private final Logger logger;

    /**
     * Get the required Swing object.
     *
     * @param lastUpdateText Label to update when refreshing.
     * @param patternsList   Text area to update when refreshing.
     */
    public MainDialogActionListener(final JLabel lastUpdateText, final JTree patternsList) {
        this.logger = Logger.getLogger(this.getClass().getName());

        this.codeParser = new CodeParser();

        this.patternsList = patternsList;
        this.patternsList.clearSelection();
        this.patternsList.removeAll();

        this.lastUpdateLabel = lastUpdateText;
        this.updateLastUpdatedLabel();
        this.updateFoundPatterns();
    }

    /**
     * @return returns the project the current editor is focused on
     */
    private Project getCurrentProject() {
        //Get the current project depending on the focus
        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        if (dataContext == null) {
            return null;
        }

        return DataKeys.PROJECT.getData(dataContext);
    }

    /**
     * A click happens, refresh the label and found patterns.
     *
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
        List<IDesignPattern> patterns = this.codeParser.findPatternsForProject(getCurrentProject());
        this.patternsList.clearSelection();
        this.patternsList.removeAll();
        DefaultMutableTreeNode defaultRootNode = new DefaultMutableTreeNode("Found Design Patterns");
        this.patternsList.setModel(new DefaultTreeModel(defaultRootNode));

        fillTreeWithPatternsFromTree(codeParser.generateCodeReport(patterns).getTreePresentation().getRoot(),
                defaultRootNode);

        logger.info("New root node has amount of children: " + defaultRootNode.getChildCount());

        this.patternsList.setModel(new DefaultTreeModel(defaultRootNode.getFirstChild()));

    }

    /**
     * Fill the current tree with all the patterns.
     *
     * @param treeNode the root node of the codereport.
     * @param root     the root node of the tree.
     */
    protected void fillTreeWithPatternsFromTree(final TreeNode treeNode, final DefaultMutableTreeNode root) {

        PatternTreeNode currentNode = new PatternTreeNode(treeNode);

        root.add(currentNode);

        if (treeNode.hasChildren()) {
            fillTreeWithPatternsFromTree(treeNode.getFirstChild(), currentNode);
        }

        if (treeNode.hasNextSibling()) {
            fillTreeWithPatternsFromTree(treeNode.getNextSibling(), root);
        }
    }

    /**
     * Get a formatted date string that can be used to fill the last updated label text.
     *
     * @return String
     */
    protected String getFormattedTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
