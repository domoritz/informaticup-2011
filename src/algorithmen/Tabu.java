package algorithmen;

import informaticup.Algorithmus;
import informaticup.Weltfunktion;
import informaticup.datenstruktur.*;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Algorithmus: Metaheuristik/Optimierungsverfahren: Tabu-Suche
 *
 */
public class Tabu implements informaticup.Algorithmus {

    // Zeiger auf die Welt
    private Welt _welt;
    // Anzahl der platzierten Automaten
    private int _anzahlDerAutomaten;
    // Anzahl der Iterationen
    private int _anzahlDerIterationen = 2500;
    // Aktueller Zustand (Automaten platziert)
    private Zustand _zustand;
    // Güte des aktuellen Zustandes
    private long _guete;
    // Tabu-Liste: Verkettete Liste, die mit einem Iterator in linearer Zeit durchsucht werden kann
    private LinkedList<Aenderungsvorschlag> _tabuListe;
    // Bisher beste gefundene Lösung
    Koordinate[] _bisherBesteLoesung;
    // Güte der bisher besten gefundenen Lösung
    long _bisherBesteLoesungGuete = -1;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _progressDelegate;

    /**
     * Konstruktur: Variablen initialisieren.
     * @param weltfunktion Weltfunktion
     * @param zustand Zustand mit bereits gesetzten Automaten (Initiallösung)
     */
    public Tabu(Weltfunktion weltfunktion, Zustand zustand) {
        _welt = weltfunktion.getWelt();
        _anzahlDerAutomaten = _welt.getSollAnzahlAutomaten();
        _zustand = zustand;

        // Tabu-Liste erzeugen
        _tabuListe = new LinkedList<Aenderungsvorschlag>();
    }

    /**
     * Setzt die Anazhl der Iterationen.
     * @param anzahlDerInterationen Anzahl der Iterationen
     */
    public void setAnzahlDerIterationen(int anzahlDerInterationen) {
        _anzahlDerIterationen = anzahlDerInterationen;
    }

    /**
     * Startet den eigentlichen Algorithmus (Tabu-Suche).
     */
    private void Optimiere() {
        // Berechne Güte der Initiallösung
        _guete = 0;
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            _guete += _zustand.getGewichtskarte().getWert(_zustand.getAutomat(i).getPosition());
        }

        // Ausgangssituation speichern (beste bisher gefundene Lösung)
        _bisherBesteLoesung = new Koordinate[_anzahlDerAutomaten];
        for (int j = 0; j < _anzahlDerAutomaten; j++) {
            _bisherBesteLoesung[j] = new Koordinate(_zustand.getAutomat(j).getPosition().getX(), _zustand.getAutomat(j).getPosition().getY());
        }
        _bisherBesteLoesungGuete = _guete;

        // Iterationen durchführen
        for (int i = 0; i < _anzahlDerIterationen; i++) {
            if (i % 50 == 0) {
                // GUI: Fortschrittsbalken aktualisieren
                _progressDelegate.fortschritt(i, 0, _anzahlDerIterationen);
                _progressDelegate.formRepaint();
            }

            // DEBUG: Statusnachricht
            // System.out.println("Tabu: Iteration " + i);

            // Suche die beste Lösung in der Nachbarschaft heraus. Diese Lösung ist bereits zulässig, nicht in der Tabu-Liste und die beste Lösung in der Nachbarschaft.
            Aenderungsvorschlag besteAenderung = GeneriereNachbarschaftsLoesung();

            // Prüfe, ob ein Änderungsvorschlag gefunden wurde
            if (besteAenderung.Guete > -1) {
                // Übernehme diese Lösung
                Koordinate aktuellePosition = _zustand.getAutomat(besteAenderung.AutomatenIndex).getPosition();
                // Automatenposition setzen
                _zustand.getAutomat(besteAenderung.AutomatenIndex).setPosition(new Koordinate(aktuellePosition.getX() + besteAenderung.AutomatenVerschiebung.getX(), aktuellePosition.getY() + besteAenderung.AutomatenVerschiebung.getY()));
                _guete = besteAenderung.Guete;

                // Tabu-Liste aktualisieren
                Aenderungsvorschlag verbotenerZug = new Aenderungsvorschlag();
                verbotenerZug.AutomatenIndex = besteAenderung.AutomatenIndex;
                verbotenerZug.AutomatenVerschiebung = new Koordinate(-besteAenderung.AutomatenVerschiebung.getX(), -besteAenderung.AutomatenVerschiebung.getY());
                _tabuListe.addFirst(verbotenerZug);

                // Tabu-Liste verkleinern, wenn nötig
                if (_tabuListe.size() > _anzahlDerIterationen / 100) {
                    _tabuListe.removeLast();
                }

                // Prüfe, ob diese Lösung besser ist als alle bisherigen
                if (_guete > _bisherBesteLoesungGuete) {
                    _bisherBesteLoesung = new Koordinate[_anzahlDerAutomaten];

                    for (int j = 0; j < _anzahlDerAutomaten; j++) {
                        _bisherBesteLoesung[j] = new Koordinate(_zustand.getAutomat(j).getPosition().getX(), _zustand.getAutomat(j).getPosition().getY());
                    }
                    _bisherBesteLoesungGuete = _guete;
                }
            }
        }

