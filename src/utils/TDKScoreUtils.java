package utils;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TDKScoreUtils {
    public static final String NAME_REGEX = "^[a-zA-Z ]+$";
    public static final String SCORE_NUMERIC_REGEX = "^[0-9]+$";
    public static final String ROUND_NUMERIC_REGEX = "^[1-3]+$";
    public static final String VICTORY_NUMERIC_REGEX = "^[0-2]+$";
    public static final String TIME_REGEX = "(0?[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9])";
    public static final String INPUT_MESSAGE_NAME = "Por favor escriba el Nombre ";
    public static final String INPUT_MESSAGE_NEW_SCORE = "Por favor escriba el nuevo marcador ";
    public static final String INPUT_MESSAGE_NUMERIC = "Por favor escriba el nuevo Valor ";
    public static final String INPUT_MESSAGE_TIME = "Por favor escriba el tiempo en formato mm:ss";
    public static final String TITTLE_MESSAGE = "Ingrese un Valor";
    public static Color BLUE_COLOR = new Color(0, 128, 255);
    public static Color RED_COLOR = new Color(255, 51, 51);
    public static String style = "style='color: white; text-shadow: -2px 0 black, 0 2px black, 2px 0 black, 0 -2px black;'";
    private static final Pattern TEXT_PATTERN = Pattern.compile("(?s)>([^<]*)</");


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

    public static void formatJlabelText(JLabel label) {
        String htmlText = label.getText();
        String currentText = extractTextFromHtml(htmlText);

        // El ancho y alto disponible
        int availableWidth = label.getWidth();
        int availableHeight = label.getHeight();

        // Si el componente aún no tiene tamaño (0) o no tiene texto, salimos.
        if (availableWidth <= 0 || availableHeight <= 0 || currentText == null || currentText.isEmpty()) {
            return;
        }

        // Delegamos el cálculo a un método central.
        float newSize = calculateBestFitFontSize(
                label,
                currentText,
                availableWidth,
                availableHeight
        );

        // Aplicar y redibujar solo si el tamaño cambió significativamente
        if (newSize > 0 && Math.abs(label.getFont().getSize2D() - newSize) > 0.5f) {
            label.setFont(label.getFont().deriveFont(newSize));
            label.repaint();
        }
    }

    /**
     * Extrae la cadena de texto visible de una cadena que contiene HTML simple.
     *
     * @param html La cadena HTML completa del JLabel.
     * @return El texto puro a medir.
     */
    public static String extractTextFromHtml(String html) {
        if (html == null || !html.startsWith("<html>")) {
            // No es HTML o es texto plano, lo devolvemos directamente.
            return html != null ? html : "";
        }

        Matcher matcher = TEXT_PATTERN.matcher(html);

        // Buscamos el último trozo de texto visible.
        String visibleText = "";
        while (matcher.find()) {
            // El grupo 1 es el contenido entre las etiquetas.
            String match = matcher.group(1).trim();
            if (!match.isEmpty()) {
                visibleText = match;
            }
        }
        return visibleText;
    }

    /**
     * Calcula el tamaño de fuente óptimo para que el texto encaje dentro de las dimensiones.
     */
    private static float calculateBestFitFontSize(JLabel label, String text, int targetWidth, int targetHeight) {
        // Usamos la fuente actual como base, buscando desde un tamaño grande (ej. 200)
        // para asegurar que siempre encontremos un punto de inicio.
        String fontName = label.getFont().getName();
        int fontStyle = label.getFont().getStyle();
        float bestSize = 200.0f;

        // 1. Reducir la fuente hasta que el texto quepa.
        while (bestSize > 1.0f) {
            Font tempFont = new Font(fontName, fontStyle, (int) bestSize);
            FontMetrics fm = label.getFontMetrics(tempFont);

            if (fm.stringWidth(text) < targetWidth && fm.getHeight() < targetHeight) {
                // El texto cabe a este tamaño.
                break;
            }
            bestSize -= 1.0f;
        }

        // 2. Aumentar ligeramente para maximizar el ajuste sin desbordar.
        float finalSize = bestSize;
        while (true) {
            float nextSize = finalSize + 1.0f;
            Font tempFont = new Font(fontName, fontStyle, (int) nextSize);
            FontMetrics fm = label.getFontMetrics(tempFont);

            if (fm.stringWidth(text) < targetWidth && fm.getHeight() < targetHeight) {
                finalSize = nextSize;
            } else {
                break;
            }
        }

        return finalSize;
    }
}
