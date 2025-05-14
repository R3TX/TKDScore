import javax.swing.*;
import java.awt.*;

public class Board {
    private BoardBackground ventana;
    private JLabel redRival;
    private JLabel blueRival;

    public Board() {
        ventana = new BoardBackground();
        ventana.getContentPane().setBackground(Color.BLUE);
        ventana.setVisible(true); // Mostrar la ventana
    }

    public void assignNames(){
        blueRival = new JLabel();
        blueRival.setText("blueCompetitor");
        blueRival.setVerticalAlignment(0);
        blueRival.setHorizontalAlignment(0);
        blueRival.setBackground(Color.BLUE);
        blueRival.setBorder(BorderFactory.createBevelBorder(1));
        blueRival.setVisible(true);
        ventana.add(blueRival);
    }


}
