package nl.windesheim.codeparser.plugin.dialogs;

import com.intellij.icons.AllIcons;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeNode;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

public class PatternTreeNode extends DefaultMutableTreeNode {
    private Icon icon;
    private String tooltip;
    private FilePart filePart;

    public PatternTreeNode(TreeNode node) {
        userObject = node;

        if(node.getClassOrInterface() != null) {
            filePart = node.getClassOrInterface().getFilePart();
        }

        if (node.getNodeType().equals(NodeType.ROOT)) {
            icon = AllIcons.Nodes.Package;
        } else if (node.getNodeType().equals(NodeType.CLASS)) {
            icon = AllIcons.Nodes.Class;
        } else if (node.getNodeType().equals(NodeType.INTERFACE)) {
            icon = AllIcons.Nodes.Interface;
        } else if (node.getNodeType().equals(NodeType.CLASS_LIST)) {
            icon = AllIcons.Hierarchy.Subtypes;
        } else {
            icon = AllIcons.Nodes.Property;
        }
    }

    public Icon getIcon() {
        return icon;
    }

    public PatternTreeNode setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public String getTooltip() {
        return tooltip;
    }

    public PatternTreeNode setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public FilePart getFilePart() {
        return filePart;
    }

    public PatternTreeNode setFilePart(FilePart filePart) {
        this.filePart = filePart;
        return this;
    }
}
