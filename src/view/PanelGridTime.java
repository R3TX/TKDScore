package view;

import utils.ComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;


public class PanelGridTime extends JPanel {
    private JLabel matchScore;
    private JLabel timeScore;
    private JLabel roundScore;
    private JLabel breakName;

    public PanelGridTime() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int frameHeight = 600;
        final double SMALL_ROW_WEIGHT = 0.08; // 8% for small rows
        int labelHeight008 = (int) (frameHeight * SMALL_ROW_WEIGHT);
        int labelHeight048 = (int) (frameHeight * 0.48);
        int labelHeight02 = (int) (frameHeight * 0.2);

        Dimension preferredSize008 = new Dimension(1, labelHeight008);

        // label for match name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        gbc.weighty = SMALL_ROW_WEIGHT;

        ComponentBuilder
                .createAndAddLabel(
                        this,
                        gbc,
                        "Match",
                        JLabel.CENTER,
                        false,
                        preferredSize008,
                        null);

        // label for match score
        gbc.gridy = 1;
        gbc.weighty = SMALL_ROW_WEIGHT;
        matchScore = ComponentBuilder
                .createAndAddLabel(
                        this,
                        gbc,
                        "1",
                        JLabel.CENTER,
                        false,
                        preferredSize008,
                        "matchScore");

        // label for time
        gbc.gridy = 2;
        gbc.weighty = 0.48;
        timeScore = ComponentBuilder
                .createAndAddLabel(
                        this,
                        gbc,
                        "00:00",
                        JLabel.CENTER,
                        false,
                        new Dimension(1, labelHeight048),
                        "timeScore");

        // label for Breack name
        gbc.gridy = 3;
        gbc.weighty = 0.2;
        breakName = ComponentBuilder
                .createAndAddLabel(
                        this,
                        gbc,
                        "Break",
                        JLabel.CENTER,
                        false,
                        new Dimension(1, labelHeight02),
                        "breakName");
        breakName.setOpaque(true);
        breakName.setBackground(Color.YELLOW);
        breakName.setVisible(false);

        // Label for Round Name
        gbc.gridy = 4;
        gbc.weighty = SMALL_ROW_WEIGHT;
        ComponentBuilder
                .createAndAddLabel(
                        this,
                        gbc,
                        "Round",
                        JLabel.CENTER,
                        false,
                        preferredSize008,
                        null);

        // Label for round Score
        gbc.gridy = 5;
        gbc.weighty = SMALL_ROW_WEIGHT;
        roundScore = ComponentBuilder
                .createAndAddLabel(
                        this,
                        gbc,
                        "1",
                        JLabel.CENTER,
                        false,
                        preferredSize008,
                        "roundScore");

    }

    public JLabel getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(JLabel matchScore) {
        this.matchScore = matchScore;
    }

    public JLabel getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(JLabel timeScore) {
        this.timeScore = timeScore;
    }

    public JLabel getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(JLabel roundScore) {
        this.roundScore = roundScore;
    }

    public JLabel getBreakName() {
        return breakName;
    }

    public void setBreakName(JLabel breakName) {
        this.breakName = breakName;
    }

    public void setMouseListener(MouseListener mouseListener) {
        matchScore.addMouseListener(mouseListener);
        timeScore.addMouseListener(mouseListener);
        roundScore.addMouseListener(mouseListener);
    }
}