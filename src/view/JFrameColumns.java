package view;

import model.entity.MatchEntity;
import model.entity.PlayerColor;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class JFrameColumns extends JFrame implements ScoreboardView {

    private PanelGridScore redPanel;
    private PanelGridScore bluePanel;
    private PanelGridTime timePanel;

    public JFrameColumns() {
        createBoardView();
    }

    public void createBoardView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        int frameWidth = 800;
        int panelWidth45 = (int) (frameWidth * 0.45);
        int panelWidth10 = (int) (frameWidth * 0.10);

        Dimension preferredSize45 = new Dimension(panelWidth45, 1);
        // Panel 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.45;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        redPanel = new PanelGridScore(PlayerColor.RED);
        redPanel.setPreferredSize(preferredSize45);
        redPanel.setBackground(Color.RED);
        redPanel.getCompetitorScore().setBackground(TDKScoreUtils.RED_COLOR);
        add(redPanel, gbc);

        // Panel 2 (columna del medio)
        gbc.gridx = 1;
        gbc.weightx = 0.1;
        timePanel = new PanelGridTime();
        timePanel.setPreferredSize(new Dimension(panelWidth10, 1));
        timePanel.setBackground(Color.BLACK);
        add(timePanel, gbc);

        // Panel 3
        gbc.gridx = 2;
        gbc.weightx = 0.45;
        bluePanel = new PanelGridScore(PlayerColor.BLUE);
        bluePanel.setPreferredSize(preferredSize45);
        bluePanel.setBackground(Color.BLUE);
        bluePanel.getCompetitorScore().setBackground(TDKScoreUtils.BLUE_COLOR);
        add(bluePanel, gbc);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // =================================================================
    // SCOREBOARDVIEW INTERFACE METHODS
    // =================================================================

    @Override
    public void updateMainScore(int redScore, int blueScore) {
        updateText(redPanel.getCompetitorScore(), String.valueOf(redScore));
        updateText(bluePanel.getCompetitorScore(), String.valueOf(blueScore));
    }

    @Override
    public void updateFouls(int redFouls, int blueFouls) {
        // The view uses the GAM-JEOM count
        updateText(redPanel.getGamjeonCount(), String.valueOf(redFouls));
        updateText(bluePanel.getGamjeonCount(), String.valueOf(blueFouls));
    }

    @Override
    public void updateTimerDisplay(String time) {
        updateText(timePanel.getTimeScore(), time);
    }

    @Override
    public void updateRoundNumber(int roundNumber) {
        updateText(timePanel.getRoundScore(), String.valueOf(roundNumber));
    }

    @Override
    public void updateRoundWins(int redWins, int blueWins) {
        // The view uses 'victoriesCount' for total rounds won in the match
        updateText(redPanel.getVictoriesCount(), String.valueOf(redWins));
        updateText(bluePanel.getVictoriesCount(), String.valueOf(blueWins));
    }

    // --- Game Flow and State Methods ---

    @Override
    public void onMatchStarted(MatchEntity match) {
        // 1. Update match number
        updateText(timePanel.getMatchScore(), String.valueOf(match.getMatchNumber()));

        // 2. Update competitor names (assuming MatchController can retrieve them)
        try {
            // These methods must exist in MatchController to decouple the view from the DAO.
            String redName = match.getRedCompetitor().getName();
            String blueName = match.getBlueCompetitor().getName();
            updateText(redPanel.getCompetitorName(), redName);
            updateText(bluePanel.getCompetitorName(), blueName);
        } catch (Exception e) {
            System.err.println("Error setting competitor names: " + e.getMessage());
            updateText(redPanel.getCompetitorName(), "RED");
            updateText(bluePanel.getCompetitorName(), "BLUE");
        }

        // 3. Initialize state
        updateRoundNumber(1);
        restoreScoreFoulsHeadKick();
        JOptionPane.showMessageDialog(this, "Match " + match.getMatchNumber() + " started!", "New Match", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onRoundConcluded(PlayerColor playerColor, boolean isBreakTime) {
        // 1. Clear the scoreboard from the previous round
        //restoreInitialScoreAndFouls();

        // 2. Handle the break/pause state
        if (isBreakTime) {
            timePanel.getBreakName().setVisible(true);
            paintWinner(playerColor);
        } else {
            timePanel.getBreakName().setVisible(false);
            restoreWinnerColor();
        }
        // NOTE: updateRoundWins() must be called by the controller after updating the model.
    }

    // 3. Highlight the round winner (if applicable)
    private void paintWinner(PlayerColor playerColor){
        if (playerColor != null) {
            PanelGridScore winnerPanel = PlayerColor.RED.equals(playerColor)
                    ? redPanel : bluePanel;
            // It's assumed we only want to temporarily highlight the score with a different color.
            winnerPanel.getCompetitorScore().setBackground(Color.YELLOW);

            JOptionPane.showMessageDialog(this, "Round concluded! Winner ID: " + playerColor, "End Round", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Restore winner color
    private void restoreWinnerColor(){
        redPanel.getCompetitorScore().setBackground(TDKScoreUtils.RED_COLOR);
        bluePanel.getCompetitorScore().setBackground(TDKScoreUtils.BLUE_COLOR);
    }

    @Override
    public void onMatchConcluded(PlayerColor playerColor) {

        JOptionPane.showMessageDialog(this, "Match Finished! Overall Winner: " + playerColor, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        // Call full state restoration after showing the message.
        restoreInitialState();
    }

    @Override
    public void restoreInitialState() {
        // Resets all counters to zero or initial state.
        restoreScoreFoulsHeadKick();
        updateRoundWins(0, 0);
        updateRoundNumber(1);
        updateText(timePanel.getMatchScore(), "1");

        // Reset styles and colors
        redPanel.getCompetitorScore().setBackground(TDKScoreUtils.RED_COLOR);
        bluePanel.getCompetitorScore().setBackground(TDKScoreUtils.BLUE_COLOR);
        timePanel.getBreakName().setVisible(false);

        // Chronometer logic
        // chronometer.restartTime(chronometer.getMatchTime());
    }

    // --- View Auxiliary Methods (UI) ---
    @Override
    public void restoreScoreFoulsHeadKick() {
        // Resets only scores and fouls, useful at the start of each round.
        updateMainScore(0, 0);
        updateFouls(0, 0);
        // HeadKickCount must also be reset if they are per round.
        updateHeadKicks(0, 0);
    }

    public void updateNames(String redName, String blueName) {
        updateText(redPanel.getCompetitorName(), redName);
        updateText(bluePanel.getCompetitorName(), blueName);
    }

    @Override
    public void updateHeadKicks(int redKicks, int blueKicks) {
        updateText(redPanel.getHeadKickCount(), String.valueOf(redKicks));
        updateText(bluePanel.getHeadKickCount(), String.valueOf(blueKicks));
    }

    public void updateMatchNumber(int matchNumber) {
        updateText(timePanel.getMatchScore(), String.valueOf(matchNumber));
    }

    private void updateText(JLabel label, String text) {
        label.setText(TDKScoreUtils.formatTextColor(text));
        TDKScoreUtils.formatJlabelText(label);
        label.repaint();
    }

    public void setMouseListenerToComponent(MouseListener mouseListener) {
        redPanel.setMouseListener(mouseListener);
        bluePanel.setMouseListener(mouseListener);
        timePanel.setMouseListener(mouseListener);

    }
}
