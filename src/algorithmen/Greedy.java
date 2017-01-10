package algorithmen;

import informaticup.Algorithmus;
import informaticup.Weltfunktion;
import informaticup.datenstruktur.*;

/**
 * Algorithmus: Heuristik: Der Greedy-Algorithmus wählt zu jedem Zeitpunkt den Schritt, der zur Zeit am meisten Erfolg verspricht.
 */
public class Greedy implements informaticup.Algorithmus {

    // Zustand ohne gesetzte Automaten
    private Zustand _zustand;
    // Zeiger auf die Welt
    private Welt _welt;
    // Anzahl der zu setzenden Automaten
    private int _anzahlDerAutomaten;
    private int _sucheParameter = 0;
    // Wie viele Automaten wurden bereits gesetzt?
    private int _automatenGesetzt = 0;
    // Soll die Stichproben-Suche oder die vollständige Suche benutzt werden?
    private boolean _stichproben = false;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _progressDelegate = null;
    // Gewichtskarte (von Zustand)
    private Karte _gewichtskarte;

    /**
     * Konstruktur der Klasse: Initialisiert die Variablen.
     * @param weltfunktion Weltfunktion
     * @param zustand Zustand ohne Automaten
     */
    public Greedy(Weltfunktion weltfunktion, Zustand zustand) {
        _welt = weltfunktion.getWelt();
        _zustand = zustand;
        _gewichtskarte = zustand.getGewichtskarte();
        _anzahlDerAutomaten = _welt.getSollAnzahlAutomaten();

        // DEBUG: Anzahl der Automaten ausgeben
        // System.out.println("Greedy: Es sollen " + _anzahlDerAutomaten + " gesetzt werden.");
    }

    /**
     * Legt fest, ob Stichproben-Suche oder vollständige Suche verwendet werden soll.
     * @param enabled true=Stichproben-Suche
     */
    public void setStichproben(boolean enabled) {
        _stichproben = enabled;
    }

    /**
     * Startet die Berechnung des Algorithmus.
     * @param progressDelegate GUI: Fortschrittsbalken
     * @throws AlgorithmusException Ausnahme, die geworfen wird, falls der Algorithmus fehlschlägt
     */
    public void Berechne(Algorithmus.ProgressCallbackDelegate progressDelegate) throws AlgorithmusException {
        // GUI: Fortschrittsbalken initialisieren
        this._progressDelegate = progressDelegate;
        this._progressDelegate.fortschritt(0, 0, 100);

        // Setze die Automaten schrittweise
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            this._progressDelegate.fortschritt(i, 0, _anzahlDerAutomaten);
            // DEBUG: Statusnachricht
            // System.out.println("Greedy: Setzte Automat: " + i + "/" + _anzahlDerAutomaten + ".");

            SetzeNaechstenAutomaten();
            
            this._progressDelegate.formRepaint();
        }

