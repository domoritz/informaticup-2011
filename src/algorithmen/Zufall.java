package algorithmen;

import informaticup.Algorithmus;
import informaticup.datenstruktur.*;
import informaticup.Weltfunktion;

/**
 * "Algorithmus": Verteilt die Automaten zufällig auf der Landkarte.
 */
public class Zufall implements informaticup.Algorithmus {

    // Zustand ohne Automaten
    private Zustand _zustand;
    // Anzahl der zu setzenden Automaten
    private int _anzahlDerAutomaten;
    // Anzahl der bereits gesetzten Automaten
    private int _automatenGesetzt = 0;
    // Zeiger auf die Welt
    private Welt _welt;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _progressDelegate = null;

    /**
     * Konstruktor: Initialisiert die Variablen.
     * @param weltfunktion Weltfunktion
     * @param zustand Zustand (ohne Automaten)
     */
    public Zufall(Weltfunktion weltfunktion, Zustand zustand) {
        _welt = weltfunktion.getWelt();
        _anzahlDerAutomaten = _welt.getSollAnzahlAutomaten();
        _zustand = zustand;
    }

    /**
     * Startet den Algorithmus.
     */
    public void Berechne(Algorithmus.ProgressCallbackDelegate progressDelegate) throws AlgorithmusException {
        this._progressDelegate = progressDelegate;
        EroeffnungZufall(_anzahlDerAutomaten);
    }

    /**
     * Gibt das Ergebnis des Algorithmus zurück.
     * @return Zustand mit Automaten
     */
    public Zustand getErgebnis() {
        return _zustand;
    }

    /**
     * Primitives Eröffnungsverfahren, das alle Automaten zufällig verteilt.
     * @param anzahlDerAutomaten Anzahl der zu platzierenden Automaten
     */
    private void EroeffnungZufall(int anzahlDerAutomaten) throws AlgorithmusException {
        //_zustand = new Zustand(new Weltfunktion(_welt), anzahlDerAutomaten);
        System.out.println("Größe der Welt: x=(" + _welt.getPixelkarte().getMinx() + ", " + _welt.getPixelkarte().getMaxx() + ")");
        System.out.println("Größe der Welt: y=(" + _welt.getPixelkarte().getMiny() + ", " + _welt.getPixelkarte().getMaxy() + ")");

        this._progressDelegate.fortschritt(0, 0, anzahlDerAutomaten);

        for (int i = 0; i < anzahlDerAutomaten; i++) {
            // Gesperrte Automaten nicht verändern
            if (_zustand.getAutomaten()[i] == null || !_zustand.getAutomaten()[i].getGesperrt()) {
                // Setze Automat zufällig
                Koordinate zufallskoordinate = null;

                for (int j = 0; j < 101; j++) {
                    // 100 Versuche, den Automaten zu setzen
                    zufallskoordinate = new Koordinate(Zufallszahl(_welt.getPixelkarte().getMinx(), _welt.getPixelkarte().getMaxx()), Zufallszahl(_welt.getPixelkarte().getMiny(), _welt.getPixelkarte().getMaxy()));

                    if (PunktZulaessig(zufallskoordinate)) {
                        break;
                    } else {
                        zufallskoordinate = null;
                    }
                }

                if (zufallskoordinate != null) {
                    _zustand.setAutomat(i, new Automat(zufallskoordinate, _welt.getRadiusAutomaten()));
                    _automatenGesetzt++;
                } else {
                    // Es wurde kein sinnvoller Punkt für den Automaten gefunden
                    throw new AlgorithmusException(AlgorithmusException.AlgorithmusErrorCode.KEIN_ZULAESSIGER_PUNKT);
                }
            }
            this._progressDelegate.fortschritt(i + 1, 0, anzahlDerAutomaten);
            this._progressDelegate.formRepaint();
        }
    }

    /**
     * Überprüft, ob an der angegebenen Stelle ein Automat platziert werden darf. Automatenkreise dürfen sich nicht überschneiden.
     * @param koordinate Position des neuen Automaten
     */
    private boolean PunktZulaessig(Koordinate koordinate) {
        // Iteriere über alle bereits gesetzten Automaten
        for (int i = 0; i < _automatenGesetzt; i++) {
            Koordinate aktuellerAutomat = _zustand.getAutomat(i).getPosition();
            int abstandX = aktuellerAutomat.getX() - koordinate.getX();
            int abstandY = aktuellerAutomat.getY() - koordinate.getY();

            if (abstandX * abstandX + abstandY * abstandY < 4 * _welt.getRadiusAutomaten() * _welt.getRadiusAutomaten()) {
                // Automatenkreise überschneiden sich, nicht zulässig!
                return false;
            }
        }

        return true;
    }

    /**
     * Erzeugt eine Zufallszahl im Intervall [min, max]
     * @param min Untere Schranke
     * @param max Obere Schranke
     * @return Zufallszahl
     */
    private int Zufallszahl(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public int getFortschritt() {
        return (int) (((float) _automatenGesetzt / (float) _anzahlDerAutomaten) * 100.0);
    }

    public String getStatusText() {
        return _automatenGesetzt + " / " + _anzahlDerAutomaten + " zufällig gesetzt";
    }
}
