package controller.listeners;

import model.entity.CommonComponentsName;
import org.apache.commons.lang3.StringUtils;
import utils.TDKScoreUtils;

import javax.swing.JLabel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TDKScoreViewMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        // Verify what object was clicked
        if (source instanceof JLabel label) {
            String input;
            if (CommonComponentsName.commonStats.stream().anyMatch(x-> x.componentName().equals(label.getName()))){
                input = TDKScoreUtils
                        .getInputText(
                                TDKScoreUtils.INPUT_MESSAGE_NEW_SCORE,
                                TDKScoreUtils.SCORE_NUMERIC_REGEX,
                                TDKScoreUtils.extractTextFromHtml(label.getText()));
                if (StringUtils.isNoneEmpty(input)) {
                    label.setText(TDKScoreUtils.formatTextColor(input));
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
