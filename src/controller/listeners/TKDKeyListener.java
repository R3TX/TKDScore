package controller.listeners;

import controller.match.MatchController;
import model.entity.PlayerColor;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class TKDKeyListener implements KeyListener {
    private final List<JLabel> jLabelList;
    private final Chronometer chronometer;
    private final int widthSeparation;
    private final int heightLabel;
    private final Dimension screenSize;
    private final MatchController matchController;

    public TKDKeyListener(List<JLabel> jLabelList, Chronometer chronometer, int widthSeparation, int heightLabel, Dimension screenSize, MatchController matchController) {
        this.jLabelList = jLabelList;
        this.chronometer = chronometer;
        this.widthSeparation = widthSeparation;
        this.heightLabel = heightLabel;
        this.screenSize = screenSize;
        this.matchController = matchController;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyChar();
        switch (keyCode) {
            case '1':
                matchController.registerScore(PlayerColor.BLUE, 1);
                break;
            case '3':
                matchController.registerScore(PlayerColor.RED, 1);
                break;
            case '4':
                matchController.registerScore(PlayerColor.BLUE, 2);
                break;
            case '6':
                matchController.registerScore(PlayerColor.RED, 2);
                break;
            case '7':
                matchController.registerScore(PlayerColor.BLUE, 3);
                break;
            case '9':
                matchController.registerScore(PlayerColor.RED, 3);
                break;
            case '-':
                matchController.registerGamJeom(PlayerColor.RED);
                break;
            case '+':
                matchController.registerGamJeom(PlayerColor.BLUE);
                break;
            case '0':
                matchController.decreaseGamJeom(PlayerColor.RED);
                break;
            case '2':
                matchController.decreaseGamJeom(PlayerColor.BLUE);
                break;
            case '/':
                matchController.requestFullMatchReset();
                break;
            case KeyEvent.VK_SPACE:
                matchController.toggleTimer();
                break;
            case '8':
                matchController.decreaseRoundScore(PlayerColor.BLUE);
                break;
            case '5':
                matchController.decreaseRoundScore(PlayerColor.RED);
                break;


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
/*
    private void scorePlus(JLabel jLabel, int plus, boolean isFault, boolean isDecreasing) {
        if((((chronometer.isRunning() || isFault) && !chronometer.isIS_BREAK_TIME() )  ||  (!chronometer.isRunning() && isDecreasing)) && (Integer.parseInt(jLabel.getText())+plus)>=0){
            int currentScore = Integer.parseInt(jLabel.getText()) + plus;
            jLabel.setText(String.valueOf(currentScore));
            if (currentScore > 9) {
                int widthSeparationScore = (widthSeparation / 3);
                jLabelList.get(1).setBounds(widthSeparationScore, heightLabel, widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 3)));
                jLabel.setFont(new Font(jLabel.getFont().getFontName(), jLabel.getFont().getStyle(), TDKScoreUtils.findFontSizeByJLabelSize(jLabel.getFont(), jLabel)));
            }
        }
    }
*/
}
