import javax.swing.*;
import java.awt.*;

public class Board {
    JFrame ventana;
    JLabel redRival;
    JLabel blueRival;
    public Board() {
        ventana = new JFrame("Taekwondo Score");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setAlwaysOnTop(true);
        ventana.setResizable(false);
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setVisible(true); // Mostrar la ventana
    }

    public void assignNames(){
        redRival = new JLabel();
    }



}
