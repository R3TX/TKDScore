import listeners.*;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Board {
    private JFrame mainWindow;
    private List<JLabel> jLabelList;
    private List<String> jLabelTextList;
    private Chronometer chronometer;

    private Dimension screenSize;
    private int widthSeparation;
    private Font currentFont;
    private int heightLabel;
    private int defaultFontSize;


    private BoardBackground boardBackground;

    public Board() {
        initializeComponents();
        setChronometerTimes(); // dont move
        screenSize(); //debe ir antes del listener
        setFrame();
        setAllVisible();
        alignLabels();
        setLabelsName();
    }

    private void screenSize() {
        defaultFontSize = 52;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        widthSeparation = (int) (screenSize.getWidth() / 7);
        currentFont = jLabelList.get(0).getFont();
        jLabelList.get(0).setFont(new Font(currentFont.getFontName(), currentFont.getStyle(), defaultFontSize));

        heightLabel = (int) jLabelList.get(0).getPreferredSize().getHeight();
        jLabelList.get(0).setBounds(0,0,widthSeparation * 3, heightLabel);
        //defaultFontSize = TDKScoreUtils.findFontSizeByJLabelSize(currentFont, jLabelList.get(0));
    }

    private void alignLabels() {

        int boardBackgroundWidth = boardBackground.getWidth() / 7;

        // Configuración de los labels azules
        alignLabel(jLabelList.get(0), 0, 0, boardBackgroundWidth * 3, heightLabel, defaultFontSize); // Nombre
        alignLabel(jLabelList.get(2), 0, (int) (screenSize.getHeight() - (heightLabel * 2)), boardBackgroundWidth * 2, heightLabel, defaultFontSize); // Faltas nombre
        alignLabel(jLabelList.get(3), boardBackgroundWidth * 2, (int) (screenSize.getHeight() - (heightLabel * 2)), boardBackgroundWidth, heightLabel, defaultFontSize); // Faltas puntuación
        jLabelList.get(0).setForeground(Color.WHITE);
        jLabelList.get(2).setForeground(Color.WHITE);
        jLabelList.get(3).setForeground(Color.WHITE);
        // Configuración de los labels rojos
        int xRojo = boardBackgroundWidth * 4;
        alignLabel(jLabelList.get(4), xRojo, 0, boardBackgroundWidth * 3, heightLabel, defaultFontSize); // Nombre
        alignLabel(jLabelList.get(6), xRojo, (int) (screenSize.getHeight() - (heightLabel * 2)), boardBackgroundWidth * 2, heightLabel, defaultFontSize); // Faltas nombre
        alignLabel(jLabelList.get(7), xRojo + boardBackgroundWidth * 2, (int) (screenSize.getHeight() - (heightLabel * 2)), boardBackgroundWidth, heightLabel, defaultFontSize); // Faltas puntuación
        jLabelList.get(4).setForeground(Color.WHITE);
        jLabelList.get(6).setForeground(Color.WHITE);
        jLabelList.get(7).setForeground(Color.WHITE);
        //Configuration for score labels
        int widthSeparationScore = (int) (boardBackgroundWidth / 3);
        jLabelList.get(1).setBounds(widthSeparationScore, heightLabel, boardBackgroundWidth * 2, (int) (screenSize.getHeight() - (heightLabel * 3)));
        TDKScoreUtils.findFontSizeByJLabelSize(currentFont, jLabelList.get(1));
        alignLabel(jLabelList.get(5), xRojo + widthSeparationScore*2, heightLabel, boardBackgroundWidth * 2, (int) (screenSize.getHeight() - (heightLabel * 3)), jLabelList.get(1).getFont().getSize()); // Puntuación rojo
        jLabelList.get(1).setForeground(Color.WHITE);
        jLabelList.get(5).setForeground(Color.WHITE);

        setScoreLabelBackground( TDKScoreUtils.BLUE_COLOR, jLabelList.get(1));
        setScoreLabelBackground(TDKScoreUtils.RED_COLOR, jLabelList.get(5));

        //winnings labels
        FontMetrics fontMetrics = jLabelList.get(13).getFontMetrics(currentFont);
        alignLabel(jLabelList.get(13), widthSeparationScore * 7, heightLabel, boardBackgroundWidth/2, heightLabel, defaultFontSize);
        TDKScoreUtils.findFontSizeByJLabelSize(currentFont, jLabelList.get(13));
        jLabelList.get(13).setFont(new Font(currentFont.getFontName(),currentFont.getStyle(),jLabelList.get(13).getFont().getSize()/2));
        alignLabel(jLabelList.get(14), widthSeparationScore * 7, heightLabel * 2,fontMetrics.charWidth('W') * 12 , heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(15), boardBackgroundWidth * 4, heightLabel, boardBackgroundWidth/2, heightLabel, jLabelList.get(13).getFont().getSize());
        alignLabel(jLabelList.get(16), boardBackgroundWidth * 4, heightLabel * 2, fontMetrics.charWidth('W') * 12, heightLabel, defaultFontSize);
        jLabelList.get(13).setForeground(Color.WHITE);
        jLabelList.get(14).setForeground(Color.WHITE);
        jLabelList.get(15).setForeground(Color.WHITE);
        jLabelList.get(16).setForeground(Color.WHITE);
        //black line labels/*
        int xBlack = boardBackgroundWidth * 3;
        int screenSize = boardBackground.getHeight()/heightLabel;
        int screenSizeSeparation = boardBackground.getHeight()/screenSize;
        alignLabel(jLabelList.get(8), xBlack, screenSizeSeparation, boardBackgroundWidth, heightLabel, defaultFontSize); //match
        alignLabel(jLabelList.get(9), xBlack, screenSizeSeparation * 2, boardBackgroundWidth, heightLabel, defaultFontSize); // match number
        alignLabel(jLabelList.get(10), xBlack,screenSizeSeparation*(screenSize-3), boardBackgroundWidth, heightLabel, defaultFontSize); //round
        alignLabel(jLabelList.get(11), xBlack,screenSizeSeparation*(screenSize-2), boardBackgroundWidth, heightLabel, defaultFontSize); //round number

        jLabelList.get(12).setBounds(xBlack, screenSizeSeparation * (screenSize/2), boardBackgroundWidth, heightLabel*3);
        jLabelList.get(12).setFont(new Font(currentFont.getFontName(),currentFont.getStyle(),TDKScoreUtils.findFontSizeByJLabelSize(currentFont,jLabelList.get(12))));
        alignLabel(jLabelList.get(17), xBlack, screenSizeSeparation *(screenSize/2 + 1)+heightLabel*2 , boardBackgroundWidth, heightLabel, jLabelList.get(12).getFont().getSize()); //break label

        for (int i = 8; i < 13; i++) {
            jLabelList.get(i).setForeground(Color.WHITE);
        }

        jLabelList.get(12).setForeground(Color.YELLOW);
    }


    public void setScoreLabelBackground(Color color, JLabel label) {
        label.setBackground(color);
        label.setOpaque(true);
    }

    private void alignLabel(JLabel label, int x, int y, int width, int height, int fontSize) {
        label.setBounds(x, y, width, height);
        label.setFont(getFontWithSize(label, fontSize));

    }

    private Font getFontWithSize(JLabel label, int size) {
        Font currentFont = label.getFont();
        return new Font(currentFont.getFontName(), currentFont.getStyle(), size);
    }


    //866*363 654*550
    private void initializeComponents() {
        jLabelList = new ArrayList<>();
        jLabelTextList = new ArrayList<>();
        mainWindow = new JFrame("TKD Score");
        boardBackground = new BoardBackground();

        createLabelTextList();
        createLabelsList();
        mainWindow.requestFocusInWindow();
    }

    private void createLabelTextList(){
        jLabelTextList.add("Nombre Azul"); //blue contestant name 0
        jLabelTextList.add("0"); //blue contestant score 1
        jLabelTextList.add("GAM-JEOM"); //blue contestant faults name 2
        jLabelTextList.add("0"); //blue contestant faults Score 3
        jLabelTextList.add("Nombre Rojo");  //red contestant name 4
        jLabelTextList.add("0"); //red contestant score 5
        jLabelTextList.add("GAM-JEOM"); //red contestant faults name 6
        jLabelTextList.add("0"); //red contestant faults Score 7
        jLabelTextList.add("Match"); //8
        jLabelTextList.add("1"); //9
        jLabelTextList.add("Round");  //10
        jLabelTextList.add("1"); //11
        jLabelTextList.add(Objects.isNull(chronometer)?"02:00":chronometer.getMatchTime()); // round time 12
        jLabelTextList.add("WINNER"); // won name for blue 13
        jLabelTextList.add("0"); // won score for blue 14
        jLabelTextList.add("WINNER"); // won name for red 15
        jLabelTextList.add("0"); // won score for red 16
        jLabelTextList.add("Break"); // won score for red 17
    }

    private void createLabelsList(){
        for (String s : jLabelTextList) {
            jLabelList.add(new JLabel(s));
        }
    }


    private void setChronometerTimes() {

        String matchTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round", TDKScoreUtils.TIME_REGEX, "02:00");
        String breakTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso", TDKScoreUtils.TIME_REGEX, "01:00");
        jLabelList.get(12).setText(matchTime);
        chronometer = new Chronometer(jLabelList.get(12), matchTime, breakTime, jLabelList.get(17));
    }

    private void setLabelsName() {
        jLabelList.get(0).setName("blueName");
        jLabelList.get(1).setName("blueScore");
        jLabelList.get(3).setName("blueGam");
        jLabelList.get(4).setName("redName");
        jLabelList.get(5).setName("redScore");
        jLabelList.get(7).setName("redGam");
        jLabelList.get(9).setName("match");
        jLabelList.get(11).setName("round");
        jLabelList.get(12).setName("timer");
        jLabelList.get(14).setName("blueWin");
        jLabelList.get(16).setName("redWin");
        jLabelList.get(17).setName("break");
    }

    private void setAllVisible() {
        TDKMouseListener tdkMouseListener = new TDKMouseListener(chronometer,jLabelList );
        TKDScorePropertyChangeListener tkdScorePropertyChangeListener = new TKDScorePropertyChangeListener(jLabelList,chronometer);
        jLabelList.forEach(x -> {
            x.setVisible(true);
            x.setForeground(Color.BLACK);
            x.setHorizontalAlignment(JLabel.CENTER);
            x.setVerticalAlignment(JLabel.CENTER);
            x.addMouseListener(tdkMouseListener);
            x.addPropertyChangeListener(tkdScorePropertyChangeListener);
            boardBackground.add(x);
        });
    }

    private void setFrame() {
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setAlwaysOnTop(false);
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
        boardBackground.setLayout(null);
        mainWindow.add(boardBackground);
        mainWindow.setVisible(true);
        mainWindow.addKeyListener(new TKDKeyListener(jLabelList,jLabelTextList,chronometer,widthSeparation,heightLabel,Toolkit.getDefaultToolkit().getScreenSize()));
    }

}
