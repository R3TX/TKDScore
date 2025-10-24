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
            if(StringUtils.isNoneEmpty(label.getName())) {
                String input;
                switch (label.getName()) {
                    case "blueName":
                        input = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NAME, TDKScoreUtils.NAME_REGEX, label.getText());
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.updateCompetitorName(input, PlayerColor.BLUE);
                        }
                    case "redName":
                        input = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NAME, TDKScoreUtils.NAME_REGEX, label.getText());
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.updateCompetitorName(input, PlayerColor.RED);
                        }
                        break;

                    case "blueScore":
                        input=TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE, TDKScoreUtils.NUMERIC_REGEX, label.getText());
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.setManualRoundScore(PlayerColor.BLUE, Integer.parseInt(input));
                        }
                    case "redScore":
                        input=TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE, TDKScoreUtils.NUMERIC_REGEX, label.getText());
                        if (StringUtils.isNoneEmpty(input)) {
                            matchController.setManualRoundScore(PlayerColor.RED, Integer.parseInt(input));
                        }
                        break;

                    case "round":
                        String blueWins = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para las victorias de azul", TDKScoreUtils.NUMERIC_REGEX, "0");
                        String redWins = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para las victorias de rojo", TDKScoreUtils.NUMERIC_REGEX, "0");
                        if (StringUtils.isNoneEmpty(blueWins) && StringUtils.isNoneEmpty(redWins)) {
                            // 📞 Llama al Controller. ELIMINAR manipulación directa de jLabelList
                            matchController.setRoundWins(Integer.parseInt(blueWins), Integer.parseInt(redWins));
                        }
                    case "match":
                        input = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC, TDKScoreUtils.NUMERIC_REGEX, label.getText());
                        if (StringUtils.isNoneEmpty(input)) {
                            // 📞 Llama al Controller
                            matchController.setMatchNumber(Integer.parseInt(input));
                        }
                        break;
                    case "timer":
                        String matchTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round", TDKScoreUtils.TIME_REGEX, "");
                        String breakTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso", TDKScoreUtils.TIME_REGEX, "");

                        // 📞 Llama al Controller
                        if (StringUtils.isNoneEmpty(matchTime) && StringUtils.isNoneEmpty(breakTime)) {
                            matchController.updateAndResetMatchTimes(matchTime, breakTime);
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
