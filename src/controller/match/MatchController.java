package controller.match;

import controller.listeners.Chronometer;
import model.entity.*;
import model.service.CompetitorService;
import model.service.MatchService;
import model.service.RoundService;
import model.service.RoundStatisticsService;
import view.ScoreboardView;

import javax.swing.*;
import java.util.List;

public class MatchController implements ChronometerListener {
    private final MatchService matchService;
    private final RoundService roundService;
    private final RoundStatisticsService statsService;
    private final CompetitorService competitorService;
    private Chronometer chronometer;
    private final ScoreboardView scoreboardView;

    private MatchEntity currentMatch;
    private RoundEntity currentRound;
    private RoundStatisticsEntity blueStatics;
    private RoundStatisticsEntity redStatics;
    private boolean isBreakTime = false;

    //check if delete later
    private int currentRoundNumber = 1;
    private int currentMatchNumber = 1;

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
        CompetitorEntity competitorRed = competitorService.save(new CompetitorEntity("RED", PlayerColor.RED.name()));
        CompetitorEntity competitorBlue = competitorService.save(new CompetitorEntity("BLUE", PlayerColor.BLUE.name()));

        //TODO make match number and round Number more dynamics here
        currentMatch = matchService.createNewMatch(currentMatchNumber, competitorRed.getuId(), competitorBlue.getuId());
        currentRound = roundService.startNewRound(currentMatch.getuId(), currentRoundNumber);
        redStatics = statsService.createStats(currentRound.getRoundId(), competitorRed.getuId());
        blueStatics = statsService.createStats(currentRound.getRoundId(), competitorBlue.getuId());
        restoreViewValues();
    }

    public void restoreViewValues() {
        String redName = currentMatch.getRedCompetitor().getName();
        String blueName = currentMatch.getBlueCompetitor().getName();
        //scoreboardView.updateTimerDisplay(chronometer.getCurrentTimeDisplay());
        scoreboardView.updateMainScore(redStatics.getBaseScore(), blueStatics.getBaseScore());
        scoreboardView.updateFouls(redStatics.getGamJeomFouls(), blueStatics.getGamJeomFouls());
        scoreboardView.updateHeadKicks(redStatics.getHeadKicks(), blueStatics.getHeadKicks());
        scoreboardView.updateRoundWins(currentMatch.getRedWonRounds(), currentMatch.getBlueWonRounds());
        scoreboardView.updateNames(redName, blueName);
        scoreboardView.updateRoundNumber(currentRound.getRoundNumber());
        scoreboardView.updateMatchNumber(currentMatch.getMatchNumber());
        scoreboardView.restoreWinnerColor();

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

        if (redStatics == null || blueStatics == null) return;
        // 1. Obtener los datos del modelo (RoundStatisticsEntity)
        // 2. Actualizar el Score y los Fouls
        scoreboardView.updateMainScore(redStatics.getBaseScore(), blueStatics.getBaseScore());
        scoreboardView.updateFouls(redStatics.getGamJeomFouls(), blueStatics.getGamJeomFouls());
        scoreboardView.updateHeadKicks(redStatics.getHeadKicks(), blueStatics.getHeadKicks());
    }


    // =================================================================
    // REFACTORIZACIÓN DE MÉTODOS EXISTENTES
    // =================================================================

    // =================================================================
    // --- new methods for KeyListener ---
    // =================================================================


    public void registerScore(PlayerColor playerColor, int points) {
        if (!chronometer.isRunning() || isBreakTime) {
            return; // if time is running and is not break time a point is marked
        }
        RoundStatisticsEntity competitor = PlayerColor.BLUE.equals(playerColor) ? blueStatics : redStatics;

        // La lógica de 3 puntos (cabeza) debe ser manejada en el Listener o aquí si se usa un flag.
        // Asumiendo que 'points' ya es el total (e.g., 3 para cabeza).
        statsService.registerScore(competitor, points, points == 3);
        updateScoreboard(); // Notificar a la vista
    }

    public void registerGamJeom(PlayerColor playerColor) {
        if (isBreakTime) return; // No faltas en descanso

        boolean gamjeonForBlue = PlayerColor.BLUE.equals(playerColor);

        // 1. Penalizar al Competidor A (A recibe Gam-Jeom)
        RoundStatisticsEntity penalizedId = gamjeonForBlue ? blueStatics : redStatics;
        statsService.registerGamJeom(penalizedId);

        // 2. Otorgar punto al Competidor B (B recibe el punto)
        RoundStatisticsEntity pointsCompetitorId = gamjeonForBlue ? redStatics : blueStatics;
        statsService.registerScore(pointsCompetitorId, 1, false);

        updateScoreboard(); // Notificar a la vista

        // 3. Verificar si el límite de faltas fue alcanzado (5)
        if (penalizedId.getGamJeomFouls() >= 5) {
            handleGamJeomLimit(gamjeonForBlue?PlayerColor.RED:PlayerColor.BLUE); // El ganador es el otro competidor
        }
    }

    public void decreaseGamJeom(PlayerColor playerColor) {
        if (isBreakTime) return; // No faltas en descanso

        // 1. Despenalizar al Competidor A (A recibe Gam-Jeom)
        if (PlayerColor.BLUE.equals(playerColor)) {
            statsService.decreaseGamJeom(blueStatics, redStatics);
        } else {
            statsService.decreaseGamJeom(redStatics, blueStatics);
        }
        updateScoreboard(); // Notificar a la vista
    }

    public void toggleTimer() {
        chronometer.startStopTimer();
    }

    public void requestFullMatchReset() {
        int confirmDialog = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas reiniciar TODO el partido?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmDialog == JOptionPane.YES_OPTION) {
            startNewMatch(); // Lógica de la capa de negocio (limpiar DB/estado)
            restoreViewValues(); // Lógica de la capa de la vista (limpiar JLabels)
        }
    }

    public void decreaseRoundScore(PlayerColor playerColor) {
        if (!chronometer.isRunning()) {
            RoundStatisticsEntity competitor = PlayerColor.BLUE.equals(playerColor) ? blueStatics : redStatics;
            statsService.decreaseScore(competitor);
        }
        updateScoreboard(); // Delegación a la vista
    }


    // --- Nuevos métodos expuestos para MouseListener ---

    public void manualUpdateRoundNumber(int roundNumber) {
        currentRound.setRoundNumber(roundNumber);
        roundService.updateRound(currentRound);
        scoreboardView.updateRoundNumber(roundNumber);
    }

    public void updateCompetitorName(String name, PlayerColor playerColor) {
        CompetitorEntity competitor = PlayerColor.BLUE.equals(playerColor) ? currentMatch.getBlueCompetitor() : currentMatch.getRedCompetitor();
        competitor.setName(name);
        competitorService.update(competitor);

        // Actualizar la vista mediante el método de la interfaz
        String redName = currentMatch.getRedCompetitor().getName();
        String blueName = currentMatch.getBlueCompetitor().getName();
        scoreboardView.updateNames(redName, blueName);
    }

    public void setManualRoundScore(PlayerColor playerColor, int newScore) {
        RoundStatisticsEntity competitor = PlayerColor.BLUE.equals(playerColor) ? blueStatics : redStatics;
        statsService.setManualScore(competitor, newScore);

        updateScoreboard(); // Delegación a la vista
    }


    public void setManualWin(PlayerColor playerColor, int score) {
        if (PlayerColor.BLUE.equals(playerColor)) {
            currentMatch.setBlueWonRounds(score);
        } else {
            currentMatch.setRedWonRounds(score);
        }
        scoreboardView.updateRoundWins(currentMatch.getRedWonRounds(), currentMatch.getBlueWonRounds());
        if (score >= 2) {
            handleMatchWin(playerColor);
        }
    }

    public void setManualRoundWins(int blueWins, int redWins) {
        // Lógica de negocio: actualizar conteo de rondas ganadas (puede ser MatchService)

        if (blueWins > -1) {
            currentMatch.setBlueWonRounds(blueWins);
        }
        if (redWins > -1) {
            currentMatch.setRedWonRounds(redWins);
        }
        matchService.updateMatch(currentMatch);
        // Actualizar la vista
        scoreboardView.updateRoundWins(redWins, blueWins);

        // Verificar si se alcanzó el límite de match
        if (blueWins >= 2 || redWins >= 2) {
            handleMatchWin(checkWinner(blueWins,redWins));
        }
    }

    public void manualUpdateMatchNumber(int matchNumber) {
        // Lógica de negocio: MatchService.updateMatchNumber(currentMatch.getuId(), matchNumber);
        currentMatch.setMatchNumber(matchNumber);
        matchService.updateMatch(currentMatch);

        //UPdateview
        scoreboardView.updateMatchNumber(matchNumber);
    }

    public void manualUpdateAndResetMatchTimes(String matchTime, String breakTime) {
        chronometer.stopTime();
        if (matchTime.trim().isEmpty()) {
            matchTime = chronometer.getMatchTime();
        }
        if (breakTime.trim().isEmpty()) {
            breakTime = chronometer.getBreakTime();
        }
        chronometer.setMatchTime(matchTime);
        chronometer.setBreakTime(breakTime);
        chronometer.restartTime(matchTime);
        isBreakTime = false;
    }
    //TODO check from here
    // --- Métodos de lógica movida de Listeners ---

    // Lógica movida de PropertyChangeListener cuando se alcanza el límite de Gam-Jeom (5)
    public void handleGamJeomLimit(PlayerColor playerColor) {

        PlayerColor matchWinner = setRoundWinner(playerColor);
        if(null != matchWinner){
            handleMatchWin(matchWinner);
            return;
        }

        //starts breack time
        startBreakTime();

        // El PropertyChangeListener de la falta ya reinició el contador a "0"
    }
    //starts breack time
    private void startBreakTime(){
        // Iniciar el descanso (Lógica de la vista)
        isBreakTime = true; // Actualiza el estado
        chronometer.restartTime(chronometer.getBreakTime());
        chronometer.startStopTimer(); // Inicia el tiempo de descanso
    }

    // Movido de TKDScorePropertyChangeListener
    private PlayerColor setRoundWinner(PlayerColor playerColor) {
        // Actualizar JLabels para reflejar el ganador de la ronda

        int redWins = currentMatch.getRedWonRounds();
        int blueWins = currentMatch.getBlueWonRounds();

        if (PlayerColor.BLUE.equals(playerColor)) {
            blueWins++;
            currentMatch.setBlueWonRounds(blueWins); // Asumimos setters existen
            currentRound.setRoundWinner(blueStatics.getCompetitor());
        } else {
            redWins++;
            currentMatch.setRedWonRounds(redWins); // Asumimos setters existen
            currentRound.setRoundWinner(redStatics.getCompetitor());
        }
        //update victory label in view
        scoreboardView.updateRoundWins(redWins, blueWins);

        // 3. Notificar a la vista que la ronda concluyó para manejar el estado visual
        scoreboardView.onRoundConcluded(playerColor, true); // Inicia el descanso (isBreakTime = true)

        if(blueWins>=2 || redWins >=2) {
            return checkWinner(blueWins, redWins);
        }
        return null;
    }

    //check which player has more points
    private PlayerColor checkWinner(int blueScore,int redScore){
        PlayerColor player;
        if (blueScore > redScore) {
            player = PlayerColor.BLUE;
        } else if (redScore > blueScore) {
            player = PlayerColor.RED;
        } else {
            player=null;
        }
        return player;
    }


    //TODO separate logic in methods
    // Lógica movida de Chronometer/PropertyChangeListener cuando el tiempo expira
    public void handleRoundTimeOut() {
        // 1. Detener el cronómetro (ya lo hace Chronometer antes de la llamada a este método)
        int blueScore = blueStatics.getBaseScore();
        int redScore = redStatics.getBaseScore();

        // 2. Determinar ganador de la ronda
        PlayerColor player = checkWinner(blueScore,redScore);

        if (null == player) {
            // Lógica de desempate movida del Listener
            int result = JOptionPane.showOptionDialog(null, "¿Quien es el ganador de la ronda por desempate?", "Ganador de Ronda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"AZUL", "ROJO"}, "AZUL");
            player = result == JOptionPane.OK_OPTION?PlayerColor.BLUE:PlayerColor.RED;
        }

        // 3. Registrar el ganador de la ronda (Lógica de negocio)
        PlayerColor matchWinner = setRoundWinner(player);
        //update current round
        saveCurrentRound();
        if(null != matchWinner){
            handleMatchWin(matchWinner);
            return;
        }

        //starts breack time
        startBreakTime();
    }

    //TODO create new round and statistics and on
    //Start methods for nextRound
    public void handleBreakTimeOut() {
        //cambiar estado del timer
        changeTimerStatus();
        //reestablecer score
        scoreboardView.restoreScoreFoulsHeadKick();
        //increase round
        currentRoundNumber+=1;
        // Lógica de la vista movida del Listener
        nextRound(); // Avanza el número de ronda
        //move round in front
        scoreboardView.updateRoundNumber(currentRoundNumber);

        // Notifica a la vista para restablecer el indicador de "Break" (asumiendo que nextRound no lo hace)
        scoreboardView.onRoundConcluded(null, false);


    }

    private void nextRound() {

        //creates a new round and statistics
        currentRound = roundService.startNewRound(currentMatch.getuId(),currentRoundNumber);
        redStatics = statsService.createStats(currentRound.getRoundId(),redStatics.getCompetitor().getuId());
        blueStatics = statsService.createStats(currentRound.getRoundId(),blueStatics.getCompetitor().getuId());
    }

    private void saveCurrentRound(){
        matchService.updateMatch(currentMatch);
        roundService.updateRound(currentRound);
        statsService.updateStatistics(redStatics);
        statsService.updateStatistics(blueStatics);
    }

    // Lógica movida de PropertyChangeListener cuando un jugador gana 2 rondas
    public void handleMatchWin(PlayerColor playerColor) {
        // 1. Determinar el ganador final (aunque ya lo sabemos, el diálogo puede ser para confirmación)
        int result = JOptionPane.showOptionDialog(null, "¿Quien es el ganador final?", "Ganador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"ROJO","AZUL"}, "AZUL");
        boolean isBlueWinner = result == JOptionPane.NO_OPTION;

        // 2. Lógica de negocio (registrar ganador en MatchEntity - requiere un ID)
        CompetitorEntity winnerId = isBlueWinner ? currentMatch.getBlueCompetitor() : currentMatch.getRedCompetitor();
        currentMatch.setMatchWinner(winnerId);
        matchService.updateMatch(currentMatch);
        //save finished round
        saveCurrentRound();

        //get statistics
        List<RoundEntity> roundList = roundService.findByMarchId(currentMatch.getuId());



        //create a new match
        startNewMatch();


        // 3. Lógica de la vista: reiniciar el estado
        //cambiar estado del timer
        changeTimerStatus();
        restoreViewValues();

        showWinnerMessage(isBlueWinner); // Muestra el mensaje de ganador
    }

    public void startNewMatch(){
        CompetitorEntity competitorRed = competitorService.save(new CompetitorEntity("RED", PlayerColor.RED.name()));
        CompetitorEntity competitorBlue = competitorService.save(new CompetitorEntity("BLUE", PlayerColor.BLUE.name()));
        currentMatch = matchService.createNewMatch(currentMatch.getMatchNumber()+1, competitorRed.getuId(), competitorBlue.getuId());
        currentRound = roundService.startNewRound(currentMatch.getuId(), 1);
        redStatics = statsService.createStats(currentRound.getRoundId(), competitorRed.getuId());
        blueStatics = statsService.createStats(currentRound.getRoundId(), competitorBlue.getuId());
    }




    private void changeTimerStatus(){
        isBreakTime = false; // Actualiza el estado del controlador a modo Round
        chronometer.restartTime(chronometer.getMatchTime());
        chronometer.stopTime();
    }

    // --- Métodos de manipulación de la Vista (deberían ser delegados a una ScoreboardView) ---



    // El método showWinnerMessage es un componente de la VISTA y debería ser movido.
    // Lo dejo en el Controller como simplificación por ahora.
    private void showWinnerMessage(Boolean isBlueWinner) {
        PlayerColor playerColor = isBlueWinner ? PlayerColor.BLUE : PlayerColor.RED;
        scoreboardView.onMatchConcluded(playerColor);
    }




    // --- Métodos existentes (mantener o adaptar) ---
    // ... getCurrentMatch(), getCurrentRoundId(), finishCurrentRound(), etc.
    // ya que la lógica de tiempo/estado se mueve al MatchController.


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

    // --- Scoring and Statistics Management ---

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

    public void setChronometerAndUpdateTimes(Chronometer chronometer) {
        setChronometer(chronometer);
        scoreboardView.updateTimerDisplay(chronometer.getCurrentTimeDisplay());
    }

    public boolean isBreakTime() {
        return isBreakTime;
    }

    public void setBreakTime(boolean breakTime) {
        isBreakTime = breakTime;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }
}