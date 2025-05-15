import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
}
