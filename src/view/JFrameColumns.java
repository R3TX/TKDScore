package view;

import controller.listeners.Chronometer;
import controller.match.MatchController;
import model.entity.MatchEntity;
import model.entity.PlayerColor;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;

public class JFrameColumns extends JFrame implements ScoreboardView {

    private final MatchController matchController;
    private final Chronometer chronometer; // Se requiere para el control de tiempo

    private PanelGridScore redPanel;
    private PanelGridScore bluePanel;
    private PanelGridTime timePanel;

    public JFrameColumns(MatchController matchController, Chronometer chronometer) {
        this.matchController = matchController;
        this.chronometer = chronometer;
        createBoardView();


    }

    public void createBoardView(){
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
        redPanel.getCompetitorScore().setText(String.valueOf(redScore));
        bluePanel.getCompetitorScore().setText(String.valueOf(blueScore));
        // Logic for font resizing based on score (if applicable) goes here
    }

    @Override
    public void updateFouls(int redFouls, int blueFouls) {
        // The view uses the GAM-JEOM count
        redPanel.getGamjeonCount().setText(String.valueOf(redFouls));
        bluePanel.getGamjeonCount().setText(String.valueOf(blueFouls));
    }

    @Override
    public void updateTimerDisplay(String time) {
        timePanel.getTimeScore().setText(time);
    }

    @Override
    public void updateRoundNumber(int roundNumber) {
        timePanel.getRoundScore().setText(String.valueOf(roundNumber));
    }

    @Override
    public void updateRoundWins(int redWins, int blueWins) {
        // The view uses 'victoriesCount' for total rounds won in the match
        redPanel.getVictoriesCount().setText(String.valueOf(redWins));
        bluePanel.getVictoriesCount().setText(String.valueOf(blueWins));
    }

    // --- Game Flow and State Methods ---

    @Override
    public void onMatchStarted(MatchEntity match) {
        // 1. Update match number
        timePanel.getMatchScore().setText(String.valueOf(match.getMatchNumber()));

        // 2. Update competitor names (assuming MatchController can retrieve them)
        try {
            // These methods must exist in MatchController to decouple the view from the DAO.
            String redName = match.getRedCompetitor().getName();
            String blueName = match.getBlueCompetitor().getName();
            redPanel.getCompetitorName().setText(redName);
            bluePanel.getCompetitorName().setText(blueName);
        } catch (Exception e) {
            System.err.println("Error setting competitor names: " + e.getMessage());
            redPanel.getCompetitorName().setText("RED ID: " + match.getRedCompetitor().getuId());
            bluePanel.getCompetitorName().setText("BLUE ID: " + match.getBlueCompetitor().getuId());
        }

        // 3. Initialize state
        updateRoundNumber(1);
        restoreInitialScoreAndFouls();
        JOptionPane.showMessageDialog(this, "Match " + match.getMatchNumber() + " started!", "New Match", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onRoundConcluded(Integer winnerId, boolean isBreakTime) {
        // 1. Clear the scoreboard from the previous round
        restoreInitialScoreAndFouls();

        // 2. Handle the break/pause state
        if (isBreakTime) {
            timePanel.getBreackName().setOpaque(true);
            timePanel.getBreackName().setBackground(Color.YELLOW);
            timePanel.getBreackName().setForeground(Color.BLACK);
        } else {
            timePanel.getBreackName().setOpaque(false);
            timePanel.getBreackName().setBackground(Color.BLACK);
            timePanel.getBreackName().setForeground(Color.WHITE); // Restore text color if dark
        }

        // 3. Highlight the round winner (if applicable)
        if (winnerId != null && winnerId != 0) {
            MatchEntity currentMatch = matchController.getCurrentMatch();

            PanelGridScore winnerPanel = (winnerId == currentMatch.getRedCompetitor().getuId())
                    ? redPanel : bluePanel;
            // It's assumed we only want to temporarily highlight the score with a different color.
            winnerPanel.getCompetitorScore().setBackground(Color.YELLOW);

            JOptionPane.showMessageDialog(this, "Round concluded! Winner ID: " + winnerId, "End Round", JOptionPane.INFORMATION_MESSAGE);
        }

        // NOTE: updateRoundWins() must be called by the controller after updating the model.
    }

    @Override
    public void onMatchConcluded(int winnerId) {
        MatchEntity currentMatch = matchController.getCurrentMatch();
        String winnerName = "WINNER";

        try {
            winnerName = matchController.getCompetitorName(winnerId);
        } catch (Exception ignored) { /* Use default value */ }

        JOptionPane.showMessageDialog(this, "Match Finished! Overall Winner: " + winnerName, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        // Call full state restoration after showing the message.
        restoreInitialState();
    }

    @Override
    public void restoreInitialState() {
        // Resets all counters to zero or initial state.
        restoreInitialScoreAndFouls();
        updateRoundWins(0, 0);
        updateRoundNumber(1);
        timePanel.getMatchScore().setText("0");

        // Reset styles and colors
        redPanel.getCompetitorScore().setBackground(TDKScoreUtils.RED_COLOR);
        bluePanel.getCompetitorScore().setBackground(TDKScoreUtils.BLUE_COLOR);
        timePanel.getBreackName().setOpaque(false);
        timePanel.getBreackName().setBackground(Color.BLACK);

        // Chronometer logic
        // chronometer.restartTime(chronometer.getMatchTime());
    }

    // --- View Auxiliary Methods (UI) ---

    private void restoreInitialScoreAndFouls() {
        // Resets only scores and fouls, useful at the start of each round.
        updateMainScore(0, 0);
        updateFouls(0, 0);
        // HeadKickCount must also be reset if they are per round.
        redPanel.getHeadKickCount().setText("0");
        bluePanel.getHeadKickCount().setText("0");
    }
}