        // GUI: Fortschrittsbalken aktualisieren
        this._progressDelegate.fortschritt(_anzahlDerAutomaten, 0, _anzahlDerAutomaten);
    }

    /**
     * Gibt die Lösung des Algorithmus zurück.
     * @return Zustand mit Automaten
     */
    public Zustand getErgebnis() {
        return _zustand;
    }

    /**
     * Setzt den nächsten Automaten
     * @throws AlgorithmusException Ausnahme
     */
    private void SetzeNaechstenAutomaten() throws AlgorithmusException {
        // Ermittle die beste Stelle für den neuen Automaten
        Koordinate koordinate = FindeBestePosition();

        // Gesperrte Automaten nicht verändern
        if (_zustand.getAutomaten()[_automatenGesetzt] == null || !_zustand.getAutomaten()[_automatenGesetzt].getGesperrt()) {
            _zustand.setAutomat(_automatenGesetzt, new Automat(koordinate, _welt.getRadiusAutomaten()));
        }

        // DEBUG: Statusnachricht
        // System.out.println("Automat gesetzt bei [" + koordinate.getX() + "][" + koordinate.getY() + "] mit Gewicht " + _zustand.getGewichtskarte().getWert(koordinate.getX(), koordinate.getY()));

        _automatenGesetzt++;
    }

    /**
     * Sucht die beste Position für den nächsten Automaten.
     * @return Koordinate auf der Karte
     * @throws AlgorithmusException Ausnahme
     */
    private Koordinate FindeBestePosition() throws AlgorithmusException {
        if (_sucheParameter == 0) {
            if (_stichproben) {
                // Benutze Stichproben-Suche mit dem Parameter 0,1
                return FindeBestePositionStichprobenSuche(0.1);
            }

            // Benutze vollständige Suche
            return FindeBestePositionVollstaendigeSuche();
        }

        return null;
    }

    /**
     * Sucht die beste Position mit der Stichproben-Suche.
     * @param rate Legt fest, wie viele Stellen überprüft werden sollen
     * @return Koordinate auf der Karte
     * @throws AlgorithmusException Ausnahme
     */
    private Koordinate FindeBestePositionStichprobenSuche(double rate) throws AlgorithmusException {
        Koordinate besterPixel = null;
        long gewichtBesterPixel = -1;

        // Algorithmus: Teile die Karte in zwei Hälften auf. Nimm eine
        // Stichprobe von n Pixeln aus jeder Hälfte. Fahre mit der "besseren"
        // Hälfte rekursiv fort.

        // Größe des aktuell betrachteten Bereichs, zwei Koordinaten: links oben, rechts unten
        Koordinate aktuellerBereichLeftTop = new Koordinate(_gewichtskarte.getMinx(), _gewichtskarte.getMiny());
        Koordinate aktuellerBereichRightBottom = new Koordinate(_gewichtskarte.getMaxx(), _gewichtskarte.getMaxy());

        while (true) {
            // Breite und Höhe des Gebietes berechnen
            int breiteX = aktuellerBereichRightBottom.getX() - aktuellerBereichLeftTop.getX();
            int breiteY = aktuellerBereichRightBottom.getY() - aktuellerBereichLeftTop.getY();

            if (breiteX < 2 && breiteY < 2) {
                // Abbruchbedingung: Der betrachtete Bereich besteht nur noch aus einem einzelnen Pixel
                break;
            }

            Koordinate teil1LeftTop, teil1RightBottom, teil2LeftTop, teil2RightBottom;

            // Bestimme, ob in x- oder y-Richtung geteilt werden soll. Es wird immer die größere "Achse" geteilt.
            // Berechne die Eckkoordinaten der neuen Teile
            if (breiteX < breiteY) {
                // In Y-Richtung teilen
                teil1LeftTop = aktuellerBereichLeftTop;
                teil1RightBottom = new Koordinate(teil1LeftTop.getX() + breiteX, teil1LeftTop.getY() + breiteY / 2);
                teil2LeftTop = new Koordinate(teil1LeftTop.getX(), teil1LeftTop.getY() + breiteY / 2);
                teil2RightBottom = aktuellerBereichRightBottom;
            } else {
                // In X-Richtung teilen
                teil1LeftTop = aktuellerBereichLeftTop;
                teil1RightBottom = new Koordinate(teil1LeftTop.getX() + breiteX / 2, teil1LeftTop.getY() + breiteY);
                teil2LeftTop = new Koordinate(teil1LeftTop.getX() + breiteX / 2, teil1LeftTop.getY());
                teil2RightBottom = aktuellerBereichRightBottom;
            }

            // Stichprobengröße aus Paramter und Größe des betrachteten Gebietes berechnen (mind. 1 Pixel)
            int stichprobenGroesse = (int) (rate * breiteX * breiteY) + 1;

            // DEBUG: Stichprobengröße ausgeben
            // System.out.println("Aktuelle Stichprobengröße: " + stichprobenGroesse);

            // Stichproben aus Teil 1
            long summeTeil1 = 0;
            for (int i = 0; i < stichprobenGroesse; i++) {
                // Stichprobe entnehmen
                int x = Zufallszahl(teil1LeftTop.getX(), teil1RightBottom.getX());
                int y = Zufallszahl(teil1LeftTop.getY(), teil1RightBottom.getY());

                if (PunktZulaessig(new Koordinate(x, y))) {
                    // Nur zulässige Punkte überhaupt betrachten
                    // Punkte aufsummieren
                    summeTeil1 += _gewichtskarte.getWert(x, y);

                    // Prüfe Stichprobe
                    if (_zustand.getGewichtskarte().getWert(x, y) > gewichtBesterPixel) {
                        // Besserer Punkt gefunden
                        gewichtBesterPixel = _gewichtskarte.getWert(x, y);
                        besterPixel = new Koordinate(x, y);
                    }
                }

            }

            // Stichproben aus Teil 2
            long summeTeil2 = 0;
            for (int i = 0; i < stichprobenGroesse; i++) {
                // Stichprobe entnehmen
                int x = Zufallszahl(teil2LeftTop.getX(), teil2RightBottom.getX());
                int y = Zufallszahl(teil2LeftTop.getY(), teil2RightBottom.getY());

                if (PunktZulaessig(new Koordinate(x, y))) {
                    summeTeil2 += _gewichtskarte.getWert(x, y);

                    // Prüfe Stichprobe
                    if (_zustand.getGewichtskarte().getWert(x, y) > gewichtBesterPixel) {
                        // Besserer Punkt gefunden
                        gewichtBesterPixel = _gewichtskarte.getWert(x, y);
                        besterPixel = new Koordinate(x, y);
                    }
                }
            }

            // Suche den besseren Teil heraus
            if (summeTeil1 > summeTeil2) {
                // Mit Teil 1 fortfahren
                aktuellerBereichLeftTop = teil1LeftTop;
                aktuellerBereichRightBottom = teil1RightBottom;
            } else {
                // Mit Teil 2 fortfahren
                aktuellerBereichLeftTop = teil2LeftTop;
                aktuellerBereichRightBottom = teil2RightBottom;
            }
        }

        if (gewichtBesterPixel > -1) {
            return besterPixel;
        } else {
            // Es wurde kein sinnvoller Punkt für den Automaten gefunden
            throw new AlgorithmusException(AlgorithmusException.AlgorithmusErrorCode.KEIN_ZULAESSIGER_PUNKT);
        }
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
     * Sucht die beste Position für den nächsten Automaten mit vollständger Suche.
     * @return Koordinate auf der Karte
     * @throws AlgorithmusException Ausnahme
     */
    private Koordinate FindeBestePositionVollstaendigeSuche() throws AlgorithmusException {
        // Besten Pixel ermitteln
        Koordinate besterPixel = null;
        long gewichtBesterPixel = -1;

        // Überprüfe alle Pixel und wähle den aktuell besten Pixel aus
        // Iteriere über alle Pixel in x-Richtung       
        for (int x = _gewichtskarte.getMinx(); x < _gewichtskarte.getMaxx(); x++) {
            // Iteriere über alle Pixel in y-Richtung
            for (int y = _gewichtskarte.getMiny(); y < _gewichtskarte.getMaxy(); y++) {
                long gewichtPixel = _gewichtskarte.getWert(x, y);

                if (gewichtPixel > gewichtBesterPixel) {
                    if (PunktZulaessig(new Koordinate(x, y))) {
                        // Es wurde ein neuer bester Pixel gefunden, der auch noch zulässig ist, d.h. hier darf ein Automat platziert werden
                        besterPixel = new Koordinate(x, y);
                        gewichtBesterPixel = gewichtPixel;
                    }
                }
            }
        }

        if (gewichtBesterPixel > -1) {
            // Besten gefundenen Pixel zurückgeben
            return besterPixel;
        } else {
            // Es gibt keine sinnvolle oder zulässige Position für einen neuen Automaten
            throw new AlgorithmusException(AlgorithmusException.AlgorithmusErrorCode.KEIN_ZULAESSIGER_PUNKT);
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

    public int getFortschritt() {
        return 0;
    }

    public String getStatusText() {
        return "";
    }
}
