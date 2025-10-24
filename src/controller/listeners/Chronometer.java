package controller.listeners;

import controller.match.ChronometerListener;

import javax.swing.*;
import java.awt.*;

public class Chronometer {
    private int minutes;
    private int seconds;
    private String matchTime;
    private String breakTime;

    private final Timer timer;
    private ChronometerListener listener; // Nuevo: Callback para notificar al Controller

    /**
     * Constructor refactorizado. Ya no recibe JLabels.
     * Recibe el Listener que se encarga de la lógica de fin de tiempo.
     *
     * @param breakTime   Tiempo por defecto del descanso.
     * @param listener    El MatchController que implementa ChronometerListener.
     * @param matchTime   Tiempo por defecto del round.
     */

    public Chronometer(ChronometerListener listener, String matchTime, String breakTime) {
        this.matchTime = matchTime;
        this.breakTime = breakTime;
        // Inicializa el tiempo con el valor dado (usualmente el Match Time)
        parseTime(matchTime);
        this.listener = listener;
        this.timer = new Timer(1000, e -> initTimer());
    }

    public void initTimer() {
        // 1. Actualiza el tiempo interno (minutos y segundos)
        updateTime();
        // 2. Notifica al controlador el nuevo tiempo para que se actualice la vista
        listener.onTimeUpdate(getCurrentTimeDisplay());
    }

    public void updateTime() {
        seconds--;

        if (seconds < 0) {
            seconds = 59;
            minutes--;
        }

        if (minutes < 0) {
            minutes = 0;
            seconds = 0;
            timer.stop(); // Detener el motor de tiempo

            // Llama al MatchController para que maneje el evento de fin de tiempo
            listener.onTimeOut();
        }
    }

        private String getCurrentTimeDisplay() {
            String second = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
            String minute = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
            return minute + ":" + second;
        }

    public void startStopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }
    public void stopTime(){
            timer.stop();
    }

    public void restartTime(String time) {
       // No detenemos el timer aquí. Es responsabilidad del controlador
        // si quiere detenerlo y luego reiniciarlo.
        // Si el controlador llama a restartTime(), es para reajustar el valor.

        parseTime(time);

        // Notifica al controlador el nuevo valor inicial para la vista
        listener.onTimeUpdate(getCurrentTimeDisplay());
    }

    private void parseTime(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            this.minutes = Integer.parseInt(parts[0]);
            this.seconds = Integer.parseInt(parts[1]);
        } else {
            // Manejo de error o valor por defecto
            this.minutes = 0;
            this.seconds = 0;
        }
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

    public boolean isRunning(){
        return timer.isRunning();
    }

    public ChronometerListener getListener() {
        return listener;
    }

    public void setListener(ChronometerListener listener) {
        this.listener = listener;
    }
}
