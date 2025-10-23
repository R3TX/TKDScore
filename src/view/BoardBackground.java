package view;

import javax.swing.*;
import java.awt.*;

public class BoardBackground extends JPanel {

    @Override
    public void paintComponent(Graphics draw) {
        super.paintComponent(draw);
        final int widthSeparations = getWidth() / 7;
        draw.setColor(Color.BLUE);
        draw.fillRect(0, 0, widthSeparations * 3, getHeight());
        draw.setColor(Color.BLACK);
        draw.fillRect(widthSeparations * 3, 0, widthSeparations, getHeight());
        draw.setColor(Color.RED);
        draw.fillRect(widthSeparations * 4, 0, widthSeparations * 3, getHeight());
    }

}
