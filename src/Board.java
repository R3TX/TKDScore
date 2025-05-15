import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Board {
    private JFrame mainWindow;
    private List<JLabel> jLabelList;
    /*
    private JLabel redName;
    private JLabel redScore;
    private JLabel redGamJeomName;
    private JLabel redGamJeomScore;
    private JLabel blueName;
    private JLabel blueScore;
    private JLabel blueGamJeomName;
    private JLabel blueGamJeomScore;
    private JLabel matchName;
    private JLabel matchScore;
    private JLabel roundName;
    private JLabel roundScore;
    private JLabel timeLabel;
    */

    private BoardBackground boardBackground;

    public Board() {
        initializeComponents();
        setFrame();
        setAllVisible();
        alignLabels();
        //addComponents();
    }
/*
    private void alignLabels(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Name size and font
        int widthSeparation = (int) (screenSize.getWidth()/7);
        Font currentFont= jLabelList.get(0).getFont();
        jLabelList.get(0).setFont(new Font(currentFont.getFontName(),currentFont.getStyle(),52));
        int heigthLabel = (int) jLabelList.get(0).getPreferredSize().getHeight();
        jLabelList.get(0).setBounds(0, 0, widthSeparation*3, heigthLabel);
        jLabelList.get(0).setBorder(BorderFactory.createBevelBorder(1));
        //End Name size and font
        //Score Size and font
        int widthSeparationScore = (int) (widthSeparation/3);

        jLabelList.get(1).setBounds(widthSeparationScore, heigthLabel, widthSeparation*2, (int) (screenSize.getHeight()-(heigthLabel*3)));
        findFontSizeByJLabelSize(currentFont, jLabelList.get(1));
        jLabelList.get(1).setBorder(BorderFactory.createBevelBorder(1));


        //faults name
        jLabelList.get(2).setFont(new Font(currentFont.getFontName(),currentFont.getStyle(),52));
        int widthLabel = (int) jLabelList.get(2).getPreferredSize().getWidth();
        jLabelList.get(2).setBounds(0, (int) (screenSize.getHeight()-(heigthLabel*2)), widthLabel, heigthLabel);
        //jLabelList.get(2).setBorder(BorderFactory.createBevelBorder(1));
        jLabelList.get(2).setHorizontalAlignment(JLabel.LEFT);

        //faults score
        jLabelList.get(3).setFont(new Font(currentFont.getFontName(),currentFont.getStyle(),52));
        jLabelList.get(3).setBounds(widthLabel, (int) (screenSize.getHeight()-(heigthLabel*2)), widthLabel, heigthLabel);
        jLabelList.get(3).setBorder(BorderFactory.createBevelBorder(1));

    }
*/
    private void alignLabels() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthSeparation = (int) (screenSize.getWidth() / 7);
        Font currentFont= jLabelList.get(0).getFont();
        jLabelList.get(0).setFont(new Font(currentFont.getFontName(),currentFont.getStyle(),52));
        int heightLabel = (int) jLabelList.get(0).getPreferredSize().getHeight();
        int defaultFontSize=52;

        // Configuración de los labels azules
        alignLabel(jLabelList.get(0), 0, 0, widthSeparation * 3, heightLabel,defaultFontSize); // Nombre
        alignLabel(jLabelList.get(2), 0, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation * 2, heightLabel,defaultFontSize); // Faltas nombre
        alignLabel(jLabelList.get(3), widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation, heightLabel,defaultFontSize); // Faltas puntuación

        // Configuración de los labels rojos
        int xRojo = widthSeparation * 4;
        alignLabel(jLabelList.get(4), xRojo, 0, widthSeparation * 3, heightLabel, defaultFontSize); // Nombre
        alignLabel(jLabelList.get(6), xRojo, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation * 2, heightLabel, defaultFontSize); // Faltas nombre
        alignLabel(jLabelList.get(7), xRojo + widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation, heightLabel, defaultFontSize); // Faltas puntuación

        int widthSeparationScore = (int) (widthSeparation/3);
        jLabelList.get(1).setBounds(widthSeparationScore, heightLabel, widthSeparation*2, (int) (screenSize.getHeight()-(heightLabel*3)));
        findFontSizeByJLabelSize(currentFont, jLabelList.get(1));
        jLabelList.get(1).setBorder(BorderFactory.createBevelBorder(1));
        alignLabel(jLabelList.get(5), xRojo + widthSeparationScore, heightLabel, widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 3)),jLabelList.get(1).getFont().getSize()); // Puntuación

        setScoreLabelBackground(new Color(0,128,255), jLabelList.get(1));
        setScoreLabelBackground(new Color(255,51,51), jLabelList.get(5));

        //black line labels
        int xBlack = widthSeparation * 3;
        alignLabel(jLabelList.get(8), xBlack, heightLabel, widthSeparation, heightLabel,52);
        alignLabel(jLabelList.get(9), xBlack, heightLabel*2, widthSeparation, heightLabel,52);
        alignLabel(jLabelList.get(10), xBlack, (int) (screenSize.getHeight()-(heightLabel*4)), widthSeparation, heightLabel,52);
        alignLabel(jLabelList.get(11), xBlack, (int) (screenSize.getHeight()-(heightLabel*3)), widthSeparation, heightLabel,52);

        alignLabel(jLabelList.get(12), xBlack, heightLabel*6, widthSeparation, heightLabel,52);

        for (int i = 8; i<13;i++) {
            jLabelList.get(i).setForeground(Color.WHITE);
        }

        setTimeFormat();


    }
    public void setTimeFormat(){
        // Obtener la fecha y hora actual
        Date fechaHora = new Date();

        // Crear un objeto SimpleDateFormat para el formato deseado
        SimpleDateFormat formato = new SimpleDateFormat("mm:ss"); // Formato de hora: hh:mm:ss (horas, minutos y segundos)

        // Formatear la fecha y hora
        String tiempoFormateado = formato.format(fechaHora);

        // Establecer el texto del JLabel con el tiempo formateado
        jLabelList.get(12).setText(tiempoFormateado);
    }

    public void setScoreLabelBackground(Color color, JLabel label){
        label.setBackground(color);
        label.setOpaque(true);
    }

    private void alignLabel(JLabel label, int x, int y, int width, int height, int fontSize) {
        label.setBounds(x, y, width, height);
        label.setFont(getFontWithSize(label, fontSize));
        //label.setBorder(BorderFactory.createBevelBorder(1));
    }

    private Font getFontWithSize(JLabel label, int size) {
        Font currentFont = label.getFont();
        return new Font(currentFont.getFontName(), currentFont.getStyle(), size);
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
        return fontSize;
    }

    private void initializeComponents(){
        jLabelList = new ArrayList<>();
        mainWindow = new JFrame("TKD Score");
        boardBackground= new BoardBackground();

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

    private void setAllVisible(){
        jLabelList.forEach(x-> {
            x.setVisible(true);
            x.setForeground(Color.BLACK);
            x.setHorizontalAlignment(JLabel.CENTER);
            x.setVerticalAlignment(JLabel.CENTER);
            boardBackground.add(x);
        });
    }
/*
    private void addComponents(){
        boardBackground.add(redName);
        boardBackground.add(redScore);
        boardBackground.add(redGamJeomName);
        boardBackground.add(redGamJeomScore);
        boardBackground.add(blueName);
        boardBackground.add(blueScore);
        boardBackground.add(blueGamJeomName);
        boardBackground.add(blueGamJeomScore);
        boardBackground.add(matchName);
        boardBackground.add(matchScore);
        boardBackground.add(roundName);
        boardBackground.add(roundScore);
        boardBackground.add(timeLabel);
    }
*/
    private void setFrame(){
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setAlwaysOnTop(false);
        //boardBackground.setResizable(false);
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        boardBackground.setLayout(null);
        mainWindow.add(boardBackground);
        mainWindow.setVisible(true);
    }
}
