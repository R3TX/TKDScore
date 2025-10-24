package controller.match;

import controller.listeners.Chronometer;
import model.entity.*;
import model.service.CompetitorService;
import model.service.MatchService;
import model.service.RoundService;
import model.service.RoundStatisticsService;
import view.ScoreboardView;

import javax.swing.*;
import java.util.Optional;

public class MatchController implements ChronometerListener{
    private final MatchService matchService;
    private final RoundService roundService;
    private final RoundStatisticsService statsService;
    private final CompetitorService competitorService;
    private final Chronometer chronometer;
    private final ScoreboardView scoreboardView;

    private MatchEntity currentMatch;
    private RoundEntity currentRound;
    private boolean isBreakTime = false;

    public MatchController(MatchService matchService,
                           RoundService roundService,
                           RoundStatisticsService statsService,
                           CompetitorService competitorService,
                           Chronometer chronometer,
                           ScoreboardView scoreboardView) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.statsService = statsService;
        this.competitorService = competitorService;
        this.chronometer = chronometer;
        this.scoreboardView = scoreboardView;
    }
    public int getCurrentRoundId() {
        if (currentRound == null) {
            throw new IllegalStateException("No round is currently active.");
        }
        return currentRound.getRoundId();
    }
    // =================================================================
    // MÉTODOS DE ACTUALIZACIÓN DE LA VISTA (NUEVO MÉTODO CENTRAL)
    // =================================================================

    private void updateScoreboard() {

        if (currentMatch == null || currentRound == null) return;
        // 1. Obtener los datos del modelo (RoundStatisticsEntity)
        RoundStatisticsEntity redStats = statsService.getOrCreateStats(getCurrentRoundId(), currentMatch.getRedCompetitor().getuId());
        RoundStatisticsEntity blueStats = statsService.getOrCreateStats(getCurrentRoundId(), currentMatch.getBlueCompetitor().getuId());
        // 2. Actualizar el Score y los Fouls
        scoreboardView.updateMainScore(redStats.getTotalScore(), blueStats.getTotalScore());
        scoreboardView.updateFouls(redStats.getGamJeomFouls(), blueStats.getGamJeomFouls());
        scoreboardView.updateHeadKicks(redStats.getHeadKicks(),blueStats.getHeadKicks());
    }

    // Método para ser llamado por el cronómetro o un Timer de Swing
    public void updateTimerDisplay(String time) {
        scoreboardView.updateTimerDisplay(time);
    }
    public void updateRoundNumber(int roundNumber) {
        scoreboardView.updateRoundNumber(roundNumber);
    }

    // =================================================================
    // REFACTORIZACIÓN DE MÉTODOS EXISTENTES
    // =================================================================

    // --- new methods for KeyListener ---

    public void registerScore(PlayerColor playerColor, int points) {
        if (!chronometer.isRunning() || isBreakTime) {
            return; // if time is running and is not break time a point is marked
        }
        int competitorId = PlayerColor.BLUE.equals(playerColor) ? currentMatch.getBlueCompetitor().getuId() : currentMatch.getRedCompetitor().getuId();

        // La lógica de 3 puntos (cabeza) debe ser manejada en el Listener o aquí si se usa un flag.
        // Asumiendo que 'points' ya es el total (e.g., 3 para cabeza).
        statsService.registerScore(getCurrentRoundId(), competitorId, points, points == 3);
        updateScoreboard(); // Notificar a la vista
    }

    public void registerGamJeom(PlayerColor playerColor) {
        if (isBreakTime) return; // No faltas en descanso

        boolean forBlue = PlayerColor.BLUE.equals(playerColor);

        // 1. Penalizar al Competidor A (A recibe Gam-Jeom)
        int penalizedId = forBlue ? currentMatch.getBlueCompetitor().getuId() : currentMatch.getRedCompetitor().getuId();
        statsService.registerGamJeom(getCurrentRoundId(), penalizedId);

        // 2. Otorgar punto al Competidor B (B recibe el punto)
        int pointsCompetitorId = forBlue ? currentMatch.getRedCompetitor().getuId() : currentMatch.getBlueCompetitor().getuId();
        statsService.registerScore(getCurrentRoundId(), pointsCompetitorId, 1, false);

        updateScoreboard(); // Notificar a la vista

        // 3. Verificar si el límite de faltas fue alcanzado (5)
        RoundStatisticsEntity penalizedStats = statsService.getOrCreateStats(getCurrentRoundId(), penalizedId);
        if (penalizedStats.getGamJeomFouls() >= 5) {
            handleGamJeomLimit(!forBlue); // El ganador es el otro competidor
        }
    }

    public void decreaseGamJeom(PlayerColor playerColor) {
        if (isBreakTime) return; // No faltas en descanso

        boolean forBlue = PlayerColor.BLUE.equals(playerColor);

        // 1. Despenalizar al Competidor A (A recibe Gam-Jeom)
        int penalizedId = forBlue ? currentMatch.getBlueCompetitor().getuId() : currentMatch.getRedCompetitor().getuId();
        statsService.decreaseGamJeom(getCurrentRoundId(), penalizedId);

        // 2. Otorgar punto al Competidor B (B recibe el punto)
        int pointsCompetitorId = forBlue ? currentMatch.getRedCompetitor().getuId() : currentMatch.getBlueCompetitor().getuId();
        statsService.decreaseScore(getCurrentRoundId(), pointsCompetitorId, 1);

        updateScoreboard(); // Notificar a la vista

        // 3. Verificar si el límite de faltas fue alcanzado (5)
        RoundStatisticsEntity penalizedStats = statsService.getOrCreateStats(getCurrentRoundId(), penalizedId);
        if (penalizedStats.getGamJeomFouls() >= 5) {
            handleGamJeomLimit(!forBlue); // El ganador es el otro competidor
        }
    }

    public void toggleTimer() {
        chronometer.startStopTimer();
    }

    public void requestFullMatchReset() {
        int confirmDialog = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas reiniciar TODO el partido?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmDialog == JOptionPane.YES_OPTION) {
            resetMatch(); // Lógica de la capa de negocio (limpiar DB/estado)
            scoreboardView.restoreInitialState(); // Lógica de la capa de la vista (limpiar JLabels)
        }
    }

    /**
     * Revisar y ajustar
     */
    /*
    private void resetScoreboardView() {
        for (int i = 0; i < jLabelList.size(); i++) {
            jLabelList.get(i).setText(initialLabelTexts.get(i));
        }
        jLabelList.get(1).setBackground(Color.BLUE); // Usar constantes de color si existen
        jLabelList.get(5).setBackground(Color.RED);
        jLabelList.get(17).setBackground(Color.BLACK); // Asumiendo que 17 es el label de 'Break'
        chronometer.restartTime(chronometer.getMatchTime());
    }
*/
    // --- Nuevos métodos expuestos para MouseListener ---

    public void updateCompetitorName(String name, PlayerColor playerColor) {
        CompetitorEntity competitor = PlayerColor.BLUE.equals(playerColor) ? currentMatch.getBlueCompetitor() : currentMatch.getRedCompetitor();
        competitor.setName(name);
        competitorService.save(competitor);


        // Actualizar la vista mediante el método de la interfaz
        String redName = currentMatch.getRedCompetitor().getName();
        String blueName = currentMatch.getBlueCompetitor().getName();
        scoreboardView.updateNames(redName, blueName);
    }

    public void setManualRoundScore(PlayerColor playerColor, int newScore) {
        CompetitorEntity competitor = PlayerColor.BLUE.equals(playerColor) ? currentMatch.getBlueCompetitor() : currentMatch.getRedCompetitor();
        statsService.setManualScore(getCurrentRoundId(), competitor.getuId(), newScore);

        updateScoreboard(); // Delegación a la vista
    }

    public void decreaseRoundScore(PlayerColor playerColor) {
        if (!chronometer.isRunning()) {
            CompetitorEntity competitor = PlayerColor.BLUE.equals(playerColor) ? currentMatch.getBlueCompetitor() : currentMatch.getRedCompetitor();
            statsService.decreaseScore(getCurrentRoundId(), competitor.getuId(), 1);
        }
        updateScoreboard(); // Delegación a la vista
    }

    public void setRoundWins(int blueWins, int redWins) {
        // Lógica de negocio: actualizar conteo de rondas ganadas (puede ser MatchService)

        currentMatch.setBlueWonRounds(blueWins);
        currentMatch.setRedWonRounds(redWins);
        matchService.updateMatch(currentMatch);
        // Actualizar la vista
        scoreboardView.updateRoundWins(redWins, blueWins);

        // Verificar si se alcanzó el límite de match
        if (blueWins >= 2 || redWins >= 2) {
            handleMatchWin(PlayerColor.BLUE);
        }
    }

    public void setMatchNumber(int matchNumber) {
        // Lógica de negocio: MatchService.updateMatchNumber(currentMatch.getuId(), matchNumber);
        currentMatch.setMatchNumber(matchNumber);
        matchService.updateMatch(currentMatch);

        //UPdateview
        scoreboardView.updateMatchNumber(matchNumber);
    }

    public void updateAndResetMatchTimes(String matchTime, String breakTime) {
        if (matchTime.trim().isEmpty()) {
            matchTime = chronometer.getMatchTime();
        }
        if (breakTime.trim().isEmpty()) {
            breakTime = chronometer.getBreakTime();
        }
        chronometer.setMatchTime(matchTime);
        chronometer.setBreakTime(breakTime);
        chronometer.restartTime(matchTime);
        isBreakTime=false;
    }

    // --- Métodos de lógica movida de Listeners ---

    public void handleTimeOut() {
        if (!isBreakTime) {
            // 📞 Llama al Controller: Fin de Ronda (por tiempo)
            handleRoundTimeOut();
        } else {
            // 📞 Llama al Controller: Fin de Descanso
            handleBreakTimeOut();
        }
    }

    // Lógica movida de Chronometer/PropertyChangeListener cuando el tiempo expira
    public void handleRoundTimeOut() {
        // 1. Detener el cronómetro (ya lo hace Chronometer antes de la llamada a este método)
        int blueScore = currentRound.getFinalBlueScore();
        int redScore = currentRound.getFinalRedScore();

        // 2. Determinar ganador de la ronda
        boolean isBlueWinner;
        if (blueScore > redScore) {
            isBlueWinner = true;
        } else if (redScore > blueScore) {
            isBlueWinner = false;
        } else {
            // Lógica de desempate movida del Listener
            int result = JOptionPane.showOptionDialog(null, "¿Quien es el ganador de la ronda por desempate?", "Ganador de Ronda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"AZUL", "ROJO"}, "AZUL");
            isBlueWinner = result == JOptionPane.OK_OPTION;
        }

        // 3. Registrar el ganador de la ronda (Lógica de negocio)
        setRoundWinner(isBlueWinner);

        // 4. Iniciar el tiempo de descanso (Lógica de la vista, ya manejada en Chronometer)
        isBreakTime = true;
        // La vista es notificada a través de setRoundWinner -> scoreboardView.onRoundConcluded(winnerId, true);
        chronometer.restartTime(chronometer.getBreakTime());
        chronometer.startStopTimer(); // Inicia el tiempo de descanso
    }

    public void handleBreakTimeOut() {
        // Lógica de la vista movida del Listener
        restoreScore(); // Reinicia el marcador de la ronda
        nextRound(); // Avanza el número de ronda

        isBreakTime = false; // Actualiza el estado del controlador a modo Round

        // Notifica a la vista para restablecer el indicador de "Break" (asumiendo que nextRound no lo hace)
        scoreboardView.onRoundConcluded(null, false);

        chronometer.restartTime(chronometer.getMatchTime());
        chronometer.startStopTimer(); // Inicia el tiempo de la nueva ronda
    }

    // Lógica movida de PropertyChangeListener cuando se alcanza el límite de Gam-Jeom (5)
    public void handleGamJeomLimit(boolean penalizedIsBlue) {
        if (!penalizedIsBlue) {
            setRoundWinner(true); // Rojo penalizado -> Azul gana
        } else {
            setRoundWinner(false); // Azul penalizado -> Rojo gana
        }

        // Iniciar el descanso (Lógica de la vista)
        isBreakTime = true; // Actualiza el estado
        chronometer.restartTime(chronometer.getBreakTime());
        chronometer.startStopTimer(); // Inicia el tiempo de descanso

        // El PropertyChangeListener de la falta ya reinició el contador a "0"
    }

    // Lógica movida de PropertyChangeListener cuando un jugador gana 2 rondas
    public void handleMatchWin(PlayerColor playerColor) {
        // 1. Determinar el ganador final (aunque ya lo sabemos, el diálogo puede ser para confirmación)
        int result = JOptionPane.showOptionDialog(null, "¿Quien es el ganador final?", "Ganador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"AZUL", "ROJO"}, "AZUL");
        boolean isBlueWinner = result == JOptionPane.YES_OPTION;

        // 2. Lógica de negocio (registrar ganador en MatchEntity - requiere un ID)
        CompetitorEntity winnerId = isBlueWinner ? currentMatch.getBlueCompetitor() : currentMatch.getRedCompetitor();
        currentMatch.setMatchWinner(winnerId);
        matchService.updateMatch(currentMatch);
        //finishMatch(currentMatch.getuId(), winnerId);

        // 3. Lógica de la vista: reiniciar el estado
        /*
        chronometer.restartTime(chronometer.getMatchTime());
        chronometer.setIsBreakTime(false);
        jLabelList.get(9).setText(String.valueOf(Integer.parseInt(jLabelList.get(9).getText()) + 1)); // increase match time
        jLabelList.get(11).setText("1"); // reset round number
        jLabelList.get(14).setText("0"); // restart won score for blue
        jLabelList.get(16).setText("0"); // restart won score for red

         */
        restoreScore(); // Reinicia el puntaje y faltas de la ronda

        showWinnerMessage(isBlueWinner); // Muestra el mensaje de ganador
    }

    // --- Métodos de manipulación de la Vista (deberían ser delegados a una ScoreboardView) ---

    // Movido de TKDScorePropertyChangeListener
    private void setRoundWinner(boolean isBlueWinner) {
        // Actualizar JLabels para reflejar el ganador de la ronda

        int redWins=currentMatch.getRedWonRounds();
        int blueWins=currentMatch.getBlueWonRounds();

        if (isBlueWinner) {
            blueWins++;
            currentMatch.setBlueWonRounds(blueWins); // Asumimos setters existen
        } else {
            redWins++;
            currentMatch.setRedWonRounds(redWins); // Asumimos setters existen
        }
        scoreboardView.updateRoundWins(redWins, blueWins);

        // 3. Notificar a la vista que la ronda concluyó para manejar el estado visual
        Integer winnerId = isBlueWinner ? currentMatch.getBlueCompetitor().getuId() : currentMatch.getRedCompetitor().getuId();
        scoreboardView.onRoundConcluded(winnerId, true); // Inicia el descanso (isBreakTime = true)

        // 4. Lógica de control de tiempo
        chronometer.restartTime(chronometer.getBreakTime());
        isBreakTime=true;

/*
        JLabel roundWinnerLabel = jLabelList.get(isBlueWinner ? 1 : 5);
        roundWinnerLabel.setBackground(Color.YELLOW);

        JLabel winnerWinCountLabel = jLabelList.get(isBlueWinner ? 14 : 16);
        int newWinCount = Integer.parseInt(winnerWinCountLabel.getText()) + 1;
        winnerWinCountLabel.setText(String.valueOf(newWinCount));

        // Actualizar el score principal (LABEL 1 y 5) con el nuevo contador de rondas ganadas
        roundWinnerLabel.setText(String.valueOf(newWinCount));

        // Restaurar el perdedor (etiqueta 1 y 5)
        JLabel roundLoserLabel = jLabelList.get(!isBlueWinner ? 1 : 5);
        JLabel loserWinCountLabel = jLabelList.get(!isBlueWinner ? 14 : 16);
        roundLoserLabel.setText(loserWinCountLabel.getText());

        // Iniciar el descanso
        chronometer.restartTime(chronometer.getBreakTime());
        chronometer.setIsBreakTime(true);

        */
    }

    // El método showWinnerMessage es un componente de la VISTA y debería ser movido.
    // Lo dejo en el Controller como simplificación por ahora.
    private void showWinnerMessage(Boolean isBlueWinner) {
        int winnerId = isBlueWinner ? currentMatch.getBlueCompetitor().getuId() : currentMatch.getRedCompetitor().getuId();
        scoreboardView.onMatchConcluded(winnerId);
    }

    private void restoreScore() {
        // Reiniciar puntaje y faltas de la ronda
        /*
        jLabelList.get(1).setBackground(Color.BLUE);
        jLabelList.get(5).setBackground(Color.RED);
        jLabelList.get(1).setText("0");
        jLabelList.get(5).setText("0");
        jLabelList.get(3).setText("0"); // Gam-Jeom Azul
        jLabelList.get(7).setText("0"); // Gam-Jeom Rojo

         */
    }

    private void nextRound() {
        /*
        int currentRound = Integer.parseInt(jLabelList.get(11).getText()) + 1;

        // Lógica de inicio de partido (si ambas victorias son 0, la ronda es 1)
        if(Integer.parseInt(jLabelList.get(14).getText())==0 && Integer.parseInt(jLabelList.get(16).getText())==0) {
            currentRound = 1;
        }
        jLabelList.get(11).setText(String.valueOf(currentRound));

         */
    }





    // --- Métodos existentes (mantener o adaptar) ---
    // ... getCurrentMatch(), getCurrentRoundId(), finishCurrentRound(), etc.
    // NOTE: El método registerScoreEvent/registerGamJeomEvent en el MatchController ya no son necesarios,
    // ya que la lógica de tiempo/estado se mueve al MatchController.


    // --- Match Flow Management ---

    /**
     * Starts a new match and the first round.
     *
     * @param matchNumber      The number of the new match.
     * @param redCompetitorId  The ID of the red competitor.
     * @param blueCompetitorId The ID of the blue competitor.
     * @return The newly created MatchEntity.
     */
    public MatchEntity startNewMatch(int matchNumber, int redCompetitorId, int blueCompetitorId) {
        // 1. Create the Match
        MatchEntity match = matchService.createNewMatch(matchNumber, redCompetitorId, blueCompetitorId);
        // 2. Set the newly created match as the current active match
        this.currentMatch = match;
        // 3. Automatically start the first Round
        // This is a common business logic requirement: Match start = Round 1 start.
        startNewRound(match.getuId(), 1);

        return match;
    }

    /**
     * Provides access to the MatchEntity that is currently active.
     *
     * @return The currently active MatchEntity.
     * @throws IllegalStateException if no match is currently active.
     */
    public MatchEntity getCurrentMatch() {
        if (currentMatch == null) {
            // Es buena práctica lanzar una excepción si se intenta acceder a un estado no inicializado.
            throw new IllegalStateException("No match is currently active. Call startNewMatch first.");
        }
        return currentMatch;
    }

    /**
     * Starts a new sequential round (e.g., Round 2 after Round 1 finishes).
     *
     * @param matchId     The ID of the current match.
     * @param roundNumber The number of the round (2 or 3).
     * @return The newly created RoundEntity.
     */
    public RoundEntity startNewRound(int matchId, int roundNumber) {
        RoundEntity newRound = roundService.startNewRound(matchId, roundNumber);
        this.currentRound = newRound;
        return newRound;
    }


/*
    public RoundStatisticsEntity registerScoreEvent(int competitorId, int points, boolean isHeadKick) {
        return registerScore(getCurrentRoundId(), competitorId, points, isHeadKick);
    }
*/
    /*
    public RoundStatisticsEntity registerGamJeomEvent(int competitorId) {
        return registerGamJeom(getCurrentRoundId(), competitorId);
    }
*/
    public RoundEntity finishCurrentRound() {
        RoundEntity finishedRound = finishRound(getCurrentRoundId());

        // Determina la siguiente acción (Match o Round)
        // ... Lógica que determina si el partido termina o si se pasa al descanso ...
        // (Esta lógica compleja idealmente residiría en MatchService, pero la simplificaremos aquí)

        // 🚨 IMPORTANTE: Esto es lo que el Listener necesita saber para parar el tiempo.
        return finishedRound;
    }

    public RoundEntity advanceToNextRound() {
        // Lógica de si hay un ganador de partido, o si toca ronda 2, 3, etc.
        // Por simplificación, asumo que el servicio puede manejar el conteo de la próxima ronda.
        int nextRoundNumber = this.currentRound.getRoundNumber() + 1;

        // Simulación de que el match no ha terminado.
        return startNewRound(currentMatch.getuId(), nextRoundNumber);
    }

    public void resetMatch() {
        // Lógica de negocio: borrar RoundStatistics, Round, y Match si es necesario.
        // Aquí solo limpiamos el estado del controlador.
        this.currentMatch = null;
        this.currentRound = null;
    }

    /**
     * Finalizes the current match, setting the official winner.
     *
     * @param matchId  The ID of the match.
     * @param winnerId The ID of the winning competitor.
     * @return The updated MatchEntity.
     */
    public MatchEntity finishMatch(int matchId, int winnerId) {
        MatchEntity finishedMatch = matchService.concludeMatch(matchId, winnerId);
        return finishedMatch;
    }

    // --- Scoring and Statistics Management ---

    /**
     * Registers a score event for a competitor in the current round.
     *
     * @param roundId      The current active round ID.
     * @param competitorId The ID of the scoring competitor.
     * @param points       The base points scored (1, 2, or 3).
     * @param isHeadKick   True if the score was a head kick.
     * @return The updated RoundStatisticsEntity.
     */
    /*
    public RoundStatisticsEntity registerScore(int roundId, int competitorId, int points, boolean isHeadKick) {
        return statsService.registerScore(roundId, competitorId, points, isHeadKick);
    }
*/
    /**
     * Registers a Gam-Jeom foul for a competitor in the current round.
     *
     * @param roundId      The current active round ID.
     * @param competitorId The ID of the penalized competitor.
     * @return The updated RoundStatisticsEntity.
     */
    /*
    public RoundStatisticsEntity registerGamJeom(int roundId, int competitorId) {
        return statsService.registerGamJeom(roundId, competitorId);
    }
*/
    /**
     * Finalizes the current round based on the scores accumulated in RoundStatistics.
     * NOTE: Actual score calculation (who wins the round) should be done here
     * or delegated back to a service layer method that analyzes the current stats.
     *
     * @param roundId The ID of the round to finish.
     */
    public RoundEntity finishRound(int roundId) {
        MatchEntity match = getCurrentMatch();
        // Business Logic Example (Simplified):
        // 1. Fetch R.S. for Red and Blue.
        // 2. Compare total scores.
        // 3. Determine winnerId and final scores.

        int redId = match.getRedCompetitor().getuId();
        int blueId = match.getBlueCompetitor().getuId();

        RoundStatisticsEntity redStats = statsService.getOrCreateStats(roundId, redId);
        RoundStatisticsEntity blueStats = statsService.getOrCreateStats(roundId, blueId);

        int redScore = redStats.getTotalScore();
        int blueScore = blueStats.getTotalScore();

        Integer winnerId = null;
        if (redScore > blueScore) {
            winnerId = redId;
        } else if (blueScore > redScore) {
            winnerId = blueId;
        }

        return roundService.concludeRound(roundId, redScore, blueScore, winnerId);
    }

    /**
     * Retrieves the competitor's name based on their ID, primarily for display in the View.
     * This decouples the View (JFrameColumns) from the Data Access layer.
     *
     * @param competitorId The ID of the competitor.
     * @return The competitor's name (String).
     * @throws RuntimeException if the competitor is not found.
     */
    public String getCompetitorName(int competitorId) {
        Optional<CompetitorEntity> competitor = competitorService.findById(competitorId);

        if (competitor.isEmpty()) {
            //in case there is no competitor
            throw new RuntimeException("Competitor with ID " + competitorId + " not found.");
        }

        return competitor.get().getName();
    }

    @Override
    public void onTimeUpdate(String time) {
        scoreboardView.updateTimerDisplay(time); // Simplemente delega a la vista
    }

    @Override
    public void onTimeOut() {
        if (!isBreakTime) {
            // Fin de Ronda (por tiempo)
            handleRoundTimeOut();
        } else {
            // Fin de Descanso
            handleBreakTimeOut();
        }
    }

    public boolean isBreakTime() {
        return isBreakTime;
    }

    public void setBreakTime(boolean breakTime) {
        isBreakTime = breakTime;
    }
}