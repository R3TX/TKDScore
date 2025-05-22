package listeners;

import javax.swing.*;

public class Chronometer {
    private int minutes;
    private int seconds;
    private String matchTime;
    private String breakTime;
    private boolean IS_BREAK_TIME;
    private JLabel timerLabel;

    private final Timer timer;

    public Chronometer(JLabel timerLabel, String matchTime, String breackTime) {
        this.timerLabel = timerLabel;
        this.matchTime = matchTime;
        this.breakTime = breackTime;
        this.minutes = Integer.parseInt(timerLabel.getText().split(":")[0]);
        this.seconds = Integer.parseInt(timerLabel.getText().split(":")[1]);
        this.timer = new Timer(1000, e -> initTimer());
    }

    public void initTimer() {
        updateTime();
        updateLabel();
    }

    public void updateTime() {
        seconds--;

        if (minutes == 0 && seconds < 0) {

            if (!IS_BREAK_TIME) {
                restartTime(breakTime);
                timer.start();
                IS_BREAK_TIME = true;
            } else {
                timer.stop();
                restartTime(matchTime);
                IS_BREAK_TIME = false;
            }
        }
        if (seconds < 0) {
            seconds = 59;
            minutes--;
        }
    }

    public void updateLabel() {
        String second = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
        timerLabel.setText(minutes + ":" + second);
    }

    public void startStopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }

    public void restartTime(String time) {
        if (timer.isRunning()) {
            timer.stop();
        }

        timerLabel.setText(time);
        this.minutes = Integer.parseInt(time.split(":")[0]);
        this.seconds = Integer.parseInt(time.split(":")[1]);
    }

    public String getBreakTime() {
        return breakTime;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public void setIsBreakTime(boolean isBreakTime) {
        IS_BREAK_TIME = isBreakTime;
    }

    public boolean isIS_BREAK_TIME() {
        return IS_BREAK_TIME;
    }
}
