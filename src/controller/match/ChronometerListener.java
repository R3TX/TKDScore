package controller.match;

/**
 * Interface de callback para notificar al controlador sobre eventos del cronómetro.
 */
public interface ChronometerListener {
    /**
     * Llamado cada segundo para actualizar la vista con el tiempo actual.
     * @param time El tiempo actual en formato String (ej: "01:30").
     */
    void onTimeUpdate(String time);

    /**
     * Llamado cuando el contador llega a 00:00.
     */
    void onTimeOut();
}