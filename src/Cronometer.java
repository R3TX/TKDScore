import javax.swing.*;

public class Cronometer {
    private int minutos;
    private int segundo;
    private JLabel timerLabel;

    private Timer timer;

    public Cronometer(JLabel timerLabel) {
        this.timerLabel = timerLabel;

        this.minutos = Integer.parseInt(timerLabel.getText().split(":")[0]);
        this.segundo = Integer.parseInt(timerLabel.getText().split(":")[1]);
        this.timer = new Timer(1000,e -> initTimer());
    }

    public void initTimer(){
        updateTime();
        updateLabel();
    }
    public void updateTime(){
        segundo--;
        if(segundo<0){
            segundo=59;
            minutos--;
        }
        if(minutos==0){
            timer.stop();

        }
    }

    public void updateLabel(){
        timerLabel.setText(minutos+":"+segundo);
    }

    public void startStopTimer(){
        if(timer.isRunning()){
            timer.stop();
        }else {
            timer.start();
        }
    }

    public void restarTimer(String time){
        if(timer.isRunning()){
            timer.stop();
        }
        this.minutos = Integer.parseInt(time.split(":")[0]);
        this.segundo = Integer.parseInt(time.split(":")[1]);
        this.timer = new Timer(1000,e -> initTimer());
    }
}
