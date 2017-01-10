package algorithmen;

import informaticup.Algorithmus;
import informaticup.Weltfunktion;
import informaticup.datenstruktur.*;

/**
 * Algorithmus: Heurisitik: Greedy-Algorithmus mit Stichproben-Suche.
 */
public class GreedyStichprobe implements informaticup.Algorithmus {

    // Zustand ohne bereits gesetzte Automaten
    private Zustand _zustand;
    // Zeiger auf die Weltfuntkion
    private Weltfunktion _weltfunktion;
    // Instanz der Greedy-Klasse (-> Doku)
    private Greedy _greedy;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _progressDelegate = null;

    /**
     * Konstruktor der Algorithmus-Klasse: Variablen initialisieren.
     * @param weltfunktion Weltfunktion
     * @param zustand Zustand ohne Automaten
     */
    public GreedyStichprobe(Weltfunktion weltfunktion, Zustand zustand) {
        _weltfunktion = weltfunktion;
        _zustand = zustand;
        _greedy = new Greedy(weltfunktion, zustand);
        _greedy.setStichproben(true);
    }

    /**
     * Startet den eigentlichen Algorithmus aus der Greedy-Klasse.
     * @param progressDelegate GUI: Fortschrittsbalken
     * @throws AlgorithmusException Ausnahme
     */
    public void Berechne(Algorithmus.ProgressCallbackDelegate progressDelegate) throws AlgorithmusException {
        this._progressDelegate = progressDelegate;
        // Algorithmus aus der Greedy-Klasse aufrufen
        _greedy.Berechne(progressDelegate);
    }

    /**
     * Gibt das Ergebnis mit den Automaten zurück.
     * @return Zustand mit Automaten
     */
    public Zustand getErgebnis() {
        return _greedy.getErgebnis();
    }

    public int getFortschritt() {
        return 0;
    }

    public String getStatusText() {
        return "";
    }
}
