package view.adapter;

import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Adaptador robusto para escalar el tamaño de la fuente de un JLabel
 * automáticamente para que se ajuste al 100% del espacio disponible.
 * * Uso: label.addComponentListener(new FontResizerUtility(label));
 */
public class FontResizerUtility extends ComponentAdapter {

    private final JLabel label;

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
        TDKScoreUtils.formatJlabelText(label);
    }
}