package informaticup.datenstruktur;

import informaticup.Algorithmus;
import informaticup.Weltfunktion;

/**
 * Beschreibt den Zustand der Welt, also wo welche Automaten stehen
 * Zu jedem Zustant sollte es eine (zumindest leere) Welt geben. 
 */
public class Zustand {

    // Array mit allen Automaten
    private Automat[] _automaten;
    // Welt, die zu dem Zustand gehört
    private Weltfunktion _weltfunktion;
    // Soll der verbesserte (schnellere) Algorithmus verwendet werden?
    private boolean benutzeDeltakreisAlgorithmusModifikation = true;
    // Gewichtskarte: beschreibt das Gewicht eines Automaten an der gegebenen Stelle
    private Karte _gewichtskarte;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _emptyProgressDelegate;
    private Algorithmus.ProgressCallbackDelegate _progressDelegate;
    // Bewertung dieser Lösung
    long _bewertung = -1;
    private String infos = "";

    /**
     * Konstruktor.
     */
    public Zustand(Weltfunktion weltfunktion, int anzahlDerAutomaten) {
        _automaten = new Automat[anzahlDerAutomaten];
        _weltfunktion = weltfunktion;
    }

    /**
     * Setzt den Automaten und ermittelt den Index automatisch.
     * @param koordinate Koordinate
     */
    public void SetzeNaechstenAutomaten(Koordinate koordinate) {
        for (int i = 0; i < _automaten.length; i++) {
            if (_automaten[i] == null) {
                setAutomat(i, new Automat(koordinate, _weltfunktion.getWelt().getRadiusAutomaten()));
                _automaten[i].setGesperrt(true);
                break;
            }
        }
    }

    /**
     * Getterfunktion für die Anzahl der Automaten.
     * @return Anzahl Automaten
     */
    public int getAnzahlAutomaten() {
        return _automaten.length;
    }

    /**
     * Getterfunktion für einzelne Automaten.
     * @param index Index des Automaten
     * @return Automat
     */
    public Automat getAutomat(int index) {
        return _automaten[index];
    }

    /**
     * Settermethode für einzelne Automaten.
     * @param index, automat
     */
    public void setAutomat(int index, Automat automat) {
        _automaten[index] = automat;
    }

    /**
     * Getterfunktion für das Array der Automaten.
     * @return Automaten-Array
     */
    public Automat[] getAutomaten() {
        return _automaten;
    }

    /**
     * Settermethode für das Automaten-Array.
     * @param Automaten-Array
     */
    public void setAutomaten(Automat[] automaten) {
        this._automaten = automaten;
    }

    /**
     * Getterfunktion für die Welt.
     * @return Welt, die zu dem Zustand gehört
     */
    public Welt getWelt() {
        return getWeltfunktion().getWelt();
    }

    /**
     * Getterfunktion für die Weltfunktion.
     * @return Zeiger auf die Weltfunktion
     */
    public Weltfunktion getWeltfunktion() {
        return _weltfunktion;
    }

