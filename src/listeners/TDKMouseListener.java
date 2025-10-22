package listeners;

import org.apache.commons.lang3.StringUtils;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class TDKMouseListener implements MouseListener {

    private Chronometer chronometer;
    private List<JLabel> jLabelList;

    public TDKMouseListener(Chronometer chronometer, List<JLabel> jLabelList) {
        this.chronometer = chronometer;
        this.jLabelList = jLabelList;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        // Verify what object was clicked
        if (source instanceof JLabel) {
            JLabel label = (JLabel) source;
            if(StringUtils.isNoneEmpty(label.getName())) {
                switch (label.getName()) {
                    case "blueName":
                    case "redName":
                        label.setText(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NAME, TDKScoreUtils.NAME_REGEX, label.getText()));
                        break;

                    case "blueScore":
                    case "redScore":
                        label.setText(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE, TDKScoreUtils.NUMERIC_REGEX, label.getText()));
                        break;

                    case "round":
                        String blueWins = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para las victorias de azul", TDKScoreUtils.NUMERIC_REGEX, "0");
                        String redWins = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC + "\n para las victorias de rojo", TDKScoreUtils.NUMERIC_REGEX, "0");
                        jLabelList.get(14).setText(blueWins);
                        jLabelList.get(16).setText(redWins);
                    case "match":
                        label.setText(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC, TDKScoreUtils.NUMERIC_REGEX, label.getText()));
                        break;
                    case "timer":
                        chronometer.setMatchTime(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round", TDKScoreUtils.TIME_REGEX, chronometer.getMatchTime()));
                        chronometer.setBreakTime(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso", TDKScoreUtils.TIME_REGEX, chronometer.getBreakTime()));
                        chronometer.restartTime(chronometer.getMatchTime());
                        chronometer.setIsBreakTime(false);
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
