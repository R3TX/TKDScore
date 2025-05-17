import listeners.Chronometer;
import listeners.TDKMouseListener;
import listeners.TKDScorePropertyChangeListener;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


public class Board implements KeyListener {
    private JFrame mainWindow;
    private List<JLabel> jLabelList;
    private Chronometer chronometer;

    private Dimension screenSize;
    private int widthSeparation;
    private Font currentFont;
    private int heightLabel;
    private int defaultFontSize;


    private BoardBackground boardBackground;

    public Board() {
        initializeComponents();

        setChronometerTimes();
        setFrame();
        setAllVisible();
        screenSize();
        alignLabels();
        setLabelsName();
        addPropertyChange();
    }

    private void screenSize() {
        defaultFontSize = 52;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        widthSeparation = (int) (screenSize.getWidth() / 7);
        //widthSeparation = boardBackground.getWidth() / 7;
        currentFont = jLabelList.get(0).getFont();
        jLabelList.get(0).setFont(new Font(currentFont.getFontName(), currentFont.getStyle(), defaultFontSize));
        heightLabel = (int) jLabelList.get(0).getPreferredSize().getHeight();

    }

    private void alignLabels() {

        // Configuración de los labels azules
        alignLabel(jLabelList.get(0), 0, 0, widthSeparation * 3, heightLabel, defaultFontSize); // Nombre
        alignLabel(jLabelList.get(2), 0, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation * 2, heightLabel, defaultFontSize); // Faltas nombre
        alignLabel(jLabelList.get(3), widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation, heightLabel, defaultFontSize); // Faltas puntuación

        // Configuración de los labels rojos
        int xRojo = widthSeparation * 4;
        alignLabel(jLabelList.get(4), xRojo, 0, widthSeparation * 3, heightLabel, defaultFontSize); // Nombre
        alignLabel(jLabelList.get(6), xRojo, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation * 2, heightLabel, defaultFontSize); // Faltas nombre
        alignLabel(jLabelList.get(7), xRojo + widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 2)), widthSeparation, heightLabel, defaultFontSize); // Faltas puntuación

        //Configuration for score labels
        int widthSeparationScore = (int) (widthSeparation / 3);
        jLabelList.get(1).setBounds(widthSeparationScore, heightLabel, widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 3)));
        TDKScoreUtils.findFontSizeByJLabelSize(currentFont, jLabelList.get(1));
        alignLabel(jLabelList.get(5), xRojo + widthSeparationScore, heightLabel, widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 3)), jLabelList.get(1).getFont().getSize()); // Puntuación

        setScoreLabelBackground( TDKScoreUtils.BLUE_COLOR, jLabelList.get(1));
        setScoreLabelBackground(TDKScoreUtils.RED_COLOR, jLabelList.get(5));

        //winnings labels
        int boardBackgroundWidth = boardBackground.getWidth() / 7;
        //jLabelList.get(15).setForeground(Color.YELLOW);
        // jLabelList.get(13).setBorder(BorderFactory.createLineBorder(Color.BLACK));
        FontMetrics fontMetrics = jLabelList.get(13).getFontMetrics(currentFont);
        alignLabel(jLabelList.get(13), widthSeparationScore * 7, heightLabel, fontMetrics.charWidth('W') * 12, heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(14), widthSeparationScore * 7, heightLabel * 2, fontMetrics.charWidth('W') * 12, heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(15), boardBackgroundWidth * 4, heightLabel, fontMetrics.charWidth('W') * 12, heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(16), boardBackgroundWidth * 4, heightLabel * 2, fontMetrics.charWidth('W') * 12, heightLabel, defaultFontSize);

        //black line labels
        int xBlack = boardBackgroundWidth * 3;
        alignLabel(jLabelList.get(8), xBlack, heightLabel, widthSeparation, heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(9), xBlack, heightLabel * 2, widthSeparation, heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(10), xBlack, (int) (screenSize.getHeight() - (heightLabel * 4)), widthSeparation, heightLabel, defaultFontSize);
        alignLabel(jLabelList.get(11), xBlack, (int) (screenSize.getHeight() - (heightLabel * 3)), widthSeparation, heightLabel, defaultFontSize);

        alignLabel(jLabelList.get(12), xBlack, heightLabel * 6, widthSeparation, heightLabel, 80);

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
        mainWindow = new JFrame("TKD Score");
        boardBackground = new BoardBackground();

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
        jLabelList.add(new JLabel("02:00")); // round time 12
        jLabelList.add(new JLabel("WON")); // won name for blue 13
        jLabelList.add(new JLabel("0")); // won score for blue 14
        jLabelList.add(new JLabel("WON")); // won name for red 15
        jLabelList.add(new JLabel("0")); // won score for red 16


        // listeners.TKDKeyListener tkdKeyListener = new listeners.TKDKeyListener(jLabelList);
        mainWindow.addKeyListener(this);
        mainWindow.requestFocusInWindow();
    }

    private void addPropertyChange() {
        jLabelList.get(12).addPropertyChangeListener("text", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("text".equals(evt.getPropertyName())) {

                    // Perform actions based on the new text value
                }
            }
        });
    }



    private void setChronometerTimes() {

        String matchTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del round", TDKScoreUtils.TIME_REGEX);
        String breakTime = TDKScoreUtils.getInputText(TDKScoreUtils.INPUT_MESSAGE_TIME + "\n para la duración del descanso", TDKScoreUtils.TIME_REGEX);
        jLabelList.get(12).setText(matchTime);
        chronometer = new Chronometer(jLabelList.get(12), matchTime, breakTime);
    }

    private void setLabelsName() {
        jLabelList.get(0).setName("blueName");
        jLabelList.get(3).setName("blueGam");
        jLabelList.get(4).setName("redName");
        jLabelList.get(7).setName("redGam");
        jLabelList.get(9).setName("match");
        jLabelList.get(11).setName("round");
        jLabelList.get(12).setName("timer");
        jLabelList.get(14).setName("blueWin");
        jLabelList.get(16).setName("redWin");
    }

    private void setAllVisible() {
        TDKMouseListener tdkMouseListener = new TDKMouseListener();
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
        //boardBackground.setResizable(false);
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setMinimumSize(new Dimension(200, 200));
        boardBackground.setLayout(null);
        mainWindow.add(boardBackground);
        mainWindow.setVisible(true);
    }

    private void scorePlus(JLabel jLabel, int plus) {
        int currentScore = Integer.parseInt(jLabel.getText()) + plus;
        jLabel.setText(String.valueOf(currentScore));
        if (currentScore > 9) {
            int widthSeparationScore = (widthSeparation / 3);
            jLabelList.get(1).setBounds(widthSeparationScore, heightLabel, widthSeparation * 2, (int) (screenSize.getHeight() - (heightLabel * 3)));
            jLabel.setFont(new Font(jLabel.getFont().getFontName(), jLabel.getFont().getStyle(), TDKScoreUtils.findFontSizeByJLabelSize(jLabel.getFont(), jLabel)));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyChar();
        switch (keyCode) {
            case '1':
                scorePlus(jLabelList.get(1), 1);
                break;
            case '3':
                scorePlus(jLabelList.get(5), 1);
                break;
            case '4':
                scorePlus(jLabelList.get(1), 2);
                break;
            case '6':
                scorePlus(jLabelList.get(5), 2);
                break;
            case '7':
                scorePlus(jLabelList.get(1), 3);
                break;
            case '9':
                scorePlus(jLabelList.get(5), 3);
                break;
            case '-':
                scorePlus(jLabelList.get(5), 1);
                scorePlus(jLabelList.get(3), 1);
                break;
            case '+':
                scorePlus(jLabelList.get(1), 1);
                scorePlus(jLabelList.get(7), 1);
                break;
            case '/':
                int confirmDialog = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas continuar?", "Confirmación", JOptionPane.YES_NO_OPTION);

                // Procesar la respuesta del usuario
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    mainWindow.dispose();
                    initializeComponents();
                    setFrame();
                    setAllVisible();
                    alignLabels();
                }
                break;
            case KeyEvent.VK_ENTER:
                chronometer.startStopTimer();
                break;


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
