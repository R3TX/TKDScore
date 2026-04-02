package view.scoreView;

import model.entity.PlayerColor;
import model.entity.RoundStatisticsEntity;
import utils.CalculatorUtils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

public class JPanelbuttonsScoreView extends JPanel {
    JFrame myContainer;
    Color winner;

    public JPanelbuttonsScoreView(Map<Integer, List<RoundStatisticsEntity>> playersScore, JFrame myContainer){
        this.myContainer=myContainer;
        setLayout(new GridLayout());
        createButtons(playersScore);
        setOpaque(false);
    }
    public void createButtons(Map<Integer, List<RoundStatisticsEntity>> playersScore){
        JButton calculate= new JButton();
        JButton accept= new JButton();

        accept.setText("Aceptar");
        calculate.setText("Calcular");

        accept.setName("acceptButton");
        calculate.setName("calculateButton");

        accept.addActionListener(e->{
            myContainer.setContentPane(new WinnerView(winner));
            this.setBackground(winner);
            myContainer.revalidate();
            myContainer.repaint();
        });
        calculate.addActionListener(e->{
            winner = PlayerColor.BLUE.name().equals(CalculatorUtils.calculate(playersScore))?
                    Color.BLUE:Color.RED;
        });

        add(calculate);
        add(accept);
    }
}
