package view;

import model.entity.MatchEntity;

/**
 * Interface that defines the contract for the UI (JFrameColumns)
 * to be updated by the MatchController.
 * This ensures the Controller does not have direct access to Swing components.
 */
public interface ScoreboardView {

    // --- Métodos de Actualización del Marcador ---

    /**
     * Updates the main score display for both competitors in the active round.
     * @param redScore The current total score for the red competitor.
     * @param blueScore The current total score for the blue competitor.
     */
    void updateMainScore(int redScore, int blueScore);

    /**
     * Updates the Gam-Jeom (Fouls) display for both competitors.
     * @param redFouls The current number of fouls for the red competitor.
     * @param blueFouls The current number of fouls for the blue competitor.
     */
    void updateFouls(int redFouls, int blueFouls);

    /**
     * Updates the display of the timer/chronometer.
     * @param time The time string (e.g., "1:30").
     */
    void updateTimerDisplay(String time);

    /**
     * Updates the display showing the current round number.
     * @param roundNumber The current active round number (1, 2, or 3).
     */
    void updateRoundNumber(int roundNumber);

    /**
     * Updates the display showing the total round wins for the match.
     * @param redWins Total rounds won by the red competitor.
     * @param blueWins Total rounds won by the blue competitor.
     */
    void updateRoundWins(int redWins, int blueWins);

    // --- Métodos de Flujo de Juego y Estado ---

    /**
     * Notifies the view that a new match has started and provides the initial data.
     * @param match The MatchEntity created by the service.
     */
    void onMatchStarted(MatchEntity  match);

    /**
     * Notifies the view that a round has concluded and identifies the winner of that round.
     * The view should update the round wins display and potentially flash the winner's score.
     * @param winnerId The ID of the competitor who won the round (or null/0 if a draw).
     * @param isBreakTime True if the view should now display the break timer/indicator.
     */
    void onRoundConcluded(Integer winnerId, boolean isBreakTime);

    /**
     * Notifies the view that the match is officially over and displays the final winner screen.
     * @param winnerId The ID of the overall match winner.
     */
    void onMatchConcluded(int winnerId);

    /**
     * Resets all visual elements (scores, fouls, round wins) to zero or initial state.
     */
    void restoreInitialState();

    /**
     * Updates names
     * @param redName
     * @param blueName
     */
    void updateNames(String redName,String blueName);
}