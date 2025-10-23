package view.adapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adaptador robusto para escalar el tamaño de la fuente de un JLabel
 * automáticamente para que se ajuste al 100% del espacio disponible.
 * * Uso: label.addComponentListener(new FontResizerUtility(label));
 */
public class FontResizerUtility extends ComponentAdapter {

    private final JLabel label;
    private static final Pattern TEXT_PATTERN = Pattern.compile("(?s)>([^<]*)</");

    /**
     * Constructor que simplemente toma el JLabel. El texto se obtiene en tiempo real.
     *
     * @param label El JLabel cuya fuente se debe escalar.
     */
    public FontResizerUtility(JLabel label) {
        this.label = label;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // 1. OBTENER Y LIMPIAR EL TEXTO
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
    private String extractTextFromHtml(String html) {
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
    private float calculateBestFitFontSize(JLabel label, String text, int targetWidth, int targetHeight) {
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