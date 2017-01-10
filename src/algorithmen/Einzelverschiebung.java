package algorithmen;

import informaticup.Algorithmus;
import informaticup.datenstruktur.*;
import informaticup.Weltfunktion;

/**
 * Algorithmus: Verbessert eine bestehende Lösung, indem versucht wird, jeden Automaten um wenige Einheiten zu verschieben.
 *
 */
public class Einzelverschiebung implements informaticup.Algorithmus {

    // Zustand mit bereits platzierten Automaten
    private Zustand _zustand;
    // Anzahl der platzierten Automaten
    private int _anzahlDerAutomaten;
    // GUI: Aktuell bearbeiteter Automat (Fortschrittbalken)
    private int _aktuellerAutomat = 1;
    // GUI: Aktueller Durchlauf (Fortschrittsbalken)
    private int _aktuellerDurchlauf = 1;
    // Güte der aktuellen Lösung
    private long _guete;
    // Zeiger auf die Welt
    private Welt _welt;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _progressDelegate = null;

    /**
     * Konstruktor für die Algorithmus-Klasse. Initialisiert die Variablen.
     * @param weltfunktion Weltfunktion
     * @param zustand Zustand mit bereits platzierten Automaten
     */
    public Einzelverschiebung(Weltfunktion weltfunktion, Zustand zustand) {
        _welt = weltfunktion.getWelt();
        _anzahlDerAutomaten = _welt.getSollAnzahlAutomaten();
        _zustand = zustand;
    }

    /**
     * Berechnet die Güte des Zustandes. Je höher dieser Wert ist, desto besser ist die Lösung.
     * @return Güte
     */
    private int BerechneZustandsGuete() {
        // Die Punktezahlen aller Automaten aufaddieren, die Automaten dürfen sich NICHT überschneiden!
        int rueckgabe = 0;

        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            rueckgabe += _zustand.getGewichtskarte().getWert(_zustand.getAutomat(i).getPosition().getX(), _zustand.getAutomat(i).getPosition().getY());
        }

        return rueckgabe;
    }

    /**
     * Mischt ein int-Array durch, indem jedes Element mit einem zufälligen Element vertauscht wird.
     * @param array int-Array
     * @return randomisiertes int-Array
     */
    private int[] ArrayMischen(int[] array) {
        // Alle Arrayitems durchgehen
        for (int i = 0; i < array.length; i++) {
            int swapIndex = (i + Zufallszahl(0, array.length - 1)) % array.length;
            int swapItem = array[i];
            array[i] = array[swapIndex];
            array[swapIndex] = swapItem;
        }

        // Rückgabe eigentlich nicht notwendig
        return array;
    }

    /**
     * Erzeugt eine Zufallszahl im Intervall [min, max]
     * @param min Untere Schranke
     * @param max Obere Schranke
     * @return Zufallszahl
     */
    private int Zufallszahl(int min, int max) {
        return min + ((int) ((Math.random() * max) + 1));
    }

    /**
     * Startet den eigentlichen Einzelverschiebungs-Algorithmus.
     * @param progressDelegate GUI: Fortschrittsbalken
     */
    public void Berechne(Algorithmus.ProgressCallbackDelegate progressDelegate) {
        this._progressDelegate = progressDelegate;

        // Indexarray (Indizes der Automaten) anlegen
        int[] automat = new int[_anzahlDerAutomaten];
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            automat[i] = i;
        }

        // DEBUG: Anzahl der Automaten ausgeben
        // System.out.println("Einzelverschiebung: Es gibt " + _anzahlDerAutomaten + " Automaten.");

        // Wende das Verfahren dreimal hintereinander an
        for (int i = 0; i < 3; i++) {
            // Reihenfolge der Bearbeitung der Automaten randomisieren
            automat = ArrayMischen(automat);
            // GUI: aktuellen Durchlauf setzen
            _aktuellerDurchlauf = i + 1;

            // Verschiebe jeden Automaten, soweit möglich, maximale Verschiebung: eine Radiusbreite in jede Richtung
            for (int s = 0; s < _anzahlDerAutomaten; s++) {
                // Tatsächlichen Index des ausgewählten Automaten ermittlen (da randomisiertes Array)
                int j = automat[s];
                // GUI: Wie viele Automaten wurden bereits bearbeitet?
                _aktuellerAutomat = s + 1;

                // Gesperrte Automaten nicht verschieben
                if (!_zustand.getAutomat(j).getGesperrt()) {
                    // In x-Richtung
                    for (int x = -_welt.getRadiusAutomaten(); x < _welt.getRadiusAutomaten() + 1; x++) {
                        // In y-Richtung
                        for (int y = -_welt.getRadiusAutomaten(); y < _welt.getRadiusAutomaten() + 1; y++) {
                            int neuX = _zustand.getAutomat(j).getPosition().getX() + x;
                            int neuY = _zustand.getAutomat(j).getPosition().getY() + y;

                            // Neue Güte berechnen
                            long neueGuete = _guete - _zustand.getGewichtskarte().getWert(_zustand.getAutomat(j).getPosition()) + _zustand.getGewichtskarte().getWert(neuX, neuY);
                            // Überprüfe, ob die Gesamtlösung durch diese Veränderung verbessert wird
                            if (neueGuete > _guete) {
                                // Prüfe, ob die neuen Positionen der Automaten innerhalb der Karte sind
                                if (neuX >= _welt.getPixelkarte().getMinx() && neuX <= _welt.getPixelkarte().getMaxx() && neuY >= _welt.getPixelkarte().getMiny() && neuY <= _welt.getPixelkarte().getMaxy()) {
                                    // Prüfe, ob die Automaten sich überschneiden
                                    boolean schnitt = false;
                                    for (int k = 0; k < _anzahlDerAutomaten; k++) {
                                        if (k != j) {
                                            // Nicht auf Schnitt mit den aktuellen Automaten prüfen
                                            Koordinate koordinate = _zustand.getAutomat(k).getPosition();
                                            int deltaX = koordinate.getX() - neuX;
                                            int deltaY = koordinate.getY() - neuY;

                                            if (deltaX * deltaX + deltaY * deltaY < 4 * _welt.getRadiusAutomaten() * _welt.getRadiusAutomaten()) {
                                                // Schnittpunkt mit einem anderen Automaten (Pythagoras)
                                                schnitt = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!schnitt) {
                                        // Änderung verbessert das Ergebnis und ist zulässig, also Änderung übernehmen
                                        _zustand.getAutomat(j).setPosition(new Koordinate(neuX, neuY));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gibt die beste gefundene Lösung (Zustand) zurück.
     * @return Zustand
     */
    public Zustand getErgebnis() {
        return _zustand;
    }

    public int getFortschritt() {
        return (int) (((float) _aktuellerDurchlauf - 1.0) * 33.0 + ((float) _aktuellerAutomat / (float) _anzahlDerAutomaten) * 33.0);
    }

    public String getStatusText() {
        return "Abschlussoptimierung: Durchlauf " + _aktuellerDurchlauf + " / 3";
    }
}
