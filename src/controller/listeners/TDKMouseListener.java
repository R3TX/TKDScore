package controller.listeners;

import controller.match.MatchController;
import model.entity.PlayerColor;
import org.apache.commons.lang3.StringUtils;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TDKMouseListener implements MouseListener {

    private final MatchController matchController;

    public TDKMouseListener(MatchController matchController) {
        this.matchController = matchController;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        // Verify what object was clicked
        if (source instanceof JLabel) {
            JLabel label = (JLabel) source;
            if (StringUtils.isNoneEmpty(label.getName())) {
                String input;
                switch (label.getName()) {
                    case "nameBLUE":
                        input = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NAME,
                                        TDKScoreUtils.NAME_REGEX,
                                        TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.updateCompetitorName(input, PlayerColor.BLUE);
                        }
                        break;
                    case "nameRED":
                        input = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NAME,
                                        TDKScoreUtils.NAME_REGEX,
                                        TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.updateCompetitorName(input, PlayerColor.RED);
                        }
                        break;
                    case "victoryScoreBLUE":
                        input = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE,
                                        TDKScoreUtils.VICTORY_NUMERIC_REGEX,
                                        TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.setManualWin(PlayerColor.BLUE, Integer.parseInt(input));
                        }
                        break;
                    case "victoryScoreRED":
                        input = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE,
                                        TDKScoreUtils.VICTORY_NUMERIC_REGEX,
                                        TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.setManualWin(PlayerColor.RED, Integer.parseInt(input));
                        }
                        break;

                    case "competitorScoreBLUE":
                        input = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE,
                                        TDKScoreUtils.SCORE_NUMERIC_REGEX,
                                        TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.setManualRoundScore(PlayerColor.BLUE, Integer.parseInt(input));
                        }
                        break;
                    case "competitorScoreRED":
                        input = TDKScoreUtils.getInputText(
                                TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE,
                                TDKScoreUtils.SCORE_NUMERIC_REGEX,
                                TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.setManualRoundScore(PlayerColor.RED, Integer.parseInt(input));
                        }
                        break;

                    case "roundScore":
                        String blueWins = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para las victorias de azul",
                                        TDKScoreUtils.VICTORY_NUMERIC_REGEX,
                                        "-1");
                        String redWins = TDKScoreUtils
                                .getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para las victorias de rojo",
                                        TDKScoreUtils.VICTORY_NUMERIC_REGEX,
                                        "-1");
                        String roundScore = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para el round ",
                                        TDKScoreUtils.ROUND_NUMERIC_REGEX,
                                        "1");
                        if (StringUtils.isNoneEmpty(blueWins) && StringUtils.isNoneEmpty(redWins)) {
                            //  Llama al Controller. ELIMINAR manipulación directa de jLabelList
                            matchController.setManualRoundWins(Integer.parseInt(blueWins), Integer.parseInt(redWins));
                            matchController.manualUpdateRoundNumber(Integer.parseInt(roundScore));
                        }
                        break;
                    case "matchScore":
                        input = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_NUMERIC,
                                        TDKScoreUtils.SCORE_NUMERIC_REGEX,
                                        TDKScoreUtils.extractTextFromHtml(label.getText()));
                        if (StringUtils.isNoneEmpty(input)) {
                            //  Llama al Controller
                            matchController.manualUpdateMatchNumber(Integer.parseInt(input));
                        }
                        break;
                    case "timeScore":
                        String matchTime = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round",
                                        TDKScoreUtils.TIME_REGEX,
                                        "");
                        String breakTime = TDKScoreUtils
                                .getInputText(
                                        TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso",
                                        TDKScoreUtils.TIME_REGEX,
                                        "");

                        //  Llama al Controller
                        if (StringUtils.isNoneEmpty(matchTime) && StringUtils.isNoneEmpty(breakTime)) {
                            matchController.manualUpdateAndResetMatchTimes(matchTime, breakTime);
                        }
                        break;
                }
            }

        }

    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
