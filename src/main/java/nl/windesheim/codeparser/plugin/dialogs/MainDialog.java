package nl.windesheim.codeparser.plugin.dialogs;

import nl.windesheim.codeparser.plugin.action_listeners.MainDialogActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

/**
 * The MianDialog from where all the action happens.
 */
public class MainDialog {

    /**
     * The wrapper panel for all the other elements.
     */
    private JPanel toolPanel;

    /**
     * Button to refresh all the found design patterns.
     */
    private JButton refreshButton;

    /**
     * Tree list that should show all the found design patterns.
     */
    private JTree patternsList;

    /**
     * Message to provide the user with a last updated at timestamp.
     */
    private JLabel lastUpdateText;

    /**
     * Constructor for this class, used to add an action listener.
     */
    public MainDialog() {
        this.patternsList.setEditable(false);
        this.refreshButton.addActionListener(
                new MainDialogActionListener(this.lastUpdateText, this.patternsList)
        );
    }

    /**
     * @return the tool panel
     */
    public JPanel getToolPanel() {
        return toolPanel;
    }
}
