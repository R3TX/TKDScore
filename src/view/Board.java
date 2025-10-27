package view;

import controller.listeners.Chronometer;
import controller.listeners.TDKMouseListener;
import controller.listeners.TKDKeyListener;
import controller.match.MatchController;
import model.repository.CompetitorDAO;
import model.repository.MatchDAO;
import model.repository.RoundDAO;
import model.repository.RoundStatisticsDAO;
import model.service.CompetitorService;
import model.service.MatchService;
import model.service.RoundService;
import model.service.RoundStatisticsService;
import utils.TDKScoreUtils;


public class Board {

    public Board() {

        // 2. Instanciar la Capa de Acceso a Datos (DAOs)
        CompetitorDAO competitorDAO = new CompetitorDAO();
        MatchDAO matchDAO = new MatchDAO(competitorDAO);
        RoundDAO roundDAO = new RoundDAO(matchDAO, competitorDAO);
        RoundStatisticsDAO statsDAO = new RoundStatisticsDAO(roundDAO, competitorDAO);

        // 3. Instanciar la Capa de Servicios, inyectando DAOs
        CompetitorService competitorService = new CompetitorService(competitorDAO);
        MatchService matchService = new MatchService(matchDAO, competitorDAO);
        RoundService roundService = new RoundService(roundDAO, matchDAO, competitorDAO);
        RoundStatisticsService statsService = new RoundStatisticsService(roundDAO, competitorDAO, statsDAO);

        MatchController matchController = null;
        JFrameColumns scoreboardView = null;
        scoreboardView = new JFrameColumns();


        matchController = new MatchController(
                matchService,
                roundService,
                statsService,
                competitorService,
                null,
                scoreboardView // ¡Aquí se inyecta la ScoreboardView!
        );
        String matchTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round", TDKScoreUtils.TIME_REGEX, "02:00");
        String breakTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso", TDKScoreUtils.TIME_REGEX, "01:00");
        Chronometer chronometer = new Chronometer(matchController, matchTime, breakTime);
        matchController.setChronometerAndUpdateTimes(chronometer);
        scoreboardView.addKeyListener(new TKDKeyListener(matchController));
        scoreboardView.setMouseListenerToComponent(new TDKMouseListener(matchController));
    }

}
