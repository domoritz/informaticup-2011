package informaticup;

import informaticup.datenstruktur.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Funktionen, die auf einer Welt angewendet werden können.
 */
public class Weltfunktion {

    // Zeiger auf die Welt
    private Welt _welt;
    // GUI: Fortschrittsbalken
    private Algorithmus.ProgressCallbackDelegate _progressDelegate = null;
    // Die Pixelkarte ist nicht vorhanden, wenn der Benutzer nur "Karte zeichnen" auswählt
    private boolean _pixelUndGewichtskarteVorhanden = false;

    /**
     * Konstruktor für die Weltfunktion: Variablen initialisieren.
     * @param welt Welt
     */
    public Weltfunktion(Welt welt) {
        _welt = welt;
    }

    public boolean getPixelUndGewichtskarteVorhanden() {
        return _pixelUndGewichtskarteVorhanden;
    }

    public void setPixelUndGewichtskarteVorhanden(boolean wert) {
        _pixelUndGewichtskarteVorhanden = wert;
    }

    /**
     * Erstellt eine Pixelrepräsentation der Vektorkarte, in der die Stadtteile und deren Gewichte als Integer Werte gespeichert sind.
     */
    public Karte erstellePixelkarte(Algorithmus.ProgressCallbackDelegate progressDelegate) {
        this._progressDelegate = progressDelegate;

        // Dimensionen der erwünschten Karte herausfinden
        int maxx = 0;
        int maxy = 0;
        int minx = 0;
        int miny = 0;

        // Radius der Automaten in der Welt
        int radius = _welt.getRadiusAutomaten();

        // Suche max und min x und y indem alle Koordinaten der Stadtteile durchsucht werden
        for (Stadtteil stadtteil : _welt.getStadtteile()) {
            for (Koordinate punkt : stadtteil.getPunkte()) {
                if (punkt.getX() > maxx) {
                    maxx = punkt.getX();
                }
                if (punkt.getX() < minx) {
                    minx = punkt.getX();
                }
                if (punkt.getY() > maxy) {
                    maxy = punkt.getY();
                }
                if (punkt.getY() < miny) {
                    miny = punkt.getY();
                }
            }
        }

        // DEBUG: Größe der Karte ausgeben
        // System.out.println("minx=" + minx + ", maxx=" + maxx + ", miny=" + miny + ", maxy=" + maxy + ", radius=" + radius);

        int karteBreite = maxx - minx + 2 * radius;
        int karteHoehe = maxy - miny + 2 * radius;

        // Erstellt Karte mit Überschuss für Radien der Automaten
        Karte karte = new Karte(minx - radius, maxx + radius, miny - radius, maxy + radius);
        Karte teilerKarte = new Karte(minx - radius, maxx + radius, miny - radius, maxy + radius);

        // Java-Funktionen verwenden, um die Polygone zu füllen
        BufferedImage image = new BufferedImage(karteBreite, karteHoehe, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        BufferedImage imageTeiler = new BufferedImage(karteBreite, karteHoehe, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphicsTeiler = imageTeiler.createGraphics();

        // Zeichne die Polygone
        Polygon polygon = new Polygon();

        for (Stadtteil teil : _welt.getStadtteile()) {
            teil.berechneNebeneffekte();
            polygon.reset();

            for (int j = 0; j < teil.getPunkte().length; j++) {
                polygon.addPoint(teil.getPunkt(j).getX() - minx, teil.getPunkt(j).getY() - miny);

                // DEBUG: Koordinaten der Polygone ausgeben
                // System.out.println("Koordinate für Polygon: " + teil.getPunkt(j).getX() + ", " + teil.getPunkt(j).getY());
            }

            // Gewicht eines einzelnen Pixels ermitteln
            int gewicht = teil.getGewichtNachFkt();
            // Polygon zeichnen
            graphics.setColor(GewichtZuFarbe((int) gewicht));
            graphics.fillPolygon(polygon);
            // Außenlinien malen (ist das überhaupt noch notwendig?)
            graphics.drawPolygon(polygon);

            // Polygon zeichnen
            System.out.println("Stadtteil hat Fläche:" + teil.getFlaeche());
            graphicsTeiler.setColor(GewichtZuFarbe((int) teil.getFlaeche()));
            graphicsTeiler.fillPolygon(polygon);
            // Außenlinien malen (ist das überhaupt noch notwendig?)
            graphicsTeiler.drawPolygon(polygon);

            // DEBUG: Stadtteile mit Gewicht ausgeben

        }

        //auf festplatte speichern
        /*try {
            File outputfile = new File("karte.png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Fehler" + e);
        }*/

        // Einzelne Pixel auslesen und in die Karte übertragen
        for (int x = 0; x < karteBreite; x++) {
            if (x % 20 == 0) {
                // GUI: Fortschrittsbalken
                _progressDelegate.fortschritt(x, 0, karteBreite);
            }
            for (int y = 0; y < karteHoehe; y++) {
                long gewicht = -image.getRGB(x, y);
                gewicht *= new Long(karte.getBreite() * karte.getHoehe());
                if (-imageTeiler.getRGB(x, y) != 0) {
                    gewicht /= -imageTeiler.getRGB(x, y);
                }

                karte.setWert(minx + x, miny + y, gewicht);
                teilerKarte.setWert(minx + x, miny + y, (long) (-imageTeiler.getRGB(x, y)));
            }
        }

        _welt.setPixelkarte(karte);
        return karte;
    }

    /**
     * Wandelt einen Integer-Wert (Gewicht) in einen Farbwert um.
     * @param gewicht Gewicht
     * @return Farbwert
     */
    private Color GewichtZuFarbe(int gewicht) {
        return new Color(-gewicht, true);
    }

    /**
     * Approximiert (verkleinert) die Koordinaten der Stadtteile und den Radius der Automaten, noch bevor die Pixelkarte erstellt wird.
     * @throws Exception Ausnahme bei ungültigem Approximationsfaktor
     */
    public void Approximation() throws Exception {
        int faktor = _welt.getApproxrate();
        if (faktor <= 0) {
            throw new Exception("Approximationsfaktor darf nicht kleiner 1 sein!");
        }

        // Alle Stadtteile "verkleinern"
        Stadtteil _stadtteile[] = _welt.getStadtteile();
        for (Stadtteil stadtteil : _stadtteile) {
            for (Koordinate punkt : stadtteil.getPunkte()) {
                punkt.set(punkt.getX() / faktor, punkt.getY() / faktor);
            }
            stadtteil.berechneNebeneffekte();
        }

        // Neuen Automatenradius setzen
        _welt.setRadiusAutomaten(_welt.getRadiusAutomaten() / faktor);
    }

    /**
     * Gibt die maximale Höhe/Breite der Karte (nicht approximiert) zurück.
     * @return Breite/Höhe
     */
    public long getMaximaleBreiteHoehe(boolean flaeche) {
        //int minx = 10000, miny = 10000, maxx = -1, maxy = -1;
        Stadtteil _stadtteile[] = _welt.getStadtteile();

        long minx, maxx, miny, maxy;
        minx = _stadtteile[0].getPunkt(0).getX();
        miny = _stadtteile[0].getPunkt(0).getY();
        maxx = minx;
        maxy = miny;

        for (Stadtteil stadtteil : _stadtteile) {
            for (Koordinate punkt : stadtteil.getPunkte()) {
                minx = Math.min(minx, punkt.getX());
                miny = Math.min(miny, punkt.getY());
                maxx = Math.max(maxx, punkt.getX());
                maxy = Math.max(maxy, punkt.getY());
            }
        }

        if (flaeche) {
            return Math.abs((maxx - minx) * (maxy - miny));
        } else {
            return Math.max(maxx - minx, maxy - miny);
        }
    }

    /**
     * @return the _welt
     */
    public Welt getWelt() {
        return _welt;
    }
}
