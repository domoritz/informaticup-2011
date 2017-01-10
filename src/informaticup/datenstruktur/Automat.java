package informaticup.datenstruktur;

/**
 * Eine Klasse die alle wichtigen Informationen für Automaten speichert.
 */
public class Automat {

    // Position des Automaten
    private Koordinate _position;
    // Radius des Automaten
    private int _radius;
    // Wenn ein Automat gesperrt ist, darf seine Position nicht verändert werden
    private boolean _automatGesperrt = false;

    /**
     * Konstruktor der Klasse.
     * @param position Position des Automaten
     * @param radius Radius des Automaten
     */
    public Automat(Koordinate position, int radius) {
        this._position = position;
        this._radius = radius;
    }

    /**
     * Gibt die Position zurück.
     */
    public Koordinate getPosition() {
        return _position;
    }

    /**
     * Setzt die Position eines Automens.
     * @param Position für den Automaten
     */
    public void setPosition(Koordinate position) {
        this._position = position;
    }

    /**
     * Gibt den Radius der Automaten zurück.
     */
    public int getRadius() {
        return _radius;
    }

    /**
     * Setzt den Radius des Automaten.
     */
    public void setRadius(int radius) {
        this._radius = radius;
    }

    /**
     * Gibt Position und Radius als String zurück.
     * @return Position und Radius als String
     */
    @Override
    public String toString() {
        return _position.toString() + ", Radius " + _radius;
    }

    /**
     * Ist ein Automat gesperrt?
     * @return true/false
     */
    public boolean getGesperrt() {
        return _automatGesperrt;
    }

    /**
     * Sperrt oder entsperrt einen Automat.
     * @param gesperrt true/false
     */
    public void setGesperrt(boolean gesperrt) {
        _automatGesperrt = gesperrt;
    }
}
