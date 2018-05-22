package nl.windesheim.codeparser.plugin.action_listeners;

import com.intellij.openapi.project.ProjectManager;
import nl.windesheim.codeparser.plugin.services.CodeParser;
import nl.windesheim.reporting.components.CodeReport;
import nl.windesheim.reporting.components.TreeNode;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * @param lastUpdateText Label to update when refreshing.
     * @param patternsList Text area to update when refreshing.
     */
    public MainDialogActionListener(final JLabel lastUpdateText, final JTree patternsList) {
        this.logger = Logger.getLogger(this.getClass().getName());

        // I'm not sure which project we should get, but for now lets take the first project.
        this.codeParser = new CodeParser(ProjectManager.getInstance().getOpenProjects()[0]);

        this.patternsList = patternsList;
        this.patternsList.clearSelection();
        this.patternsList.removeAll();

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
        CodeReport codeReport = this.codeParser.findPatternsForCurrentProject();

        TreeNode root = codeReport.getTreePresentation().getRoot();

        this.logger.info("Root node: " + root);

        this.patternsList.clearSelection();
        this.patternsList.removeAll();
        DefaultMutableTreeNode defaultRootNode = new DefaultMutableTreeNode("Found Design Patterns");
        this.patternsList.setModel(new DefaultTreeModel(defaultRootNode));
        if (root != null) {
            this.fillTreeWithPatterns(codeReport.getTreePresentation().getRoot(), defaultRootNode);

            logger.info("New root node has amount of children: " + defaultRootNode.getChildCount());

            this.patternsList.setModel(new DefaultTreeModel(defaultRootNode));
        }
    }

    /**
     * Fill the current tree with all the patterns.
     * @param treeNode the root node of the codereport.
     * @param root the root node of the tree.
     */
    protected void fillTreeWithPatterns(final TreeNode treeNode, final DefaultMutableTreeNode root) {
        TreeNode node = treeNode;

        // Always keep on looping unless we break the cycle somewhere
        while (true) {
            logger.info("Found one the children: " + node);

            // Create a new category, this should be the name of the pattern. (e.g. Singleton or Observer)
            DefaultMutableTreeNode category = new DefaultMutableTreeNode(node.toString());

            // Temporarily store this node for the siblings
            TreeNode siblings = node;

            // Only continue if there is another sibling
            if (siblings.hasNextSibling()) {
                // Keep on looping as long as there is another sibling.
                while (siblings.hasNextSibling()) {
                    // Get the first next sibling and store it in  the variable
                    siblings = siblings.getNextSibling();

                    logger.info("Found one of the siblings: " + siblings);

                    // Add this node as a category under the design pattern. This should be a file name.
                    category.add(new DefaultMutableTreeNode(siblings.toString()));
                }
            }

            // Ignore empty categories
            if (category.getChildCount() > 0) {
                root.add(category);
            }

            // If the following child is null, we should break
            // Otherwise continue with the loop and use the next first child.
            if (node.getFirstChild() == null) {
                break;
            } else {
                node = node.getFirstChild();
            }
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
