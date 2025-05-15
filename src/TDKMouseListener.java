import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TDKMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        // Verificar el tipo de objeto
        if (source instanceof JLabel) {
            JLabel label = (JLabel) source;
            if(StringUtils.isNoneEmpty(label.getName())) {
                switch (label.getName()) {
                    case "blueName":
                        setCompetitorName(label);
                        break;
                    case "redName":
                        setCompetitorName(label);
                        break;
                    case "match":
                        numericValues(label);
                        break;
                    case "round":
                        numericValues(label);
                        break;
                    case "timer":
                        timerValue(label);
                        break;
                    case "blueGam":
                        numericValues(label);
                        break;
                    case "redGam":
                        numericValues(label);
                        break;
                }
            }

        }

    }

    private void timerValue(JLabel label){
        String minutes = JOptionPane.showInputDialog("Por favor escriba los Minutos");
        if (StringUtils.isNoneBlank(minutes) && minutes.matches("^[0-9]+$")){
            minutes=minutes.length()==1?"0"+minutes:minutes;
            String seconds = JOptionPane.showInputDialog("Por favor escriba los segundos");
            if (StringUtils.isNoneBlank(minutes) && minutes.matches("^[0-9]+$")) {
                seconds=seconds.length()==1?"0"+seconds:seconds;
                label.setText(minutes+":"+seconds);
            }
        }
    }

    private void numericValues(JLabel label){
        String names = JOptionPane.showInputDialog("Por favor escriba el valor");
        if (StringUtils.isNoneBlank(names) && names.matches("^[0-9]+$")){
            label.setText(names);
        }
    }

    private void setCompetitorName(JLabel label){
        String names = JOptionPane.showInputDialog("Por favor escriba el valor");
        if (StringUtils.isNoneBlank(names) && names.matches("^[a-zA-Z]+$")){
            label.setText(names);
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
