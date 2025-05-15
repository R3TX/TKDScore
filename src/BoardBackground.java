import javax.swing.*;
import java.awt.*;

public class BoardBackground extends JFrame {

    public BoardBackground() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
    }

    @Override
    public void paint(Graphics draw){
        int separations = getWidth()/7;
        draw.setColor(Color.BLUE);
        draw.fillRect(0, 0, separations*3, getHeight());
        draw.setColor(Color.BLACK);
        draw.fillRect(separations*3, 0, separations, getHeight());
        draw.setColor(Color.RED);
        draw.fillRect(separations*4, 0, separations*3, getHeight());

    }
}
