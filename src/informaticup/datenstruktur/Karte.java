package informaticup.datenstruktur;

/**
 * Ein Feld, welches jeder Koordinate einen bestimmen Integerwert zuordnet.
 * Dies wird z.B. gebraucht, wenn man eine Gewichtskarte erzeugt.
 *
 *
 */
public class Karte {

    private long[][] _karte;
    private final int _maxx;
    private final int _minx;
    private final int _maxy;
    private final int _miny;

    /**
     * Konstruktor der Klasse, erzeugt die wichtigen Eckdaten der Karte.
     * @param minx, maxx, miny, maxy
     * @return
     */
    public Karte(int minx, int maxx, int miny, int maxy) {
        System.out.println("Erzeuge Karte mit folgenden Dimensionen: in x-Richtung von " + minx + " bis " + maxx + " und in y-Richtung von " + miny + " bis " + maxy);

        _karte = new long[(maxx - minx)][(maxy - miny)];
        _maxx = maxx;
        _minx = minx;
        _maxy = maxy;
        _miny = miny;
    }

    /**
     * Gibt die Breite der Karte zurück.
     * @return Breite
     */
    public int getBreite() {
        int tmp = _maxx - _minx;
        return (tmp >= 0 ? tmp : -tmp);
    }

    /**
     * Gibt die Höhe der Karte zurück.
     * @return Höhe
     */
    public int getHoehe() {
        int tmp = _maxy - _miny;
        return (tmp >= 0 ? tmp : -tmp);
    }

    /**
     * Gibt den Wert der Karte an der Stelle zurück.
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @return Wert
     */
    public long getWert(int x, int y) {
        long wert = 0;
        try {
            wert = _karte[(x - _minx)][(y - _miny)];
        } catch (Exception e) {
        }
        return wert;
    }

    /**
     * Gibt den Wert der Karte an der Stelle zurück.
     * @param koord Koordinate
     * @return Wert
     */
    public long getWert(Koordinate koord) {
        return this.getWert(koord.getX(), koord.getY());
    }

    /**
     * Setzt Wert der Karte an einer Stelle.
     * @param x x-Koordinate
     * @param y y-Koorindate
     * @param wert Wert
     */
    public void setWert(int x, int y, long wert) {
        try {
            _karte[(x - _minx)][(y - _miny)] = wert;
        } catch (Exception e) {
        }
    }

    /**
     * Gibt den Maximalen x-Wert der Karte zurück.
     * @return Maximales x
     */
    public int getMaxx() {
        return _maxx;
    }

    /**
     * Gibt den Maximalen y-Wert der Karte zurück.
     * @return Maximales y
     */
    public int getMaxy() {
        return _maxy;
    }

    /**
     * Gibt den Minimalen x-Wert der Karte zurück.
     * @return Minimales x
     */
    public int getMinx() {
        return _minx;
    }

    /**
     * Gibt den Minimalen y-Wert der Karte zurück.
     * @return Minimales y
     */
    public int getMiny() {
        return _miny;
    }

    /**
     * Gibt alle Werte der Karte auf der Konsole aus.
     */
    public void schreibe() {
        for (int y = 0; y < _maxy - _miny; y++) {
            for (int x = 0; x < _maxx - _minx; x++) {
                System.out.print("[" + _karte[x][y] + "]");
            }
            System.out.println("");
        }
    }

    /**
     * Gibt die Werte eines Vektors weiter an die entsprechenden Koordinaten.
     * @param von Startpunkt
     * @param nach Endpunkt
     * @param wert Wert (Gewicht)
     */
    public void maleLinie(Koordinate von, Koordinate nach, int wert) {
        // DEBUG: Parameter ausgeben
        // System.out.println("======\nMale Linie von " + von.getX() + " " + von.getY() + " nach " + nach.getX() + " " + nach.getY());
        // System.out.println("Male echte Linie von " + von.getX() + " " + von.getY() + " nach " + nach.getX() + " " + nach.getY());

        setWert(von.getX(), von.getY(), wert);   // Anfangs- und Endwerte
        setWert(nach.getX(), nach.getY(), wert);

        Koordinate vektor = new Koordinate(nach.getX() - von.getX(), nach.getY() - von.getY());
        double vektorlaenge = Math.sqrt(vektor.getX() * vektor.getX() + vektor.getY() * vektor.getY());

        // Gerade: x = von + \lambda * vektor

        for (double f = 0.0; f <= 1.0; f += 0.5 / vektorlaenge) {
            long aktuellerWert = getWert((int) (von.getX() + ((double) vektor.getX()) * f), (int) (von.getY() + ((double) vektor.getY()) * f));
            if (aktuellerWert == 0) {
                aktuellerWert = wert;
            } else {
                aktuellerWert = (aktuellerWert + wert) / 2;
            }
            setWert((int) (von.getX() + ((double) vektor.getX()) * f), (int) (von.getY() + ((double) vektor.getY()) * f), aktuellerWert);
        }
    }
}
