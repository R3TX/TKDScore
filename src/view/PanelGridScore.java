package view;

import model.entity.PlayerColor;
import utils.ComponentBuilder;

import javax.swing.*;
import java.awt.*;


public class PanelGridScore extends JPanel {
    private JLabel competitorName;
    private JLabel competitorScore;
    private JLabel gamjeonCount;
    private JLabel headKickCount;
    private JLabel victoriesCount;

    public PanelGridScore(PlayerColor playerColor) {
        setLayout(new GridBagLayout());
        Dimension statSize = new Dimension(50, 40);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        final double gridWeightx = 1.0 / 3.0;

        // Label for competitor name
        if (PlayerColor.RED.equals(playerColor)) {
            gbc.gridx = 1;
        } else {
            gbc.gridx = 0;
        }

        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        gbc.weightx = gridWeightx * 2;
        gbc.weighty = 0.15;

        competitorName = ComponentBuilder.createAndAddLabel(this, gbc, "Nombre del competidor", JLabel.CENTER, false, statSize);

        // label for victories name
        if (PlayerColor.RED.equals(playerColor)) {
            gbc.gridx = 0;
        } else {
            gbc.gridx = 2;
        }


        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = gridWeightx;
        gbc.weighty = 0.075;

        ComponentBuilder.createAndAddLabel(this, gbc, "Victorias", JLabel.CENTER, false, statSize);

        // Label for victories count

        gbc.gridy = 1;
        gbc.weightx = gridWeightx;
        gbc.weighty = 0.075;
        victoriesCount = ComponentBuilder.createAndAddLabel(this, gbc, "0", JLabel.CENTER, false, statSize);

        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weighty = 0.65;

        // Label for competitor score
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = gridWeightx;
        competitorScore = ComponentBuilder.createAndAddLabel(this, gbc, "0", JLabel.CENTER, true, statSize);

        //name's row
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.weighty = 0.1; // gives 10% vertical space
        gbc.gridwidth = 1;

        // label for GAM-JEOM Name
        if (PlayerColor.RED.equals(playerColor)) {
            gbc.gridx = 0;
        } else {
            gbc.gridx = 2;
        }
        gbc.weightx = gridWeightx;
        ComponentBuilder.createAndAddLabel(this, gbc, "GAM-JEOM", JLabel.CENTER, false, statSize);

        // label for head kicks name
        gbc.gridx = 1;
        gbc.weightx = gridWeightx;
        gbc.gridwidth = 1;
        ComponentBuilder.createAndAddLabel(this, gbc, "Técnica alta", JLabel.CENTER, false, statSize);

        //empty space name
        if (PlayerColor.RED.equals(playerColor)) {
            gbc.gridx = 2;
        } else {
            gbc.gridx = 0;
        }
        gbc.weightx = gridWeightx;
        gbc.gridwidth = 1;
        ComponentBuilder.createAndAddLabel(this, gbc, "", JLabel.CENTER, false, statSize);

        //Counters row
        gbc.gridy = 4;
        gbc.weighty = 0.1; // proportion 10 % vertical space (10% + 10% = 20% )

        // Label for GAM-JEOM score
        if (PlayerColor.RED.equals(playerColor)) {
            gbc.gridx = 0;
        } else {
            gbc.gridx = 2;
        }
        gbc.weightx = gridWeightx;
        gbc.gridwidth = 1;
        gamjeonCount = ComponentBuilder.createAndAddLabel(this, gbc, "0", JLabel.CENTER, false, statSize);

        // Head kicks count
        gbc.gridx = 1;
        gbc.weightx = gridWeightx;
        gbc.gridwidth = 1;
        headKickCount = ComponentBuilder.createAndAddLabel(this, gbc, "0", JLabel.CENTER, false, statSize);

        //empty space score
        if (PlayerColor.RED.equals(playerColor)) {
            gbc.gridx = 2;
        } else {
            gbc.gridx = 0;
        }

        gbc.weightx = gridWeightx;
        gbc.gridwidth = 1;
        ComponentBuilder.createAndAddLabel(this, gbc, "", JLabel.CENTER, false, statSize);
    }

    public JLabel getCompetitorName() {
        return competitorName;
    }

    public void setCompetitorName(JLabel competitorName) {
        this.competitorName = competitorName;
    }

    public JLabel getCompetitorScore() {
        return competitorScore;
    }

    public void setCompetitorScore(JLabel competitorScore) {
        this.competitorScore = competitorScore;
    }

    public JLabel getGamjeonCount() {
        return gamjeonCount;
    }

    public void setGamjeonCount(JLabel gamjeonCount) {
        this.gamjeonCount = gamjeonCount;
    }

    public JLabel getHeadKickCount() {
        return headKickCount;
    }

    public void setHeadKickCount(JLabel headKickCount) {
        this.headKickCount = headKickCount;
    }

    public JLabel getVictoriesCount() {
        return victoriesCount;
    }

    public void setVictoriesCount(JLabel victoriesCount) {
        this.victoriesCount = victoriesCount;
    }
}