package controller.listeners;

import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class TKDKeyListener implements KeyListener {
    private final List<JLabel> jLabelList;
    private final List<String> jLabelTextList;
    private final Chronometer chronometer;
    private final int widthSeparation;
    private final int heightLabel;
    private final Dimension screenSize;

    public TKDKeyListener(List<JLabel> jLabelList, List<String> jLabelTextList, Chronometer chronometer, int widthSeparation, int heightLabel, Dimension screenSize) {
        this.jLabelList = jLabelList;
        this.jLabelTextList = jLabelTextList;
        this.chronometer = chronometer;
        this.widthSeparation = widthSeparation;
        this.heightLabel = heightLabel;
        this.screenSize = screenSize;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyChar();
        switch (keyCode) {
            case '1':
                    scorePlus(jLabelList.get(1), 1,false, false);
                break;
            case '3':
                    scorePlus(jLabelList.get(5), 1, false, false);
                break;
            case '4':
                    scorePlus(jLabelList.get(1), 2, false, false);
                break;
            case '6':
                    scorePlus(jLabelList.get(5), 2, false, false);
                break;
            case '7':
                    scorePlus(jLabelList.get(1), 3, false, false);
                break;
            case '9':
                    scorePlus(jLabelList.get(5), 3, false, false);
                break;
            case '-':
                scorePlus(jLabelList.get(5), 1,true, false);
                scorePlus(jLabelList.get(3), 1,true, false);
                break;
            case '+':
                scorePlus(jLabelList.get(1), 1,true, false);
                scorePlus(jLabelList.get(7), 1,true,false );
                break;
            case '0':
                if(Integer.parseInt(jLabelList.get(3).getText())-1>-1) {
                    scorePlus(jLabelList.get(5), -1, true, false);
                }
                scorePlus(jLabelList.get(3), -1,true, false);

                break;
            case '2':
                if(Integer.parseInt(jLabelList.get(7).getText())-1>-1) {
                    scorePlus(jLabelList.get(1), -1, true, false);
                }
                scorePlus(jLabelList.get(7), -1,true,false );

                break;
            case '/':
                int confirmDialog = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas continuar?", "Confirmación", JOptionPane.YES_NO_OPTION);

                // Procesar la respuesta del usuario
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    for(int i=0;i<jLabelList.size();i++){
                        jLabelList.get(i).setText(jLabelTextList.get(i));
                    }
                    jLabelList.get(12).setText(chronometer.getMatchTime());
                    chronometer.stopTime();
                    chronometer.restartTime(chronometer.getMatchTime());
                    jLabelList.get(1).setBackground(TDKScoreUtils.BLUE_COLOR);
                    jLabelList.get(5).setBackground(TDKScoreUtils.RED_COLOR);
                    jLabelList.get(17).setBackground(Color.BLACK);
                }
                break;
            case KeyEvent.VK_ENTER:
                chronometer.startStopTimer();
                break;
            case '8':
                if(!chronometer.isRunning()) {
                    scorePlus(jLabelList.get(1), -1, false, true);
                }
                break;
            case '5':
                if(!chronometer.isRunning()) {
                    scorePlus(jLabelList.get(5), -1, false,true );
                }
                break;


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

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

}
