package algorithmen;

import informaticup.Algorithmus;
import informaticup.Weltfunktion;
import informaticup.datenstruktur.*;

/**
 * Algorithmus: Metaheuristik/Optimierungsverfahren: Simulierte Abkühlung.
 */
public class Abkuehlung implements informaticup.Algorithmus {

    // Speichert den aktuellen Zustand. Anfangs wird ein Zustand übergeben, der bereits eine gültige Lösung darstellt (Initiallösung)
    private Zustand _zustand;
    // Zeiger auf die Welt
    private Welt _welt;
    // Aktuelle Temperatur T
    private int _temperatur = 400;
    // Initialtemperatur zu Beginn des Algorithmus T_{initial}
    private int _initialTemperatur = 400;
    // Güte der aktuellen Lösung (Zustand)
    private long _guete;
    // Anzahl der gesetzten Automaten
    private int _anzahlDerAutomaten;
    // Breite der Welt
    private int _weltBreite;
    // Höhe der Welt
    private int _weltHoehe;
    // Anzahl der Durchläufe (zu generierende Nachbarn pro Temperatur)
    private int _anzahlDerDurchlaeufe = 300;
    // Anzahl der elementaren Operationen pro Nachbar
    private int _anzahlDerVeraenderungen = 5;
    // Hängen die elementaren Operationen von der aktuellen Temperatur ab? (-> Doku: Variable Größe der elementaren Operationen)
    private boolean _aenderungenInAbhaengigkeitVonTemperatur = false;
    // Letzter Zustand, zum Rückgängigmachen eines Durchlaufes (Vorgänger des Nachbarn)
    private Koordinate[] _letzterZustand;
    // Nur für Debug-Zwecke: Durchschnittliche Energiedifferenz \Delta E
    private long _durchschnittlicheEnergiedifferenz = 0;
    // Nur für Debug-Zwecke: Zur Berechnung der durchschnittlichen Energiedifferenz
    private long _anzahlDerEnergieAenderungen = 0;
    // Güte der global besten gefundenen Lösung
    private long _bisherBesteLoesungGuete = -1;
    // Global beste gefundene Lösung (Position der Automaten)
    private Koordinate[] _bisherBesteLoesung;
    // GUI: Fortschrittsbalken-Anzeige
    private Algorithmus.ProgressCallbackDelegate _progressDelegate = null;
    // GUI: Der wievielte Durchlauf des Algorithmus läuft gerade?
    private float _aktuellerDurchlauf = 0.0f;
    // GUI: Wie oft läuft der Algorithmus durch?
    private float _maxDurchlaeufe = 1.0f;
    //zeigt berechnung live auf der karte
    private boolean _liveView = false;

    /**
     * Konstruktor für die Algorithmen-Klasse.
     * @param weltfunktion Weltfunktion (wird nur für die Welt benötigt)
     * @param zustand Zustand, der von einem Eröffnungsverfahren erzeugt wurde
     */
    public Abkuehlung(Weltfunktion weltfunktion, Zustand zustand) {
        // Werte zuweisen
        _welt = weltfunktion.getWelt();
        _zustand = zustand;
        _anzahlDerAutomaten = _welt.getSollAnzahlAutomaten();
        // Breite und Höhe der Karte berechnen
        _weltBreite = _welt.getPixelkarte().getMaxx() - _welt.getPixelkarte().getMinx();
        _weltHoehe = _welt.getPixelkarte().getMaxy() - _welt.getPixelkarte().getMiny();
    }

    /**
     * Startet den Algorithmus.
     */
    private void Optimiere() {
        // Güte der Initiallösung berechnen
        _guete = BerechneZustandsGuete();

        // Algorithmus vorbereiten
        //DebugPositionDerAutomaten();

        // Optimiere, bis System "gefroren"
        while (_temperatur > 0) {

            // Durchläufe durchführen
            for (int i = 0; i < _anzahlDerDurchlaeufe; i++) {
                // Elementare Operationen ausführen
                ElementareOperationen();

                if (!AkzeptiereAenderung()) {
                    // Mache die Änderungen wieder rückgängig
                    ElementareOperationenRueckgaengig();
                } else {
                    // Güte des Zustandes in die Variable eintragen
                    _guete = BerechneZustandsGuete();
                }
            }

            if (_temperatur % 50 == 0) {
                // Fortschritt berechnen und anzeigen
                int fortschritt = (int) ((1000 * _aktuellerDurchlauf / _maxDurchlaeufe) + (float) (_initialTemperatur - _temperatur) * (1000 / _maxDurchlaeufe) / ((float) _initialTemperatur));
                this._progressDelegate.fortschritt(fortschritt, 0, (int) (1000.0f));
                if (_liveView) {
                    this._progressDelegate.formRepaint();
                }
            }

            // Temperatur verkleinern
            TemperaturVerkleinern();
        }

        // Lade global beste gefundene Verteilung
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            _zustand.getAutomat(i).setPosition(new Koordinate(_bisherBesteLoesung[i].getX(), _bisherBesteLoesung[i].getY()));
        }

