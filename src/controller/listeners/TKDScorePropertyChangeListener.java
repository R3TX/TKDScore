package controller.listeners;

import controller.match.MatchController;
import model.entity.PlayerColor;
import org.apache.commons.lang3.StringUtils;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


public class TKDScorePropertyChangeListener implements PropertyChangeListener {
    List<JLabel> jLabelList;
    private final Chronometer chronometer;
    private final MatchController matchController;


    public TKDScorePropertyChangeListener(List<JLabel> jLabelList, Chronometer chronometer, MatchController matchController) {
        this.jLabelList = jLabelList;
        this.chronometer = chronometer;
        this.matchController = matchController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        if(source instanceof JLabel) {
            JLabel sourceLabel = (JLabel) source;
            if (StringUtils.isNoneEmpty(sourceLabel.getName())) {
                String newValue = (String) evt.getNewValue();
                switch (sourceLabel.getName()) {
                    case "timer":

                        if ("0:00".equals(newValue) || "00:00".equals(newValue)) {
                            matchController.handleTimeOut();
                        }
                        break;
                    case "blueWin":
                        if ("2".equals(newValue)) {
                            // 📞 Llama al Controller: Azul ganó 2 rondas
                            matchController.handleMatchWin(PlayerColor.BLUE);
                        }
                        break;
                    case "redWin":
                        if ("2".equals(newValue)) {
                            // 📞 Llama al Controller: Azul ganó 2 rondas
                            matchController.handleMatchWin(PlayerColor.RED);
                        }
                        break;
                    case "blueGam":
                        if ("5".equals(newValue)) {
                            // 📞 Llama al Controller: Azul tiene 5 faltas. El ganador es ROJO (false)
                            matchController.handleGamJeomLimit(true);
                        }
                        break;
                    case "redGam":
                        if ("5".equals(newValue)) {
                            // 📞 Llama al Controller: Rojo tiene 5 faltas. El ganador es AZUL (true)
                            matchController.handleGamJeomLimit(false);
                        }
                        break;
                }
            }
        }

    }
/*
    private void wonByGam(JLabel sourceLabel, boolean isBlueWinner){
        if(sourceLabel.getText().equals("5")){
            chronometer.restartTime(chronometer.getBreakTime());
            chronometer.setIsBreakTime(true);
            sourceLabel.setText("0");
            setRoundWinner(isBlueWinner);
        }
    }
*//*
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
/*
    private void endScore(JLabel sourceLabel, boolean isBlueWinner){
        if(sourceLabel.getText().equals("2")){
            int result = JOptionPane.showOptionDialog(null, "¿Quien es el ganador final?", "Ganador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"AZUL", "ROJO"}, "AZUL");
            isBlueWinner=result==JOptionPane.YES_OPTION;
            chronometer.restartTime(chronometer.getMatchTime()); //restart chronometer
            chronometer.setIsBreakTime(false); //avoid break time
            jLabelList.get(9).setText(String.valueOf(Integer.parseInt(jLabelList.get(9).getText())+1)); //increase match time
            jLabelList.get(11).setText("1"); // reset round number
            jLabelList.get(14).setText("0"); //restart won score for blue
            jLabelList.get(16).setText("0"); //restart won score for red

            showWinnerMessage(isBlueWinner);
        }
    }
*//*
    private void showWinnerMessage(Boolean isBlueWinner){

        JFrame endWinner = new JFrame();
        endWinner.setAlwaysOnTop(false);
        endWinner.setExtendedState(JFrame.MAXIMIZED_BOTH);
        endWinner.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        endWinner.setResizable(false);
        endWinner.setMinimumSize(new Dimension(200, 200));
        endWinner.setLocationRelativeTo(null);
        endWinner.setLayout(new BorderLayout());
        endWinner.getContentPane().setBackground(isBlueWinner?Color.BLUE:Color.RED);

        endWinner.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyChar();
                switch (keyCode) {
                    case '.':
                    endWinner.dispose();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            });
        endWinner.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                restoreScore();
            jLabelList.get(14).setText("0"); //restart won score for blue
            jLabelList.get(16).setText("0"); //restart won score for red
            chronometer.restartTime(chronometer.getMatchTime());
            jLabelList.get(17).setBackground(Color.BLACK);
                chronometer.setIsBreakTime(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                restoreScore();
                jLabelList.get(14).setText("0"); //restart won score for blue
                jLabelList.get(16).setText("0"); //restart won score for red
                chronometer.restartTime(chronometer.getMatchTime());
                jLabelList.get(17).setBackground(Color.BLACK);
                chronometer.setIsBreakTime(false);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        Font font = jLabelList.get(0).getFont();
        JLabel winnerTex = new JLabel("WINNER");
        winnerTex.setFont(new Font(font.getFontName(),font.getStyle(),300));
        winnerTex.setHorizontalAlignment(JLabel.CENTER);
        winnerTex.setVerticalAlignment(JLabel.CENTER);
        winnerTex.setForeground(Color.YELLOW);
        winnerTex.setVisible(true);
        endWinner.setVisible(true);
        endWinner.add(winnerTex,BorderLayout.CENTER);
    }

    private void restoreScore() {
        jLabelList.get(1).setBackground(TDKScoreUtils.BLUE_COLOR);
        jLabelList.get(5).setBackground(TDKScoreUtils.RED_COLOR);
        jLabelList.get(1).setText("0");
        jLabelList.get(5).setText("0");
        // restart gam jeon
        jLabelList.get(3).setText("0");
        jLabelList.get(7).setText("0");
    }
    private void nextRound(){
        int currentRound = Integer.parseInt(jLabelList.get(11).getText()) + 1;
        if(Integer.parseInt(jLabelList.get(14).getText())==0
                && Integer.parseInt(jLabelList.get(16).getText())==0) {
            currentRound=1;
        }
        jLabelList.get(11).setText(String.valueOf(currentRound));

    }*/

}
