import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class TKDKeyListener implements KeyListener {
    List<JLabel> jLabelList;
    public TKDKeyListener(List<JLabel> jLabelList) {
        this.jLabelList=jLabelList;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyChar();
        switch (keyCode){
            case '1':
                scorePlus(jLabelList.get(1),1);
            break;
            case '3':
                scorePlus(jLabelList.get(5),1);
                break;
            case '4':
                scorePlus(jLabelList.get(1),2);
                break;
            case '6':
                scorePlus(jLabelList.get(5),2);
                break;
            case '7':
                scorePlus(jLabelList.get(1),3);
                break;
            case '9':
                scorePlus(jLabelList.get(5),3);
                break;
            case '-':
                scorePlus(jLabelList.get(3),1);
                scorePlus(jLabelList.get(5),1);
                break;
            case '+':
                scorePlus(jLabelList.get(7),1);
                scorePlus(jLabelList.get(1),1);
                break;
            case '/':
                int confirmDialog = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas continuar?", "Confirmación", JOptionPane.YES_NO_OPTION);

                // Procesar la respuesta del usuario
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    initializeComponents();
                    // Realizar la acción que el usuario ha confirmado
                }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void scorePlus(JLabel jLabel,int plus){
        int currentScore=Integer.parseInt(jLabel.getText())+plus;
        jLabel.setText(String.valueOf(currentScore));
        if(currentScore>10){
            jLabel.setFont(new Font(jLabel.getFont().getFontName(),jLabel.getFont().getStyle(),findFontSizeByJLabelSize(jLabel.getFont(),jLabel)));
        }
    }

    private int findFontSizeByJLabelSize(Font font, JLabel jLabel){
        int fontSize = 1;
        Dimension preferredSize;

        do{
            font = new Font(font.getFontName(), font.getStyle(), fontSize);
            jLabel.setFont(font);
            preferredSize = jLabel.getPreferredSize();
            fontSize++;
        } while (preferredSize.width <= jLabel.getWidth() && preferredSize.height <= jLabel.getHeight());
        fontSize--;
        return fontSize/2;
    }

    private void initializeComponents(){
        jLabelList = new ArrayList<>();

        jLabelList.add(new JLabel("Insert Blue Name")); //blue contestant name 0
        jLabelList.add(new JLabel("0")); //blue contestant score 1
        jLabelList.add(new JLabel("GAM-JEOM")); //blue contestant faults name 2
        jLabelList.add(new JLabel("0")); //blue contestant faults Score 3
        jLabelList.add(new JLabel("Insert Red Name"));  //red contestant name 4
        jLabelList.add(new JLabel("0")); //red contestant score 5
        jLabelList.add(new JLabel("GAM-JEOM")); //red contestant faults name 6
        jLabelList.add(new JLabel("0")); //red contestant faults Score 7
        jLabelList.add(new JLabel("Match")); //8
        jLabelList.add(new JLabel("1")); //9
        jLabelList.add(new JLabel("Round"));  //10
        jLabelList.add(new JLabel("1")); //11
        jLabelList.add(new JLabel("0")); // round time 12


    }
}