        // DEBUG: Güte der gefundenen Lösung ausgeben und Position der Automaten ausgeben
        // System.out.println("Abkühlung: Gefundene Lösung hat die Güte " + _bisherBesteLoesungGuete);
        // DebugPositionDerAutomaten();
        // System.out.println("Durchschnittliche Energiedifferenz: " + _durchschnittlicheEnergiedifferenz / _anzahlDerEnergieAenderungen);

        // GUI: Fortschrittsbalken setzen
        this._progressDelegate.fortschritt(this._initialTemperatur, 0, this._initialTemperatur);
    }

    /**
     * DEBUG: Positionen der Automaten auf der Konsole ausgeben.
     */
    private void DebugPositionDerAutomaten() {
        System.out.println("Positionen der " + _zustand.getAutomaten().length + " Automaten:");

        // Alle Automaten durchgehen
        for (int i = 0; i < _zustand.getAutomaten().length; i++) {
            System.out.println(i + ": " + _zustand.getAutomat(i).toString());
        }
    }

    /**
     * Temperatur setzen.
     * @param temperatur Temperatur T, T_{initial}
     */
    public void setInitTemperatur(int temperatur) {
        this._temperatur = temperatur;
        this._initialTemperatur = temperatur;
    }

    /**
     * Initialtemperatur setzen.
     * @param temperatur Temperatur T, T_{initial}
     */
    public void setTemperatur(int temperatur) {
        this._temperatur = temperatur;
        this._initialTemperatur = temperatur;
    }

    /**
     * Verkleinert die Temperatur.
     */
    private void TemperaturVerkleinern() {
        // T um 1 verkleinern
        _temperatur--;
    }

    /**
     * Führt eine Reihe an elementaren Operationen auf der Menge der Automaten aus.
     */
    private void ElementareOperationen() {
        // Speichere den aktuellen Zustand, um die Änderungen rückgängig machen zu können
        _letzterZustand = new Koordinate[_anzahlDerAutomaten];
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            _letzterZustand[i] = new Koordinate(_zustand.getAutomat(i).getPosition().getX(), _zustand.getAutomat(i).getPosition().getY());
        }

        // Führe mehrere elementare Operationen durch
        for (int i = 0; i < _anzahlDerVeraenderungen; i++) {
            // Wähle einen zufälligen Automaten aus
            int automat = (int) ((Math.random() * _anzahlDerAutomaten));

            // Gesperrte Automaten nicht verschieben
            if (!_zustand.getAutomat(automat).getGesperrt()) {
                // Variablen für die maximale Verschiebung in x- und y-Richtung
                double maxVerschiebungX;
                double maxVerschiebungY;

                if (!_aenderungenInAbhaengigkeitVonTemperatur) {
                    // Maximale Verschiebung eines Automaten in x- und y-Richtung in Abhängigkeit der Größe der Karte berechnen
                    maxVerschiebungX = Math.max(2.0, ((double) _weltBreite / 100.0)) * 6.0;
                    maxVerschiebungY = Math.max(2.0, ((double) _weltHoehe / 100.0)) * 6.0;
                } else {
                    // Maximale Verschiebung eines Automaten in x- und y-Richtung in Abhängigkeit der Größe der Karte berechnen
                    maxVerschiebungX = Math.max(2.0, ((double) _weltBreite / 100.0)) * 10.0;
                    maxVerschiebungY = Math.max(2.0, ((double) _weltHoehe / 100.0)) * 10.0;

                    // Temperatur soll maximale Verschiebung beeinflussen
                    maxVerschiebungX *= (((double) _temperatur / ((double) _initialTemperatur)));
                    maxVerschiebungY *= (((double) _temperatur / ((double) _initialTemperatur)));

                    // Automaten mindestens um 1 verschieben lassen
                    maxVerschiebungX++;
                    maxVerschiebungY++;

                    // DEBUG Dominik
                    //System.out.println("VerschX: " + maxVerschiebungX + " - VerschY: " + maxVerschiebungY);
                }

                // DEBUG: maximale Verschiebung ausgeben
                //System.out.println("Max Verschiebung: " + maxVerschiebungX);

                // Generiere zufällige Verschiebung in x- und y-Richtung
                int verschiebungX = Zufallszahl(-(int) maxVerschiebungX, (int) maxVerschiebungX);
                int verschiebungY = Zufallszahl(-(int) maxVerschiebungY, (int) maxVerschiebungY);

                // Verschiebe den Automaten
                _zustand.getAutomat(automat).getPosition().translate(verschiebungX, verschiebungY);
            }
        }

        // Gültigkeit prüfen (z.B. Überschneidung mit anderen Automaten)
        if (!ZustandGueltig()) {
            // Mache den aktuellen Durchlauf rückgängig
            ElementareOperationenRueckgaengig();
        } else {
            // DEBUG: Erfolgsnachricht ausgeben
            //System.out.println("Gültige elementare Operation ausgeführt!");
        }
    }

    /**
     * Berechnet die Güte des Zustandes. Je höher dieser Wert ist, desto besser ist die Lösung.
     * @return Güte
     */
    private long BerechneZustandsGuete() {
        // Die Punktezahlen aller Automaten aufaddieren, die Automaten dürfen sich NICHT überschneiden!
        long rueckgabe = 0;

        // Alle Automaten durchgehen
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            rueckgabe += _zustand.getGewichtskarte().getWert(_zustand.getAutomat(i).getPosition());
        }

        // Güte mit dem global besten Wert vergleichen
        if (rueckgabe > _bisherBesteLoesungGuete) {
            // Global bessere Lösung gefunden
            _bisherBesteLoesung = new Koordinate[_anzahlDerAutomaten];
            for (int i = 0; i < _anzahlDerAutomaten; i++) {
                _bisherBesteLoesung[i] = new Koordinate(_zustand.getAutomat(i).getPosition().getX(), _zustand.getAutomat(i).getPosition().getY());
            }

            _bisherBesteLoesungGuete = rueckgabe;
        }

        return rueckgabe;
    }

    /**
     * Überprüft, ob ein Zustand gültig ist.
     * @return ja/nein
     */
    private boolean ZustandGueltig() {
        // Kein Automat darf außerhalb der Karte sein
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            if (_zustand.getAutomat(i).getPosition().getX() < _welt.getPixelkarte().getMinx() || _zustand.getAutomat(i).getPosition().getX() > _welt.getPixelkarte().getMaxx() || _zustand.getAutomat(i).getPosition().getY() < _welt.getPixelkarte().getMiny() || _zustand.getAutomat(i).getPosition().getY() > _welt.getPixelkarte().getMaxy()) {
                return false;
            }
        }

        // Automaten dürfen sich nicht überschneiden
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            for (int j = i + 1; j < _anzahlDerAutomaten; j++) {
                int distX = _zustand.getAutomat(i).getPosition().getX() - _zustand.getAutomat(j).getPosition().getX();
                int distY = _zustand.getAutomat(i).getPosition().getY() - _zustand.getAutomat(j).getPosition().getY();
                int abstand = distX * distX + distY * distY;

                // Satz des Pythagoras
                if (abstand < 4 * _welt.getRadiusAutomaten() * _welt.getRadiusAutomaten()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Macht die letzten elementaren Operationen rückgängig.
     */
    private void ElementareOperationenRueckgaengig() {
        // Alle Automaten durchgehen
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            _zustand.getAutomat(i).setPosition(new Koordinate(_letzterZustand[i].getX(), _letzterZustand[i].getY()));
        }
    }

    /**
     * Bestimmt, ob eine Lösung akzeptiert werden soll.
     * @param DeltaE Änderung des Energiezustandes.
     * @return Akzeptanz ja/nein
     */
    private boolean AkzeptiereAenderung() {
        // Die Energiedifferenz ergibt sich aus der invertierten Differenz der Güte: -(neueGüte - alteGüte), weil die Energie umso niedriger ist, je höher die Güte ist
        long energiedifferenz = _guete - BerechneZustandsGuete();
        return AkzeptiereAenderungMetropolis(energiedifferenz);
    }

    /**
     * Akzeptanz der Lösung gemäß der Metropolis-Regel.
     * @param DeltaE Änderung des Energiezustandes.
     * @return Akzeptanz ja/nein
     */
    private boolean AkzeptiereAenderungMetropolis(float DeltaE) {
        // DEBUG: Durchschnittliche Energiedifferenz berechnen
        _durchschnittlicheEnergiedifferenz += (int) DeltaE;
        _anzahlDerEnergieAenderungen++;

        if (DeltaE < 0.0) {
            // Änderungen, die zu einem niedrigen Energiezustand führen, immer akzeptieren
            return true;
        } else {
            // Änderungen, die zu einem höheren Energiezustand führen, nur mit einer bestimmten Wahrscheinlich in Abhängigkeit von der aktuellen Temperatur akzeptieren
            double zufall = Math.random();
            return zufall < Math.exp(-DeltaE / _temperatur);
        }
    }

    /**
     * Erzeugt eine Zufallszahl im Intervall [min, max].
     * @param min Untere Schranke
     * @param max Obere Schranke
     * @return Zufallszahl
     */
    private int Zufallszahl(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * Startet den Algorithmus. Simulierte Abkühlung wird mehrfach hintereinander ausgeführt.
     * @param progressDelegate GUI: Fortschritt anzeigen
     */
    public void Berechne(Algorithmus.ProgressCallbackDelegate progressDelegate) {
        int startTemperatur = _initialTemperatur;
        _maxDurchlaeufe = 11.0f;

        // GUI: Fortschritt anzeigen
        this._progressDelegate = progressDelegate;
        this._progressDelegate.fortschritt(0, 0, this._initialTemperatur);

        _liveView = true;

        System.out.println("ohne Temperaturabhängigkeit");
        _aenderungenInAbhaengigkeitVonTemperatur = false;

        // Parameter für Algorithmus setzen und Algorithmus starten
        _anzahlDerDurchlaeufe = 300;
        _anzahlDerVeraenderungen = 5;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        // Algorithmus mehrmals nacheinander mit verschiedenen Parametern ausführen (-> Doku: Verbesserungen und Erweiterungen)
        _anzahlDerDurchlaeufe = 300;
        _anzahlDerVeraenderungen = 2;
        setTemperatur(startTemperatur / 2);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        _anzahlDerDurchlaeufe = 300;
        _anzahlDerVeraenderungen = 1;
        setTemperatur(startTemperatur);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());


        System.out.println("mit Temperaturabhängigkeit");
        _aenderungenInAbhaengigkeitVonTemperatur = true;

        _anzahlDerDurchlaeufe = 500;
        _anzahlDerVeraenderungen = 10;
        setTemperatur(startTemperatur);
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        _anzahlDerDurchlaeufe = 300;
        _anzahlDerVeraenderungen = 2;
        setTemperatur(startTemperatur);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        _anzahlDerDurchlaeufe = 300;
        _anzahlDerVeraenderungen = 2;
        setTemperatur(startTemperatur / 2);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        System.out.println("ohne Temperaturabhängigkeit");
        _aenderungenInAbhaengigkeitVonTemperatur = false;

        _anzahlDerDurchlaeufe = 150;
        _anzahlDerVeraenderungen = 2;
        setTemperatur(startTemperatur / 2);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        _anzahlDerDurchlaeufe = 75;
        _anzahlDerVeraenderungen = 1;
        setTemperatur(startTemperatur / 4);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());


        System.out.println("mit Temperaturabhängigkeit");
        _aenderungenInAbhaengigkeitVonTemperatur = true;

        _anzahlDerDurchlaeufe = 150;
        _anzahlDerVeraenderungen = 2;
        setTemperatur(startTemperatur / 2);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());

        _anzahlDerDurchlaeufe = 75;
        _anzahlDerVeraenderungen = 2;
        setTemperatur(startTemperatur / 4);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());


        System.out.println("ohne Temperaturabhängigkeit");
        _aenderungenInAbhaengigkeitVonTemperatur = false;

        //noch einmal ohne veränderung
        _anzahlDerDurchlaeufe = 75;
        _anzahlDerVeraenderungen = 1;
        setTemperatur(startTemperatur / 8);
        _aktuellerDurchlauf++;
        Optimiere();
        System.out.println("Bewertung: " + _zustand.getBewertung());
    }

    /**
     * Gibt das Ergebnis (Zustand) zurück.
     * @return Zustand mit Automaten
     */
    public Zustand getErgebnis() {
        return _zustand;
    }

    public String getStatusText() {
        return "Instanz wird abgekühlt";
    }
}
