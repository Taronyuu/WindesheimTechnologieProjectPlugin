package nl.windesheim.codeparser.plugin.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

public class PatternTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Icon icon = null;

        if(value instanceof PatternTreeNode){
            PatternTreeNode node = (PatternTreeNode) value;
            if(node.getIcon() != null) {
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

        if (!tree.isEnabled()) {
            setEnabled(false);
            LookAndFeel laf = UIManager.getLookAndFeel();
            Icon disabledIcon = laf.getDisabledIcon(tree, icon);
            if (disabledIcon != null) icon = disabledIcon;
            setDisabledIcon(icon);
        } else {
            setEnabled(true);
            setIcon(icon);
        }

        return this;
    }


}
