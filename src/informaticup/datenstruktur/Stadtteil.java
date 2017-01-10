package informaticup.datenstruktur;

import informaticup.mathematik.Schwerpunkt;
import java.util.HashMap;

/**
 * Stadtteil ist ein Polygon mit einer Wichtigkeit/ einem Gewicht/ einer Relevanz.
 */
public class Stadtteil {

    // Polygon (Position des Stadtteils)
    private Koordinate[] _punkte;
    // Gewichte der numerischen Attribute
    private int[] _gewichte;
    // Namen der Attribute
    private String[] _gewichtnamen;
    // Werte der nicht-numerischen Attribute
    private HashMap<String, String> _attribute;
    // ID des Stadtteils
    public int ID;
    // Fläche des Stadtteils
    private int _flaeche = 0;
    private Koordinate _schwerpunkt = null;
    // Gewicht des Stadtteils, wird aus der Gewichtung der Attribute, die der Benutzer zu Beginn vornimmt, errechnet
    private int _gewichtNachFkt = -1;

    /**
     * Konstruktor der Klasse.
     * @param anzahlDerPunkte, anzahlDerNumerischenAttribute und ID
     */
    public Stadtteil(int anzahlDerPunkte, int anzahlDerNumerischenAttribute, int id) {
        _punkte = new Koordinate[anzahlDerPunkte];
        _gewichte = new int[anzahlDerNumerischenAttribute];
        _gewichtnamen = new String[anzahlDerNumerischenAttribute];
        _attribute = new HashMap<String, String>();
        ID = id;

        for (int i = 0; i < anzahlDerPunkte; i++) {
            _punkte[i] = new Koordinate();
        }
    }

    /**
     * Konstructor ohne id, bitte sparsam verwenden.
     * id wird auf -1 gesetzt
     * @param anzahlDerPunkte und anzahlDerNumerischenAttribute
     */
    public Stadtteil(int anzahlDerPunkte, int anzahlDerNumerischenAttribute) {
        _punkte = new Koordinate[anzahlDerPunkte];
        _gewichte = new int[anzahlDerNumerischenAttribute];
        _gewichtnamen = new String[anzahlDerNumerischenAttribute];
        _attribute = new HashMap<String, String>();
        ID = -1;

        for (int i = 0; i < anzahlDerPunkte; i++) {
            _punkte[i] = new Koordinate();
        }
    }

    /**
     * Ermittelt einen Punkt. Jedes Polygon besteht aus Punkten, die einen Index besitzen.
     * @param index Index des Punktes
     * @return Koordinate Koordinate
     */
    public Koordinate getPunkt(int index) {
        return getPunkte()[index];
    }

    /**
     * Setzt einen Punkt. Jedes Polygon besteht aus Punkten, die einen Index besitzen.
     * @param index und Punkt(int)
     */
    public void setPunkt(int index, Koordinate punkt) {
        getPunkte()[index] = punkt;
    }

    /**
     * Setzt das Gewicht des Stadtteiles.
     * @param gewicht Gewicht
     */
    public void setGewichtNachFkt(int gewicht) {
        _gewichtNachFkt = gewicht;
    }

    /**
     * Gibt Gewicht des Stadtteils zurück.
     * @return Gewicht
     */
    public int getGewichtNachFkt() {
        return _gewichtNachFkt;
    }

    /**
     * Gibt das Gewicht des Stadtteils nach genau EINEM Attribut zurück.
     * @param index Welches Attribut?
     * @return Gewicht
     */
    public int getGewicht(int index) {
        return _gewichte[index];
    }

    /**
     * Gibt die Anzahl der Gewichte zurück.
     * @return Anzahl der Gewichte
     */
    public int getGewichtCount() {
        return _gewichte.length;
    }

    /**
     * Setzt Gewicht des Stadtteils eines Attributs.
     * @param index, Gewicht
     */
    public void setGewicht(int index, int gewicht) {
        _gewichte[index] = gewicht;
    }

    /**
     * Gibt den Namen eines Attributs zurück.
     * @param index Index des Attributs
     * @return Name
     */
    public String getGewichtname(int index) {
        return _gewichtnamen[index];
    }

    /**
     * Setzt Gewichtsnamen eines Attributs.
     * @param index, Gewichtsname
     */
    public void setGewichtname(int index, String gewichtname) {
        _gewichtnamen[index] = gewichtname;
    }

    /**
     * Setzt ein nicht-numerisches Attribut des Stadtteils.
     * @param Name, Value
     */
    public void setAttribut(String name, String value) {
        _attribute.put(name, value);
    }

    /**
     * Gibt nicht-numerisches Attribut des Stadtteils zurück.
     * @param Attribut-Name
     * @return Wert
     */
    public String getAttribut(String name) {
        return _attribute.get(name);
    }

    /** Gibt Koordinaten des Polygons zurück.
     * @return Koordinate[] Koordinaten-Array
     */
    public Koordinate[] getPunkte() {
        return _punkte;
    }

    /**
     * Setzt Punkte-Array (Polygon) des Stadtteils.
     * @param punkte Koordinaten-Array
     */
    public void setPunkte(Koordinate[] punkte) {
        this._punkte = punkte;
    }

    /**
     * Berechnet Fläche und Schwerpunkt des Stadtteils.
     */
    public void berechneNebeneffekte() {
        _flaeche = 0;
        _schwerpunkt = new Koordinate(0, 0);

        for (int i = 0; i < _punkte.length; i++) {
            // Gaussche Trapezformel
            _flaeche += (_punkte[i].getX() + _punkte[(i + 1) % _punkte.length].getX()) * (_punkte[(i + 1) % _punkte.length].getY() - _punkte[i].getY());

            // Vereinfacht: einfach Mittelwert bilden
            _schwerpunkt.set(_schwerpunkt.getX() + _punkte[i].getX(), _schwerpunkt.getY() + _punkte[i].getY());
        }

        _flaeche /= 2;
        if (_flaeche < 0) {
            _flaeche *= -1;
        }

        _schwerpunkt.set(_schwerpunkt.getX() / _punkte.length, _schwerpunkt.getY() / _punkte.length);
    }

    /**
     * Gibt den Schwerpunkt des Stadtteils zurück.
     * @return Koordinate
     */
    public Koordinate getSchwerpunkt() {
        return this._schwerpunkt;
    }

    /**
     * Gibt die Fläche des Stadtteils zurück.
     * @return Fläche
     */
    public int getFlaeche() {
        return this._flaeche;
    }
}
