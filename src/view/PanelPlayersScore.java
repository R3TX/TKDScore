package view;

import model.entity.PlayerColor;
import model.entity.RoundStatisticsEntity;
import utils.ComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PanelPlayersScore extends JPanel {

    public PanelPlayersScore(Map<Integer, List<RoundStatisticsEntity>> playersScore) {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        Dimension statSize = new Dimension(50, 40);
        final double gridWeightx = 1.0 / playersScore.size();

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = gridWeightx;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        //create label for name


        playersScore.forEach((competitorId, statsList) -> {
            gbc.gridy = 0;
            gbc.weighty = 0.2;
            Color competitorColor = PlayerColor.BLUE.name().equals(statsList.get(0).getCompetitor().getAssignedColor()) ? Color.BLUE : Color.RED;
            ComponentBuilder.createAndAddLabel(
                            this,
                            gbc,
                            statsList.get(0).getCompetitor().getName(),
                            JLabel.LEFT,
                            true,
                            statSize,
                            null)
                    .setBackground(competitorColor);

            gbc.gridy = 1;
            gbc.weighty = 0.8;
            JPanel p = new PanelRoundScores(statsList);
            p.setBackground(competitorColor);
            add(p, gbc);
            gbc.gridx += 1;
        });
    }

}
