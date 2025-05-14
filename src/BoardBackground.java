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
        int half = getWidth()/2;
        draw.setColor(Color.BLUE);
        draw.fillRect(0, 0, half, getHeight());
        draw.setColor(Color.RED);
        draw.fillRect(half, 0, half, getHeight());

    }
}
