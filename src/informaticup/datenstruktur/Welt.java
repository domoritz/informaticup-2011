package informaticup.datenstruktur;

/**
 * Beschreibt die Welt ohne deren Zustand, also ohne die Automaten. Die Welt besteht aus Stadtteilen mit einem bestimmten Gewicht.
 */
public class Welt {

    // Alle Stadtteile
    private Stadtteil[] _stadtteile;
    // Radius der Automaten
    private int _radiusAutomaten;
    // Anzahl der zu platzierenden Automaten
    private int _sollAnzahlAutomaten;
    // Zeiger auf die Pixelkarte
    private Karte _pixelkarte;
    // Approximationsfaktor
    private int _approxrate = 1;

    /**
     * Konstruktor.
     * @param anzahlStadtteile, anzahlAutomaten, radiusAutomaten
     */
    public Welt(int anzahlStadtteile, int anzahlAutomaten, int radiusAutomaten) {
        _stadtteile = new Stadtteil[anzahlStadtteile];
        _sollAnzahlAutomaten = anzahlAutomaten;
        _radiusAutomaten = radiusAutomaten;
    }

    /**
     * Konstruktor.
     * @param anzahlStadtteile
     */
    public Welt(int anzahlStadtteile) {
        _stadtteile = new Stadtteil[anzahlStadtteile];
    }

    /**
     * Leerer Konstruktor, Welt ohne Automaten und Stadtteile.
     */
    public Welt() {
    }

    /**
     * Gibt Stadteil mit einem bestimmten Index zurück.
     * @param index
     * @return entsprechender Stadtteil
     */
    public Stadtteil getStadtteil(int index) {
        return getStadtteile()[index];
    }

    /**
     * Setzt Stadteil.
     * @param index, Stadtteil
     */
    public void setStadtteil(int index, Stadtteil stadtteil) {
        getStadtteile()[index] = stadtteil;
        getStadtteile()[index].ID = index;
    }

    /**
     * Gibt alle Stadteile zurück.
     * @return Stadtteile
     */
    public Stadtteil[] getStadtteile() {
        return _stadtteile;
    }

    /** Setzt ein Stadtteil-Array und die IDs automatisch.
     *@param Stadtteile Stadtteil-Array
     */
    public void setStadtteile(Stadtteil[] stadtteile) {
        this._stadtteile = stadtteile;

        // Setze IDs
        for (int i = 0; i < stadtteile.length; i++) {
            _stadtteile[i].ID = i;
        }
    }

    /**
     * Ermittelt den Radius der Automaten.
     * @return Radius der Automaten
     */
    public int getRadiusAutomaten() {
        return _radiusAutomaten;
    }

    /**
     * Setzt den Radius der Automaten. Alle Automaten haben den gleichen Radius.
     * @param Radius
     */
    public void setRadiusAutomaten(int radiusAutomaten) {
        this._radiusAutomaten = radiusAutomaten;
    }

    /**
     * Anzahl der Atomaten, die später in der Welt stehen sollen.
     * Die tatsächliche Anzahl steht im Zustand und nicht der Welt!
     * @return Anazhl der Automaten
     */
    public int getSollAnzahlAutomaten() {
        return _sollAnzahlAutomaten;
    }

    /** Übergibt die Anzahl der Automaten.
     * @param Anzahl der Automaten
     */
    public void setSollAnzahlAutomaten(int anzahlAutomaten) {
        this._sollAnzahlAutomaten = anzahlAutomaten;
    }

    /**
     * Gibt Pixelkarte zurück.
     * @return Pixelkarte
     */
    public Karte getPixelkarte() {
        return _pixelkarte;
    }

    /**
     * Setzt eine Pixelkarte.
     * @param pixelkarte the _pixelkarte to set
     * @return 
     */
    public void setPixelkarte(Karte pixelkarte) {
        this._pixelkarte = pixelkarte;
    }

    /**
     * Gibt die Approximationsrate zurück.
     * @return Approximationsrate
     */
    public int getApproxrate() {
        return _approxrate;
    }

    /**
     * Setzt die Approximationsrate.
     * @param approxrate Approximationsrate
     */
    public void setApproxrate(int approxrate) {
        this._approxrate = approxrate;
    }
}
