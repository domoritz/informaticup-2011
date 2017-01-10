package informaticup.datenstruktur;

/**
 * Klasse für Koordinaten.
 */
public class Koordinate {

    // y-Koordinate
    private int _y;
    // x-Koordinate
    private int _x;

    /**
     * Erstellt eine neue Koordinate mit den Parametern X und Y.
     * @param initY die Y-Koordinate
     * @param initX die X-Koordinate
     */
    public Koordinate(int initX, int initY) {
        _x = initX;
        _y = initY;
    }

    /**
     * Erstellt Koordinate ohne wirkliche Position.
     */
    public Koordinate() {
    }

    /**
     * Gibt die X-Koordinate der Koordinate zurück.
     * @return Die X-Koordinate
     */
    public int getX() {
        return _x;
    }

    /**
     * Gibt die Y-Koordinate der Koordinate zurück.
     * @return Die Y-Koordinate
     */
    public int getY() {
        return _y;
    }

    /**
     * Setzt die Koordinate.
     * @param x x-Koorindate
     * @param y y-Koordinate
     */
    public void set(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * Verschiebt die x und y Koordinate um einen bestimmten Wert.
     * @param dx, dy das Offset in Richtung der X-/Y-Koordinate
     */
    public void translate(int dx, int dy) {
        _x += dx;
        _y += dy;
    }

    /**
     * Wandelt die Koordinate in einen String um.
     * @return Koordinate in Stringform.
     */
    @Override
    public String toString() {
        return "(" + _x + "," + _y + ")";
    }
}
