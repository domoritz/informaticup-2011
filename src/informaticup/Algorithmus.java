package informaticup;

import informaticup.datenstruktur.Zustand;
import algorithmen.AlgorithmusException;

/**
 * Interface, das von allen Algorithmen implementiert werden muss.
 */
public interface Algorithmus {

    /**
     * Startet den Algorithmus und damit die Berechnung.
     * @param delegate GUI: Fortschrittsbalken
     * @throws AlgorithmusException Ausnahme, die geworfen wird, wenn der Algorithmus fehlschlägt
     */
    public void Berechne(ProgressCallbackDelegate delegate) throws AlgorithmusException;

    /**
     * Gibt das Ergebnis der Berechnung zurück.
     * @return Zustand mit positionierten Automaten
     */
    public Zustand getErgebnis();

    /**
     * GUI: Fortschrittsbalken
     */
    public interface ProgressCallbackDelegate {

        /**
         * GUI: Aktualisiert den Fortschritt am Fortschrittsbalken.
         * @param pos aktueller Fortschritt
         * @param min minimaler Fortschrittswert
         * @param max maximaler Fortschrittswert
         */
        void fortschritt(int pos, int min, int max);
        void formRepaint();
    }
    /**
     * GUI: Fortschrittsbalken
     */
    public ProgressCallbackDelegate _progressDelegate = null;
}