    /**
     * Getterfunktion für die Gewichtskarte. Wird bei Bedarf erstellt und dann gespeichert (für spätere Verwendung).
     * @return Gewichtskarte
     */
    public Karte getGewichtskarte() {
        if (this._emptyProgressDelegate == null) {
            this._emptyProgressDelegate = new Algorithmus.ProgressCallbackDelegate() {

                public void fortschritt(int pos, int min, int max) {
                }

                public void formRepaint() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }
        if (_gewichtskarte == null) {
            this.erzeugeGewichtskarte(this._emptyProgressDelegate);
        }
        return _gewichtskarte;
    }

    /**
     * Settermethode für die Gewichtskarte.
     * @param Gewichtskarte
     */
    public void setGewichtskarte(Karte gewichtskarte) {
        this._gewichtskarte = gewichtskarte;
    }

    /**
     * Verschiebt den Automat mit dem Index index um eine bestimmte Anzahl an Pixel.
     * @param index, x, y
     */
    public void verschiebeAutomat(int index, int x, int y) {
        this.getAutomat(index).getPosition().translate(x, y);
    }

    /**
     * Erzeugt eine Gewichtskarte.
     */
    public void erzeugeGewichtskarte(Algorithmus.ProgressCallbackDelegate progressDelegate) {
        this._progressDelegate = progressDelegate;
        erzeugeGewichtskarte(this.getWelt().getPixelkarte().getMinx(), this.getWelt().getPixelkarte().getMaxx(), this.getWelt().getPixelkarte().getMiny(), this.getWelt().getPixelkarte().getMaxy());
    }

    /**
     * Erzeugt einmalig einen Referenz-Berechnungskreis im passenden Radius.
     * Die Rückgabe ist eine Multiplikations-Matrix, die auf Berechnungen angewandt wird.
     * @param int radius
     * @return int[][]
     */
    public short[][] erstelleReferenzkreis(int radius) {
        short refKreis[][] = new short[2 * radius][2 * radius];
        int xbias = radius;
        int ybias = radius;

        int x = 0;
        int y = 0;

        for (int i = 0; i < 2 * radius; i++) {
            x = i - xbias;
            for (int j = 0; j < 2 * radius; j++) {
                y = j - ybias;
                if ((radius * radius) >= (x * x) + (y * y)) {
                    refKreis[i][j] = 1;
                } else {
                    refKreis[i][j] = 0;
                }
            }
        }

        return refKreis;
    }

    /**
     * Erzeugt einmalig einen Delta-Referenz-Berechnungskreis im passenden Radius.
     * Die Rückgabe ist eine Multiplikations-Matrix, die auf Berechnungen angewandt wird.
     * @param int radius
     * @return int[][]
     */
    public short[][] erstelleDeltakreisRechts(int radius, short[][] referenzkreis) {
        short dKreis[][] = new short[2 * radius + 1][2 * radius];
        for (int i = 0; i < 2 * radius + 1; i++) {
            for (int j = 0; j < 2 * radius; j++) {
                if (i == 0) {
                    dKreis[i][j] = (short) (0 - referenzkreis[i][j]);
                } else if (i == 2 * radius) {
                    dKreis[i][j] = (short) (referenzkreis[i - 1][j]);
                } else {
                    dKreis[i][j] = (short) (referenzkreis[i - 1][j] - referenzkreis[i][j]);
                }
            }
        }

        return dKreis;
    }

    /**
     * Erzeugt einmalig einen Delta-Referenz-Berechnungskreis im passenden Radius.
     * Die Rückgabe ist eine Multiplikations-Matrix, die auf Berechnungen angewandt wird.
     * @param int radius
     * @return int[][]
     */
    public short[][] erstelleDeltakreisUnten(int radius, short[][] referenzkreis) {
        short dKreis[][] = new short[2 * radius][2 * radius + 1];
        for (int i = 0; i < 2 * radius; i++) {
            for (int j = 0; j < 2 * radius + 1; j++) {
                if (j == 0) {
                    dKreis[i][j] = (short) (0 - referenzkreis[i][j]);
                } else if (j == 2 * radius) {
                    dKreis[i][j] = (short) (referenzkreis[i][j - 1]);
                } else {
                    dKreis[i][j] = (short) (referenzkreis[i][j - 1] - referenzkreis[i][j]);
                }
            }
        }

        return dKreis;
    }

    /**
     * Erstellt eine Karte, die jedem Punkt auf der Karte ein Gewicht eines Automaten zurodnet, der an dieser Stelle stehen würde.
     * @param minx, max, miny, maxy
     */
    private void erzeugeGewichtskarte(int minx, int maxx, int miny, int maxy) {
        Karte gewichtskarte = new Karte(minx, maxx, miny, maxy);

        //Weltfunktion _weltfunktion = this.getWeltfunktion();
        Welt _welt = _weltfunktion.getWelt();
        Karte _pixelkarte = _welt.getPixelkarte();
        int _radius = _welt.getRadiusAutomaten();

        short referenzkreis[][] = this.erstelleReferenzkreis(_radius);
        short deltakreis_rechts[][] = null;
        short deltakreis_unten[][] = null;
        if (this.benutzeDeltakreisAlgorithmusModifikation) {
            deltakreis_rechts = this.erstelleDeltakreisRechts(_radius, referenzkreis);
            deltakreis_unten = this.erstelleDeltakreisUnten(_radius, referenzkreis);
        }

        long summe = 0;
        int n = 0;
        int nmax = (maxx - minx) * (maxy - miny);

        //belegt karte mit werten für das Gewicht
        for (int x = minx; x < maxx; x++) {
            for (int y = miny; y < maxy; y++) {
                n += 1;
                if (n % 1000 == 0) {
                    this._progressDelegate.fortschritt(n, 0, nmax);
                }
                if (!this.benutzeDeltakreisAlgorithmusModifikation || (x == minx && y == miny)) {
                    summe = 0;
                    for (int rx = 0 - _radius; rx < _radius; rx++) {
                        for (int ry = 0 - _radius; ry < _radius; ry++) {
                            if (referenzkreis[rx + _radius][ry + _radius] != 0) {
                                summe += _pixelkarte.getWert(x + rx, y + ry) * referenzkreis[rx + _radius][ry + _radius];
                            }
                        }
                    }
                    // DELTAKREISE NOCH NICHT IMPLEMENTIERT
                } else if (x == minx) {
                    summe = gewichtskarte.getWert(x, y - 1);
                    for (int rx = 0 - _radius; rx < _radius; rx++) {
                        for (int ry = 0 - _radius; ry < _radius + 1; ry++) {
                            if (deltakreis_unten[rx + _radius][ry + _radius] != 0) {
                                summe += _pixelkarte.getWert(x + rx, y + ry - 1) * deltakreis_unten[rx + _radius][ry + _radius];
                            }
                        }
                    }
                } else {
                    summe = gewichtskarte.getWert(x - 1, y);
                    for (int rx = 0 - _radius; rx < _radius + 1; rx++) {
                        for (int ry = 0 - _radius; ry < _radius; ry++) {
                            if (deltakreis_rechts[rx + _radius][ry + _radius] != 0) {
                                summe += _pixelkarte.getWert(x + rx - 1, y + ry) * deltakreis_rechts[rx + _radius][ry + _radius];
                            }
                        }
                    }
                }
                gewichtskarte.setWert(x, y, summe);
            }
        }

        // Alle Werte herunterrechnen
        for (int x = minx; x < maxx; x++) {
            for (int y = miny; y < maxy; y++) {
                gewichtskarte.setWert(x, y, gewichtskarte.getWert(x, y) / (_welt.getPixelkarte().getBreite() * _welt.getPixelkarte().getHoehe()));
            }
        }

        this.setGewichtskarte(gewichtskarte);
    }

    /**
     * Berechnet das Gewicht eines Automaten, der an dieser Stelle stehen würde.
     * Das Gewicht ist die Fläche des jeweils abgedeckten Stadtteils mal der Relevanz als Faktor.
     * @param x, y
     * @return gewicht
     */
    /* FEHLER UND UNnötig
    public int getGewicht(Welt _welt, int x, int y) {
    //_Pixelkarte.getWert(x - xlokal, y);
    int gewicht = 0;
    int radiusAutomat = getWelt().getRadiusAutomaten();
    Stadtteil[] stadtteile = getWelt().getStadtteile();

    Karte _Pixelkarte = _welt.getPixelkarte();

    // addiert alle Pixel entlang der X/Y-Achse zusammen

    for (int xlokal = 0; xlokal <= radiusAutomat; xlokal++) {
    gewicht += _Pixelkarte.getWert(x - xlokal, y);
    gewicht += _Pixelkarte.getWert(x + xlokal, y);
    gewicht += _Pixelkarte.getWert(x, y - xlokal);
    gewicht += _Pixelkarte.getWert(x, y + xlokal);

    for (int ylokal = 0; ylokal <= radiusAutomat; ylokal++) {
    if ((radiusAutomat * radiusAutomat) >= ((xlokal * xlokal) + (ylokal * ylokal))) {
    //Addiert alle anderen im Kreis liegenden punkte auf.
    gewicht += _Pixelkarte.getWert(x - xlokal, y - ylokal);
    gewicht += _Pixelkarte.getWert(x + xlokal, y + ylokal);
    gewicht += _Pixelkarte.getWert(x + xlokal, y - ylokal);
    gewicht += _Pixelkarte.getWert(x - xlokal, y + ylokal);
    }
    }
    }
    //sollte keinen Fehler geben, aber sicher ist nunmal sicher
    if (gewicht >= 0) {
    return gewicht;
    } else {
    return 0;
    }
    }*/
    /**
     * gibt die Bewertng zurück
     * achtung: aproximationsrate muss nicht mehr gesetzt werden sein
     * @return
     */
    public long getBewertung() {

        long bewertung = 0;
        for (Automat automat : this.getAutomaten()) {
            if (automat != null) {
                bewertung += this.getGewichtskarte().getWert(automat.getPosition());
            }
        }
        _bewertung = bewertung;

        return _bewertung;
    }

    /**
     * @return the infos
     */
    public String getInfos() {
        return infos;
    }

    /**
     * @param infos the infos to set
     */
    public void setInfos(String infos) {
        this.infos = infos;
    }
}
