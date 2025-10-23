package utils;

import view.adapter.FontResizerUtility;

import javax.swing.*;
import java.awt.*;

// Assuming FontResizerUtility and TDKScoreUtils are available
// import tks.src.util.FontResizerUtility;
// import tks.src.util.TDKScoreUtils;

/**
 * Utility class to create and configure JLabels consistently
 * before adding them to a GridBagLayout container.
 */
public class ComponentBuilder {

    /**
     * Creates, configures, and adds a JLabel to a container that uses GridBagLayout.
     *
     * @param container The container (JPanel, JFrame, etc.) to which the label will be added.
     * @param gbc       The GridBagConstraints defining the position and weight.
     * @param text      The initial text for the label.
     * @param alignment The horizontal alignment of the text (e.g., JLabel.CENTER).
     * @param isOpaque  Indicates whether the JLabel should be opaque.
     * @param statSize  The preferred/minimum dimension to apply.
     * @return The created JLabel (needed for ScoreManager to handle).
     */
    public static JLabel createAndAddLabel(
            Container container,
            GridBagConstraints gbc,
            String text,
            int alignment,
            boolean isOpaque,
            Dimension statSize) {

        JLabel label = new JLabel(TDKScoreUtils.formatTextColor(text));

        // Standard configuration for appearance and size
        // Note: FontResizerUtility must be accessible
        label.addComponentListener(new FontResizerUtility(label));
        label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        label.setHorizontalAlignment(alignment);
        label.setVerticalAlignment(JLabel.CENTER);

        // Use setMinimumSize/setPreferredSize to ensure consistency in GBL
        if (statSize != null) {
            label.setMinimumSize(statSize);
            label.setPreferredSize(statSize);
        }
        label.setOpaque(isOpaque);

        // Add to the container
        container.add(label, gbc);

        return label;
    }
}