        // Beste Lösung in den Zustand schreiben
        for (int i = 0; i < _anzahlDerAutomaten; i++) {
            _zustand.getAutomat(i).setPosition(_bisherBesteLoesung[i]);
        }
    }

    /**
     * Durchsucht die Nachbarschaft nach der besten, zulässigen Lösung, die sich nicht in der Tabu-Liste befindet.
     * @return Lösung (Automaten)
     */
    private Aenderungsvorschlag GeneriereNachbarschaftsLoesung() {
        Aenderungsvorschlag besterVorschlag = new Aenderungsvorschlag();
        besterVorschlag.Guete = -1;

        // Zufälligen Automaten auswählen
        int automat = (int) ((Math.random() * _anzahlDerAutomaten));
        // DEBUG: Ausgewählten Automaten ausgeben
        // System.out.println("Tabu: Automat " + automat + " ausgewählt");

        boolean gleicheUebernehmen = false;
        double zufall = Math.random();
        gleicheUebernehmen = (zufall < 0.5);

        // Gesperrte Automaten nicht verändern
        if (!_zustand.getAutomat(automat).getGesperrt()) {
            // Generiere komplette Nachbarschaft im Abstand 2r in jede Richtung für einen einzigen Automaten
            for (int deltaX = 0; deltaX < 4 * _welt.getRadiusAutomaten(); deltaX++) {

                for (int deltaY = 0; deltaY < 4 * _welt.getRadiusAutomaten(); deltaY++) {
                    // Instanz der Änderungsvorschlag-Klasse erzeugen
                    Aenderungsvorschlag aktuellerVorschlag = new Aenderungsvorschlag();
                    aktuellerVorschlag.AutomatenIndex = automat;
                    aktuellerVorschlag.AutomatenVerschiebung = new Koordinate(deltaX - 2 * _welt.getRadiusAutomaten(), deltaY - 2 * _welt.getRadiusAutomaten());
                    aktuellerVorschlag.Guete = _guete;

                    // Gültigkeit dieser Änderung überprüfen
                    Koordinate neuePosition = new Koordinate(_zustand.getAutomat(automat).getPosition().getX() + aktuellerVorschlag.AutomatenVerschiebung.getX(), _zustand.getAutomat(automat).getPosition().getY() + aktuellerVorschlag.AutomatenVerschiebung.getY());

                    // Prüfe, ob Automat in der Welt ist
                    if (neuePosition.getX() < _welt.getPixelkarte().getMinx() || neuePosition.getX() > _welt.getPixelkarte().getMaxx() || neuePosition.getY() < _welt.getPixelkarte().getMiny() || neuePosition.getY() > _welt.getPixelkarte().getMaxy()) {
                        // Problem: Automat befindet sich außerhalb der Karte, ungültig!
                        aktuellerVorschlag.Guete = -1;
                    } else {
                        // Prüfe, ob sich Automaten überschneiden
                        for (int i = 0; i < _anzahlDerAutomaten; i++) {
                            // Keine Überschneidung mit dem Automaten selbst prüfen
                            if (i != automat) {
                                int abstandX = neuePosition.getX() - _zustand.getAutomat(i).getPosition().getX();
                                int abstandY = neuePosition.getY() - _zustand.getAutomat(i).getPosition().getY();

                                if (abstandX * abstandX + abstandY * abstandY < 4 * _welt.getRadiusAutomaten() * _welt.getRadiusAutomaten()) {
                                    // Überschneidung vorhanden mit einem anderen Automaten!
                                    aktuellerVorschlag.Guete = -1;
                                    break;
                                }
                            }
                        }

                        if (aktuellerVorschlag.Guete != -1) {
                            // Vorschlag wurde noch nicht als "ungültig" erkannt
                            // Prüfe, ob die Änderung Tabu ist
                            ListIterator<Aenderungsvorschlag> iterator = _tabuListe.listIterator();
                            while (iterator.hasNext()) {
                                // Item überprüfen
                                Aenderungsvorschlag aktuellesElement = iterator.next();
                                if (aktuellesElement.AutomatenIndex == aktuellerVorschlag.AutomatenIndex && aktuellesElement.AutomatenVerschiebung.getX() == aktuellerVorschlag.AutomatenVerschiebung.getX() && aktuellesElement.AutomatenVerschiebung.getY() == aktuellerVorschlag.AutomatenVerschiebung.getY()) {
                                    // Diese Änderung ist tabu!
                                    aktuellerVorschlag.Guete = -1;
                                }
                            }

                            if (aktuellerVorschlag.Guete != -1) {
                                // Keine Probleme gefunden, berechne neue Güte!
                                aktuellerVorschlag.Guete -= _zustand.getGewichtskarte().getWert(_zustand.getAutomat(automat).getPosition());
                                aktuellerVorschlag.Guete += _zustand.getGewichtskarte().getWert(neuePosition);

                                // Prüfe ob der Vorschlag eine global beste Lösung ist
                                if (aktuellerVorschlag.Guete > besterVorschlag.Guete) {
                                    besterVorschlag = aktuellerVorschlag;
                                } else if (aktuellerVorschlag.Guete == besterVorschlag.Guete && gleicheUebernehmen) {
                                    besterVorschlag = aktuellerVorschlag;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (besterVorschlag.Guete == -1) {
            // DEBUG: Erfolgsnachricht
            // System.out.println("Kein gültiger Zug gefunden!");

            // Es wurde kein gültiger Zug gefunden
            return besterVorschlag;
        } else {
            // DEBUG: Erfolgsnachricht
            // System.out.println("Tabu: folgender Vorschlag wird übernommen: Automat " + besterVorschlag.AutomatenIndex + ", VerschiebungX " + besterVorschlag.AutomatenVerschiebung.getX() + ", VerschiebungY: " + besterVorschlag.AutomatenVerschiebung.getY());
            return besterVorschlag;
        }
    }

    /**
     * Definiert einen Änderungsvorschlag, der zusammen mit einem Zustand zu einer neuen Lösung führt.
     */
    private class Aenderungsvorschlag {

        // Welcher Automat wurde verschoben?
        public int AutomatenIndex;
        // Wie stark wurde der Automat verschoben?
        public Koordinate AutomatenVerschiebung;
        // Güte der neuen Lösung, -1 bedeutet ungültig
        public long Guete = 0;
    }

    /**
     * Startet den Algorithmus.
     * @param progressDelegate GUI: Fortschrittsbalken
     * @throws AlgorithmusException Exception, die bei Fehlern geworfen wird
     */
    public void Berechne(Algorithmus.ProgressCallbackDelegate progressDelegate) throws AlgorithmusException {
        this._progressDelegate = progressDelegate;
        Optimiere();
    }

    /**
     * Gibt die beste gefundene Lösung zurück.
     * @return Zustand
     */
    public Zustand getErgebnis() {
        return _zustand;
    }

    public int getFortschritt() {
        return 0;
    }

    public String getStatusText() {
        return "";
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

                if (abstand < 4 * _welt.getRadiusAutomaten() * _welt.getRadiusAutomaten()) {
                    return false;
                }
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
        return min + ((int) ((Math.random() * max) + 1));
    }
}
