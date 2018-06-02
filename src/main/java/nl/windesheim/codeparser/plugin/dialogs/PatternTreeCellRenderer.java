package nl.windesheim.codeparser.plugin.dialogs;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

/**
 * Cell renderer for the pattern tree nodes.
 */
public class PatternTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(
            final JTree tree,
            final Object value,
            final boolean sel,
            final boolean expanded,
            final boolean leaf,
            final int row,
            final boolean hasFocus
    ) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Icon icon = null;

        if (value instanceof PatternTreeNode) {
            PatternTreeNode node = (PatternTreeNode) value;
            if (node.getIcon() != null) {
                icon = ((PatternTreeNode) value).getIcon();
            }

            setToolTipText(node.getTooltip());
        } else {
            if (leaf) {
                icon = getLeafIcon();
            } else if (expanded) {
                icon = getOpenIcon();
            } else {
                icon = getClosedIcon();
            }
        }

        if (tree.isEnabled()) {
            setEnabled(true);
            setIcon(icon);
        } else {
            setEnabled(false);
            LookAndFeel laf = UIManager.getLookAndFeel();
            Icon disabledIcon = laf.getDisabledIcon(tree, icon);
            if (disabledIcon != null) {
                icon = disabledIcon;
            }
            setDisabledIcon(icon);
        }

        return this;
    }


}
