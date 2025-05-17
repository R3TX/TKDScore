package listeners;

import org.apache.commons.lang3.StringUtils;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class TKDScorePropertyChangeListener implements PropertyChangeListener {
    List<JLabel> jLabelList;
    private Chronometer chronometer;


    public TKDScorePropertyChangeListener(List<JLabel> jLabelList, Chronometer chronometer) {
        this.jLabelList = jLabelList;
        this.chronometer = chronometer;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        if(source instanceof JLabel) {
            JLabel sourceLabel = (JLabel) source;
            if (StringUtils.isNoneEmpty(sourceLabel.getName())) {
                switch (sourceLabel.getName()) {
                    case "timer":
                        String timeout = (String) evt.getNewValue();
                        if ("0:00".equals(timeout)) {
                            if (!Chronometer.IS_BREAK_TIME) {
                                //validates that red wins
                                if (Integer.parseInt(jLabelList.get(1).getText()) < Integer.parseInt(jLabelList.get(5).getText())) {
                                    setRoundWinner(false);
                                } else if (Integer.parseInt(jLabelList.get(1).getText()) > Integer.parseInt(jLabelList.get(5).getText())) {
                                    setRoundWinner(true);
                                } else {
                                    int result = JOptionPane.showOptionDialog(null, "¿Quien es el ganador?", "Ganador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"AZUL", "ROJO"}, "AZUL");
                                    setRoundWinner(result == JOptionPane.OK_OPTION);
                                }
                            } else {
                                restoreScore();
                                nextRound();
                            }
                        }
                        break;
                    case "blueWin":
                        if(sourceLabel.getText().equals("2")){
                            chronometer.restartTime(chronometer.getMatchTime());
                            Chronometer.IS_BREAK_TIME=false;
                            jLabelList.get(9).setText(String.valueOf(Integer.parseInt(jLabelList.get(9).getText())+1));
                            jLabelList.get(11).setText("1");
                            jLabelList.get(14).setText("0");
                            restoreScore();
                            JOptionPane.showMessageDialog(null, "Gano azul");

                        }
                        break;
                    case "redWin":
                        if(sourceLabel.getText().equals("2")){
                            chronometer.restartTime(chronometer.getMatchTime());
                            Chronometer.IS_BREAK_TIME=false;
                            jLabelList.get(9).setText(String.valueOf(Integer.parseInt(jLabelList.get(9).getText())+1));
                            jLabelList.get(11).setText("1");
                            jLabelList.get(14).setText("0");
                            restoreScore();
                            JOptionPane.showMessageDialog(null, "Gano rojo");
                        }
                        break;
                    case "blueGam":
                        if(sourceLabel.getText().equals("5")){
                            chronometer.restartTime(chronometer.getBreakTime());
                            Chronometer.IS_BREAK_TIME=true;
                            sourceLabel.setText("0");
                            setRoundWinner(false);
                        }
                        break;
                    case "redGam":
                        if(sourceLabel.getText().equals("5")){
                            chronometer.restartTime(chronometer.getBreakTime());
                            Chronometer.IS_BREAK_TIME=true;
                            sourceLabel.setText("0");
                            setRoundWinner(true);
                        }
                        break;
                }
            }
        }

    }

    private void setRoundWinner( boolean isBlueWinner) {
        JLabel roundWinnerLabel = jLabelList.get(isBlueWinner ? 1 : 5);
        roundWinnerLabel.setBackground(Color.yellow);
        JLabel winnerLabel = jLabelList.get(isBlueWinner ? 14 : 16);
        JLabel loserLabel = jLabelList.get(!isBlueWinner ? 14 : 16);
        JLabel roundLoserLabel = jLabelList.get(!isBlueWinner ? 1 : 5);
        winnerLabel.setText(String.valueOf(Integer.parseInt(winnerLabel.getText()) + 1));
        roundWinnerLabel.setText(winnerLabel.getText());
        roundLoserLabel.setText(loserLabel.getText());
    }


    private void restoreScore() {
        jLabelList.get(1).setBackground(TDKScoreUtils.BLUE_COLOR);
        jLabelList.get(5).setBackground(TDKScoreUtils.RED_COLOR);
        jLabelList.get(1).setText("0");
        jLabelList.get(5).setText("0");
    }
    private void nextRound(){
        int currentRound=Integer.parseInt(jLabelList.get(11).getText())+1;
        jLabelList.get(11).setText(String.valueOf(currentRound));

    }

}
