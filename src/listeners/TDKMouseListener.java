package listeners;

import org.apache.commons.lang3.StringUtils;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TDKMouseListener implements MouseListener {

    private Chronometer chronometer;

    public TDKMouseListener(Chronometer chronometer) {
        this.chronometer = chronometer;
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
                    case "match":
                    case "round":
                    case "blueGam":
                    case "redGam":
                        label.setText(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_NUMERIC, TDKScoreUtils.NUMERIC_REGEX, label.getText()));
                        break;
                    case "timer":
                        chronometer.setMatchTime(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round", TDKScoreUtils.TIME_REGEX, chronometer.getMatchTime()));
                        chronometer.setBreakTime(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso", TDKScoreUtils.TIME_REGEX, chronometer.getBreakTime()));
                        chronometer.restartTime(chronometer.getMatchTime());
                        chronometer.setIsBreakTime(false);
                        label.setText(TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME, TDKScoreUtils.TIME_REGEX, chronometer.getMatchTime()));
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
