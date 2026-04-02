package view.scoreView;

import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;

public class WinnerView extends JPanel {
    public WinnerView(Color winnerColor) {
        this.setBackground(winnerColor);

        JLabel winner = new JLabel("WINNER");
        winner.setOpaque(false);
        winner.setVisible(true);
        winner.setForeground(Color.YELLOW);
        winner.setSize(800,600);
        TDKScoreUtils.formatJlabelText(winner);
        add(winner);
        setLayout(new GridBagLayout());

    }
}
