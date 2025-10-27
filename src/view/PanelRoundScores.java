package view;

import model.entity.RoundStatisticsEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelRoundScores extends JPanel {

    public PanelRoundScores(List<RoundStatisticsEntity> roundsStat) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        final double gridWeighty = 1.0 / roundsStat.size();
        setOpaque(true);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weighty = gridWeighty;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        roundsStat.stream().forEach(stat -> {
            JPanel p = new PanelRoundStatistics(stat, gbc.gridy + 1);
            add(p, gbc);
            gbc.gridy++;
        });
    }
}
