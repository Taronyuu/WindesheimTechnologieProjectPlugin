package nl.windesheim.codeparser.plugin.dialogs;

import com.intellij.icons.AllIcons;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeNode;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;

/**
 * A tree node for the design pattern tree.
 */
public class PatternTreeNode extends DefaultMutableTreeNode {
    /**
     * The icon a node will get.
     */
    private Icon icon;

    /**
     * The tool tip to be shown when hovering over a node.
     */
    private String tooltip;

    /**
     * The part of a file this node is referring to.
     */
    private FilePart filePart;

    /**
     * Default constructor.
     * @param node the TreeNode which we represent with this node.
     */
    public PatternTreeNode(final TreeNode node) {
        super();

        userObject = node;

        if (node.getClassOrInterface() != null) {
            filePart = node.getClassOrInterface().getFilePart();
        }

        HashMap<NodeType, Icon> nodeTypeIcons = new HashMap<>();
        nodeTypeIcons.put(NodeType.ROOT, AllIcons.Nodes.Package);
        nodeTypeIcons.put(NodeType.CLASS, AllIcons.Nodes.Class);
        nodeTypeIcons.put(NodeType.INTERFACE, AllIcons.Nodes.Interface);
        nodeTypeIcons.put(NodeType.CLASS_LIST, AllIcons.Hierarchy.Subtypes);
        nodeTypeIcons.put(NodeType.DESIGN_PATTERN, AllIcons.Nodes.Property);

        icon = nodeTypeIcons.getOrDefault(node.getNodeType(), AllIcons.FileTypes.Unknown);
    }

    /**
     * @return the icon of this node
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon of this node
     * @return this
     */
    public PatternTreeNode setIcon(final Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * @return the tooltip of this node
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip of this node
     * @return this
     */
    public PatternTreeNode setTooltip(final String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    /**
     * @return the filepart of this node
     */
    public FilePart getFilePart() {
        return filePart;
    }

    /**
     * @param filePart the filepart of this node
     * @return this
     */
    public PatternTreeNode setFilePart(final FilePart filePart) {
        this.filePart = filePart;
        return this;
    }
}