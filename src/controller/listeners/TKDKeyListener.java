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

    private final MatchController matchController;

    public TKDKeyListener(MatchController matchController) {
        this.matchController = matchController;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyChar();
        switch (keyCode) {
            case '3':
                matchController.registerScore(PlayerColor.BLUE, 1);
                break;
            case '1':
                matchController.registerScore(PlayerColor.RED, 1);
                break;
            case '6':
                matchController.registerScore(PlayerColor.BLUE, 2);
                break;
            case '4':
                matchController.registerScore(PlayerColor.RED, 2);
                break;
            case '9':
                matchController.registerScore(PlayerColor.BLUE, 3);
                break;
            case '7':
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
}
