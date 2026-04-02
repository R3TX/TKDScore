package controller.listeners;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TDKActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Verify what object was clicked
        if (source instanceof JButton) {
            JButton button = (JButton) source;
            if (StringUtils.isNoneEmpty(button.getName())) {
                switch (button.getName()) {
                    case "nameBLUE":
                        break;
                    case "name":
                        break;
                }
            }
        }
    }
}
