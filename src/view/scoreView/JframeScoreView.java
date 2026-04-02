package view.scoreView;

import model.entity.RoundStatisticsEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;

public class JframeScoreView extends JFrame {
    Map<Integer, List<RoundStatisticsEntity>> playersScore;
    ScoreViewKeyListener scoreViewKeyListener;
    public JframeScoreView(Map<Integer, List<RoundStatisticsEntity>> playersScore){
        this.playersScore = playersScore;
        scoreViewKeyListener = new ScoreViewKeyListener(this);
    }
    public void initFrame(){
        setContentPane(new PanelPlayersScore(playersScore,this));

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        GraphicsConfiguration config = getGraphicsConfiguration();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);
        Rectangle bounds = config.getBounds();

        setSize(
                bounds.width - insets.left - insets.right,
                bounds.height - insets.top - insets.bottom
        );


        setFocusable(true);
        requestFocus();
        addKeyListener(scoreViewKeyListener);
        setVisible(true);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
}

class ScoreViewKeyListener implements KeyListener{
    JframeScoreView jframeScoreView;

    public ScoreViewKeyListener(JframeScoreView jFrame){
        this.jframeScoreView=jFrame;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyChar();
        if (keyCode == '.') {
            jframeScoreView.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
