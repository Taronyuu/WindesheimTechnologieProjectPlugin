package nl.windesheim.codeparser.plugin.action_listeners;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.codeparser.patterns.Strategy;
import nl.windesheim.codeparser.plugin.dialogs.PatternTreeNode;
import nl.windesheim.codeparser.plugin.services.CodeParser;
import nl.windesheim.reporting.components.CodeReport;
import nl.windesheim.reporting.components.TreeNode;
import org.apache.xmlbeans.impl.xb.xsdschema.All;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
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
     * @param lastUpdateText Label to update when refreshing.
     * @param patternsList Text area to update when refreshing.
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
        if(dataContext == null) {
            return null;
        }

        return DataKeys.PROJECT.getData(dataContext);
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
        List<IDesignPattern> patterns = this.codeParser.findPatternsForProject(getCurrentProject());
        this.patternsList.clearSelection();
        this.patternsList.removeAll();
        DefaultMutableTreeNode defaultRootNode = new DefaultMutableTreeNode("Found Design Patterns");
       // defaultRootNode.setIcon(AllIcons.Nodes.Package);
        this.patternsList.setModel(new DefaultTreeModel(defaultRootNode));

        fillTreeWithPatternsFromTree(codeParser.generateCodeReport(patterns).getTreePresentation().getRoot(), defaultRootNode);
        //this.fillTreeWithPatterns(patterns, defaultRootNode);

        logger.info("New root node has amount of children: " + defaultRootNode.getChildCount());

        this.patternsList.setModel(new DefaultTreeModel(defaultRootNode.getFirstChild()));

    }

    /**
     * Fill the current tree with all the patterns.
     * @param treeNode the root node of the codereport.
     * @param root the root node of the tree.
     */
    protected void fillTreeWithPatternsFromTree(final TreeNode treeNode, final DefaultMutableTreeNode root) {

        PatternTreeNode currentNode = new PatternTreeNode(treeNode);

        root.add(currentNode);

        if (treeNode.hasChildren()){
            fillTreeWithPatternsFromTree(treeNode.getFirstChild(), currentNode);
        }

        if (treeNode.hasNextSibling()) {
            fillTreeWithPatternsFromTree(treeNode.getNextSibling(), root);
        }
    }


  /*  protected void fillTreeWithPatterns(final List<IDesignPattern> patterns, final DefaultMutableTreeNode root){
        for (IDesignPattern pattern : patterns){
            if (pattern instanceof Singleton){
                Singleton singleton = (Singleton) pattern;

                PatternTreeNode node = new PatternTreeNode("Singleton pattern");
                node.setIcon(AllIcons.Nodes.Property);
                node.setFilePart(singleton.getSingletonClass().getFilePart());

                root.add(node);
            }

            if (pattern instanceof Strategy){
                Strategy strategyPattern = (Strategy) pattern;

                PatternTreeNode node = new PatternTreeNode("Strategy pattern");
                node.setIcon(AllIcons.Nodes.Property);

                ClassOrInterface contextClass = strategyPattern.getContext();
                PatternTreeNode context = new PatternTreeNode("Context: " + contextClass.getName());
                context.setTooltip("<html>File: "+ contextClass.getFilePart().getFile().getName() +"@"
                        + contextClass.getFilePart().getRange() +"</html>");
                context.setIcon(AllIcons.Nodes.Class);
                context.setFilePart(contextClass.getFilePart());
                node.add(context);

                ClassOrInterface strategyInterface = strategyPattern.getStrategyInterface();
                PatternTreeNode interfaceNode = new PatternTreeNode("Strategy interface: " + strategyInterface.getName());
                interfaceNode.setTooltip("<html>File: "+ strategyInterface.getFilePart().getFile().getName() +"@"
                        + strategyInterface.getFilePart().getRange() +"</html>");
                interfaceNode.setIcon(AllIcons.Nodes.Interface);
                interfaceNode.setFilePart(strategyInterface.getFilePart());
                node.add(interfaceNode);

                //Adding the strategies
                PatternTreeNode strategies = new PatternTreeNode("Strategy classes");
                strategies.setIcon(AllIcons.Hierarchy.Subtypes);
                for (ClassOrInterface strategy : strategyPattern.getStrategies()){
                    PatternTreeNode strategyNode = new PatternTreeNode(strategy.getName());
                    strategyNode.setTooltip("<html>File: "+ strategy.getFilePart().getFile().getName() +"@"
                            + strategy.getFilePart().getRange() +"</html>");
                    strategyNode.setIcon(AllIcons.Nodes.Class);
                    strategyNode.setFilePart(strategy.getFilePart());
                    strategies.add(strategyNode);
                }
                node.add(strategies);

                root.add(node);
            }
        }
    }*/
//    /**
//     * Fill the current tree with all the patterns.
//     * @param treeNode the root node of the codereport.
//     * @param root the root node of the tree.
//     */
//    protected void fillTreeWithPatternsFromTree(final TreeNode treeNode, final DefaultMutableTreeNode root) {
//        TreeNode node = treeNode;
//
//        // Always keep on looping unless we break the cycle somewhere
//        while (true) {
//            logger.info("Found one the children: " + node);
//
//            // Create a new category, this should be the name of the pattern. (e.g. Singleton or Observer)
//            DefaultMutableTreeNode category = new DefaultMutableTreeNode(node.toString());
//
//            // Temporarily store this node for the siblings
//            TreeNode siblings = node;
//
//            // Only continue if there is another sibling
//            if (siblings.hasNextSibling()) {
//                // Keep on looping as long as there is another sibling.
//                while (siblings.hasNextSibling()) {
//                    // Get the first next sibling and store it in  the variable
//                    siblings = siblings.getNextSibling();
//
//                    logger.info("Found one of the siblings: " + siblings);
//
//                    // Add this node as a category under the design pattern. This should be a file name.
//                    category.add(new DefaultMutableTreeNode(siblings.toString()));
//
//                    if (siblings.hasChildren()) {
//                        this.fillTreeWithPatternsFromTree(siblings, category);
//                    }
//                }
//            }
//
//            // Ignore empty categories
//            if (category.getChildCount() > 0) {
//                root.add(category);
//            }
//
//            // If the following child is null, we should break
//            // Otherwise continue with the loop and use the next first child.
//            if (node.getFirstChild() == null) {
//                break;
//            } else {
//                node = node.getFirstChild();
//            }
//        }
//    }

    /**
     * Get a formatted date string that can be used to fill the last updated label text.
     * @return String
     */
    protected String getFormattedTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
