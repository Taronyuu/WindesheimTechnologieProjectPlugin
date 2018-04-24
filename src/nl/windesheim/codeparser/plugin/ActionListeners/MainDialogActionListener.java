package nl.windesheim.codeparser.plugin.ActionListeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainDialogActionListener implements ActionListener {

    // Message to provide the user with a last updated at timestamp
    private JLabel lastUpdateText;
    private String lastUpdateDefaultText = "Last updated: ";

    public MainDialogActionListener(JLabel lastUpdateText) {
        this.lastUpdateText = lastUpdateText;
        this.updateLastUpdatedLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.updateLastUpdatedLabel();
    }

    /**
     * Update the last updated label text.
     */
    protected void updateLastUpdatedLabel() {
        this.lastUpdateText.setText(this.lastUpdateDefaultText + this.getFormattedTimestamp());
    }

    /**
     * Get a formatted date string that can be used to fill the last updated label text.
     * @return String
     */
    protected String getFormattedTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
