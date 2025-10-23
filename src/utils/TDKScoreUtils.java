package utils;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class TDKScoreUtils {
    public static final String NAME_REGEX = "^[a-zA-Z ]+$";
    public static final String NUMERIC_REGEX = "^[0-9]+$";
    public static final String TIME_REGEX = "(0?[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9])";
    public static final String INPUT_MESSAGE_NAME = "Por favor escriba el Nombre ";
    public static final String INPUT_MESSAGE_NEW_SCORE = "Por favor escriba el nuevo marcador ";
    public static final String INPUT_MESSAGE_NUMERIC = "Por favor escriba el nuevo Valor ";
    public static final String INPUT_MESSAGE_TIME = "Por favor escriba el tiempo en formato mm:ss";
    public static final String TITTLE_MESSAGE = "Ingrese un Valor";
    public static Color BLUE_COLOR = new Color(0, 128, 255);
    public static Color RED_COLOR = new Color(255, 51, 51);
    public static String style = "style='color: white; text-shadow: -2px 0 black, 0 2px black, 2px 0 black, 0 -2px black;'";


    public static String getInputText(String inputMessage, String regex, String defaultValue) {
        String names = JOptionPane.showInputDialog(null, inputMessage, TITTLE_MESSAGE, JOptionPane.QUESTION_MESSAGE);

        while (null != names && (StringUtils.isWhitespace(names) || !names.matches(regex))) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un valor correcto. Inténtelo nuevamente.");
            names = JOptionPane.showInputDialog(null, inputMessage, TITTLE_MESSAGE, JOptionPane.QUESTION_MESSAGE);
        }
        if (null == names) {
            return defaultValue;
        }
        return names;

    }

    public static int findFontSizeByJLabelSize(Font font, JLabel jLabel) {
        int fontSize = 1;
        Dimension preferredSize;

        do {
            font = new Font(font.getFontName(), font.getStyle(), fontSize);
            jLabel.setFont(font);
            preferredSize = jLabel.getPreferredSize();
            fontSize++;
        } while (preferredSize.width <= jLabel.getWidth() && preferredSize.height <= jLabel.getHeight());
        fontSize--;
        return fontSize / 2;
    }

    public static String formatTextColor(String newText) {
        return "<html><span " + style + ">" + newText + "</span></html>";
    }

}
