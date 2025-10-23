package view;

import model.entity.PlayerColor;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;

public class JFrameColumns extends JFrame {
    public JFrameColumns() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        int frameWidth = 800;
        int panelWidth45 = (int) (frameWidth * 0.45);
        int panelWidth10 = (int) (frameWidth * 0.10);

        Dimension preferredSize45 = new Dimension(panelWidth45, 1);

        // Panel 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.45;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        PanelGridScore panel1 = new PanelGridScore(PlayerColor.RED);
        panel1.setPreferredSize(preferredSize45);
        panel1.setBackground(Color.RED);
        panel1.getCompetitorScore().setBackground(TDKScoreUtils.RED_COLOR);
        add(panel1, gbc);

        // Panel 2 (columna del medio)
        gbc.gridx = 1;
        gbc.weightx = 0.1;
        PanelGridTime panel2 = new PanelGridTime();
        panel2.setPreferredSize(new Dimension(panelWidth10, 1));
        panel2.setBackground(Color.BLACK);
        add(panel2, gbc);

        // Panel 3
        gbc.gridx = 2;
        gbc.weightx = 0.45;
        PanelGridScore panel3 = new PanelGridScore(PlayerColor.BLUE);
        panel3.setPreferredSize(preferredSize45);
        panel3.setBackground(Color.BLUE);
        panel3.getCompetitorScore().setBackground(TDKScoreUtils.BLUE_COLOR);
        add(panel3, gbc);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
