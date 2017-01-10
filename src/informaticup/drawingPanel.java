/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informaticup;

import informaticup.datenstruktur.Automat;
import informaticup.datenstruktur.Karte;
import informaticup.datenstruktur.Koordinate;
import informaticup.datenstruktur.Stadtteil;
import informaticup.datenstruktur.Welt;
import informaticup.datenstruktur.Zustand;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Ein JPanel, das Stadtteile und Automaten zeichnet.
 * http://java.sun.com/developer/technicalArticles/GUI/java2d/java2dpart1.html
 */
public class drawingPanel extends JPanel implements MouseListener, MouseMotionListener {

    // Skalierungsfaktor
    private double scale = 1;
    int dy = 0;
    int dx = 0;
    int maxx = 0;
    int maxy = 0;
    int minx = 0;
    int miny = 0;
    // Soll die Gewichtskarte gezeichnet werden?
    private boolean _zeichneGewichtskarte = false;
    // Zeiger auf die Welt
    private Welt _welt = new Welt();
    // Soll die Welt gezeichnet werden?
    private boolean _zeichneWelt = false;
    // Der aktuelle Zustand mit den Positionen der Automaten
    private Zustand _zustand;
    // Die Gewichtskarte
    private Karte _gewichtskarte = null;
    // Sollen Automaten gezeichnet werden?
    private boolean _zeichneAutomat = false;
    BufferedImage _gewichtsbild = null;
    private long vorher;
    private boolean _setzenModus = false;
    private boolean _gesperrtInvertierenModus = false;
    private boolean _loeschenModus = false;
    public boolean _zeichneNamen = true;
    //private Koordinate _gefangenerAutomatVorher;
    //private int _gefangenerAutomatIndex = -1;

    /**
     * Konstruktor der Klasse.
     */
    public drawingPanel() {
        // Register listeners to handle drawing
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void SetSetzenModus(boolean wert) {
        _setzenModus = wert;
        System.out.println(wert);
    }

    public void setLoeschenModus(boolean wert) {
        _loeschenModus = wert;
    }

    public void setGesperrtModus(boolean wert) {
        _gesperrtInvertierenModus = wert;
    }

    public void updateSize() {
        // Get the drawing area
        dy = getSize().height;
        dx = getSize().width;

        // DEBUG: Variablenbelegungen ausgeben
        //System.out.println("dx: " + dx + " dy: " + dy);
    }

    /**
     * Wird am Anfang und nach resize aufgerufen.
     */
    @Override
    public void paintComponent(Graphics g) {
        updateSize();

        // Clear off-screen bitmap
        super.paintComponent(g);
        // Cast Graphics to Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        this.setBackground(Color.WHITE);

        //#####################################
        g2d.scale(1, -1);
        g2d.translate(0, -(maxy - miny) * scale);

        //umgedreht

        if (_zeichneWelt) {
            paintVectorKarte(g, false);
        }

        g2d.scale(1, -1);
        g2d.translate(0, -(maxy - miny) * scale);
        //#####################################

        //nicht umgedreht

        if (_zeichneGewichtskarte) {
            paintGewichtskarte(g);
        }

        //zeichne infos
        if (_zustand != null) {
            long bewertung = _zustand.getBewertung();
            if (verschautomat.gefangen) {
                if (vorher > bewertung) {
                    g.setColor(Color.red);
                } else if (vorher == bewertung) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.green);
                }
            } else {
                g.setColor(new Color(0, 0, 0, 0.5f));
            }

            g.drawString(String.valueOf(bewertung), 50, 15);

            g.setColor(new Color(0, 0, 0, 0.5f));
            g.drawString(String.valueOf(_zustand.getInfos()), 50, 25);
        }

        //#####################################
        g2d.scale(1, -1);
        g2d.translate(0, -(maxy - miny) * scale);

        //umgedreht
        if (_zeichneAutomat) {
            paintAutomat(g);
        }

        if (_zeichneWelt) {
            paintVectorKarte(g, true);
        }

    }

    public void paintGewichtskarte() {
        _zeichneGewichtskarte = true;
    }

    /**
     * Zeichnet die Pixelkarte.
     * @param karte
     */
    public void paintGewichtskarte(Zustand zustand) {
        _gewichtsbild = null;
        System.out.println("Pixelkarte wird gezeichnet");
        _zustand = zustand;
        _zeichneGewichtskarte = true;
    }

    private void paintGewichtskarte(Graphics g) {
        if (_gewichtsbild == null) {
            erstelleImage();
        }

        //paint image
        //g.drawImage(_gewichtsbild, 0, 0, null);

        g.drawImage(_gewichtsbild, 0, 0, (int) (_gewichtsbild.getWidth() * scale), (int) (_gewichtsbild.getHeight() * scale), null);

        /*for (int k = 0; k < _karte.getMaxx(); k++) {
        for (int j = 0; j < _karte.getMaxy(); j++) {
        g.setColor(new Color(0, 0, _karte.getWert(k, j) / 1344000 * 254));
        g.fillRect(k, j, 1, 1);
        }
        }*/
    }

    /**
     * zeichnet die vektorkarte
     * @param welt
     */
    public void paintVectorKarte(Welt welt) {
        if (welt != null) {
            System.out.println("Vektorkarte wird gezeichnet");
            _welt = welt;
            _zeichneWelt = true;

            minx = 0;
            miny = 0;
            maxx = 0;
            maxy = 0;

            //suche max und min x und y indem alle koordinaten der stadtteile durchsucht werden
            for (Stadtteil stadtteil : welt.getStadtteile()) {
                for (Koordinate punkt : stadtteil.getPunkte()) {
                    if (punkt.getX() > maxx) {
                        maxx = punkt.getX();
                    }
                    if (punkt.getY() > maxy) {
                        maxy = punkt.getY();
                    }
                    if (punkt.getX() < minx) {
                        minx = punkt.getX();
                    }
                    if (punkt.getY() < miny) {
                        miny = punkt.getY();
                    }
                }
            }


            //
            minx -= _welt.getRadiusAutomaten();
            miny -= _welt.getRadiusAutomaten();

            maxx += _welt.getRadiusAutomaten();
            maxy += _welt.getRadiusAutomaten();
        }
    }

    private void paintVectorKarte(Graphics g, boolean zeichenUmrisseErneut) {
        Graphics2D g2d = (Graphics2D) g;

        //finde maximales gewicht
        int tempgew;
        int maxgewicht = -1;
        for (Stadtteil teil : _welt.getStadtteile()) {
            tempgew = teil.getGewichtNachFkt();
            if (tempgew > maxgewicht) {
                maxgewicht = tempgew;
            }
        }

        //polygone zeichnen
        Polygon polygon = new Polygon();

        for (Stadtteil teil : _welt.getStadtteile()) {
            polygon.reset();

            for (int j = 0; j < teil.getPunkte().length; j++) {
                polygon.addPoint((int) ((teil.getPunkt(j).getX() - minx) * scale), (int) ((teil.getPunkt(j).getY() - miny) * scale));
            }

            //zeichne fläche nur beim ersten Mal, außenlinien beim zweiten Mal
            if (!zeichenUmrisseErneut) {
                //male flächen

                //farbe setzen
                float r = (float) (1 - ((float) teil.getGewichtNachFkt() / maxgewicht) * 0.6);
                g.setColor(new Color(r, r, r));
                g.fillPolygon(polygon);
            } else {
                //male außenlinien

                g.setColor(Color.BLACK);
                g.drawPolygon(polygon);

                try {
                    g.setColor(Color.BLACK);
                    g2d.scale(1, -1);
                    g2d.translate(0, -(maxy - miny) * scale);
                    if (_zeichneNamen) g.drawString(teil.getAttribut("Name"), (int) ((teil.getSchwerpunkt().getX() - minx) * scale), (int) (((maxy + miny) - teil.getSchwerpunkt().getY() - miny) * scale));
                    g2d.scale(1, -1);
                    g2d.translate(0, -(maxy - miny) * scale);
                } catch (Exception e) {
                    // Möglicherweise gibt es dieses Attribut gar nicht!
                    g2d.scale(1, -1);
                    g2d.translate(0, -(maxy - miny) * scale);
                }
            }
        }
    }

    public void paintAutomat(Welt welt, Zustand zustand) {
        if (welt != null && zustand != null && zustand.getAutomaten().length > 0) {
            _zustand = zustand;
            _welt = welt;
            _zeichneAutomat = true;
        }
    }

    private void paintAutomat(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int Radius = (int) (_welt.getRadiusAutomaten() * scale);

        if (_zustand == null || _zustand.getAutomaten() == null) {
            return;
        }

        for (int i = 0; i < _zustand.getAutomaten().length; i++) {
            if (_zustand.getAutomat(i) != null) {
                int x = (int) ((_zustand.getAutomat(i).getPosition().getX() - minx) * scale);
                int y = (int) ((_zustand.getAutomat(i).getPosition().getY() - miny) * scale);

                g2d.setStroke(new BasicStroke(1.0f));
                if (verschautomat.gefangen && verschautomat.nummer == i) {
                    g2d.setStroke(new BasicStroke(2.0f));
                }

                // Zeichnet den Kreis
                g2d.setColor(Color.BLACK);
                if (_zustand.getAutomat(i).getGesperrt()) {
                    g2d.setColor(Color.BLUE);
                }

                g2d.drawOval(x - Radius, y - Radius, 2 * Radius, 2 * Radius);

                //zeichne kleines Kreuz
                g2d.setColor(new Color(0, 0, 0, 0.3f));
                double fak = 0.1;
                g2d.drawLine((int) (x - Radius * fak), (int) (y - Radius * fak), (int) (x + Radius * fak), (int) (y + Radius * fak));
                g2d.drawLine((int) (x + Radius * fak), (int) (y - Radius * fak), (int) (x - Radius * fak), (int) (y + Radius * fak));

                g2d.setStroke(new BasicStroke(1.0f));
            }
        }
    }

    void setScale(double value) {
        //System.out.println("Scale value: "+value);
        //System.out.println("maxx: "+maxx);
        //System.out.println("bounds "+this.getBounds() + this.getParent().getBounds());
        double scale1 = value * this.getParent().getBounds().getWidth() / (maxx - minx);
        double scale2 = value * this.getParent().getBounds().getHeight() / (maxy - miny);

        if (scale1 < scale2) {
            scale = scale1;
        } else {
            scale = scale2;
        }

        //System.out.println("Scale: " + scale);

        updateScale();
    }

    public void updateScale() {
        //System.out.println("Größe: " + maxx*scale + "x" + maxy*scale);
        Dimension dimension = new Dimension((int) ((maxx - minx) * scale), (int) ((maxy - miny) * scale));
        this.setPreferredSize(dimension);
        this.setSize(dimension);
    }

    private void erstelleImage() {
        System.out.println("gewichtskarte zeichnen");

        _gewichtsbild = new BufferedImage(_zustand.getGewichtskarte().getMaxx() - _zustand.getGewichtskarte().getMinx(), _zustand.getGewichtskarte().getMaxy() - _zustand.getGewichtskarte().getMiny(), BufferedImage.TYPE_4BYTE_ABGR);
        //Graphics2D gewichtsbild = _gewichtsbild.createGraphics();

        _gewichtskarte = _zustand.getGewichtskarte();
        //_gewichtskarte.schreibe();

        //finde maximalen wert
        long tmpwert;
        long maxWert = 0;
        for (int x = _gewichtskarte.getMinx(); x < _gewichtskarte.getMaxx(); x++) {
            for (int y = _gewichtskarte.getMiny(); y < _gewichtskarte.getMaxy(); y++) {
                tmpwert = _gewichtskarte.getWert(x, y);
                if (tmpwert > maxWert) {
                    maxWert = tmpwert;
                }
            }
        }

        System.out.println("maxWert: " + maxWert);

        //erstelle bild
        for (int x = _gewichtskarte.getMinx(); x < _gewichtskarte.getMaxx(); x++) {
            for (int y = _gewichtskarte.getMiny(); y < _gewichtskarte.getMaxy(); y++) {
                tmpwert = _gewichtskarte.getWert(x, y);
                float farbe = (float) ((float) (tmpwert) / (maxWert) * 0.8);
                try {
                    _gewichtsbild.setRGB(x - _gewichtskarte.getMinx(), y - _gewichtskarte.getMiny(), new Color(1, 0, 0, farbe).getRGB());
                } catch (Exception e) {
                    System.out.println("Fehler: " + e + " farbe " + farbe + " tmpwert: " + tmpwert + " maxwert: " + maxWert + " Koordinate: " + (x - _gewichtskarte.getMinx()) + "x" + (y - _gewichtskarte.getMiny()) + " " + x + " " + y + " " + _gewichtskarte.getMinx() + " " + _gewichtskarte.getMiny());
                }
            }
        }

        //bild horizontal kippen
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -_gewichtsbild.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        _gewichtsbild = op.filter(_gewichtsbild, null);

        //auf festplatte speichern
        /*try {
        File outputfile = new File("gewichtskarte.png");
        ImageIO.write(_gewichtsbild, "png", outputfile);
        } catch (IOException e) {
        System.out.println("Fehler" + e);
        }*/
    }

    void noPixelmap() {
        this._zeichneGewichtskarte = false;
    }

    /*=====================================================*/
    /**
     * Klasse für den gefangenen Automaten
     */
    private static class verschautomat {

        private static boolean gefangen;
        private static int nummer;
        private static Koordinate anfangsposition;

        public verschautomat() {
        }
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("Position: " + (e.getX() / scale + minx) + " " + (maxy - e.getY() / scale));

        // Variablen für den Automat
        Automat automat = null;
        int automatIndex = -1;

        // Auf welchen Automat wurde geklickt?
        for (int i = 0; i < _zustand.getAutomaten().length; i++) {
            if (_zustand.getAutomaten()[i] != null) {
                double distX = _zustand.getAutomat(i).getPosition().getX() - (e.getX() / scale + minx);
                double distY = _zustand.getAutomat(i).getPosition().getY() - (maxy - e.getY() / scale);
                int abstand = (int) (distX * distX + distY * distY);

                if (abstand < ((_welt.getRadiusAutomaten() / scale) * (_welt.getRadiusAutomaten() / scale) * 0.3)) {
                    // Dieser Automat wurde ausgewählt
                    automat = _zustand.getAutomat(i);
                    automatIndex = i;
                    break;
                }
            }
        }

        if (_setzenModus) {
            if (!_zustand.getWeltfunktion().getPixelUndGewichtskarteVorhanden()) {
                // Pixelkarte wurde noch nicht berechnet
                Nachrichten.WarnungAusgeben("6");
                return;
            }

            Koordinate neuePosition = new Koordinate((int) (e.getX() / scale + minx), (int) (maxy - e.getY() / scale));

            // Prüfe, ob diese Position gültig ist
            for (int i = 0; i < _zustand.getAutomaten().length; i++) {
                if (_zustand.getAutomaten()[i] != null) {
                    Koordinate aktuellerAutomat = _zustand.getAutomat(i).getPosition();
                    Koordinate differenz = new Koordinate(aktuellerAutomat.getX() - neuePosition.getX(), aktuellerAutomat.getY() - neuePosition.getY());

                    if (differenz.getX() * differenz.getX() + differenz.getY() * differenz.getY() < 4 * _zustand.getWelt().getRadiusAutomaten() * _zustand.getWelt().getRadiusAutomaten()) {
                        // Automat würde sich mit anderem Automaten überschneiden, also abbrechen!
                        Nachrichten.WarnungAusgeben("5");
                        return;
                    }
                }
            }

            _zustand.SetzeNaechstenAutomaten(neuePosition);
            repaint();
        }

        if (automatIndex != -1) {
            if (_gesperrtInvertierenModus) {
                automat.setGesperrt(!automat.getGesperrt());
            }

            if (_loeschenModus) {
                _zustand.setAutomat(automatIndex, null);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed");

        if (_zeichneAutomat) {
            Automat gefangener = null;
            for (int i = 0; i < _zustand.getAutomaten().length; i++) {
                if (_zustand.getAutomaten()[i] != null) {
                    double distX = _zustand.getAutomat(i).getPosition().getX() - (e.getX() / scale + minx);
                    double distY = _zustand.getAutomat(i).getPosition().getY() - (maxy - e.getY() / scale);
                    int abstand = (int) (distX * distX + distY * distY);

                    //System.out.println("Position: " + _zustand.getAutomat(i).getPosition().getX() +" "+ _zustand.getAutomat(i).getPosition().getY());
                    //System.out.println("Abstand:" + abstand + " " + distX + " " + distY);
                    //System.out.println("Radius: " + _welt.getRadiusAutomaten()*_welt.getRadiusAutomaten());

                    if (abstand < ((_welt.getRadiusAutomaten() / scale) * (_welt.getRadiusAutomaten() / scale) * 0.3)) {
                        System.out.println("Automat Nummer " + i + " gefangen");

                        if (verschautomat.nummer != i) {
                            // Automatenindex und Koordinate speichern, um unzulässige Verschiebungen festzustellen
                            verschautomat.nummer = i;
                            verschautomat.anfangsposition = _zustand.getAutomat(i).getPosition();
                        }

                        verschautomat.gefangen = true;
                        verschautomat.nummer = i;
                        verschautomat.anfangsposition = _zustand.getAutomat(i).getPosition();
                        vorher = _zustand.getBewertung();

                        //abbrechen
                        i = _zustand.getAutomaten().length;
                    }
                }
            }
        }


    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released");

        if (verschautomat.gefangen) {
            verschautomat.gefangen = false;

            Koordinate neuePosition = _zustand.getAutomat(verschautomat.nummer).getPosition();
            // Gültigkeit prüfen: Automaten dürfen sich nicht überschneiden!
            for (int i = 0; i < _zustand.getAutomaten().length; i++) {
                if (_zustand.getAutomaten()[i] != null) {
                    if (i != verschautomat.nummer) {
                        Koordinate aktuellerAutomat = _zustand.getAutomat(i).getPosition();
                        Koordinate differenz = new Koordinate(aktuellerAutomat.getX() - neuePosition.getX(), aktuellerAutomat.getY() - neuePosition.getY());

                        if (differenz.getX() * differenz.getX() + differenz.getY() * differenz.getY() < 4 * _zustand.getWelt().getRadiusAutomaten() * _zustand.getWelt().getRadiusAutomaten()) {
                            // Automat würde sich mit anderem Automaten überschneiden, also abbrechen!
                            _zustand.getAutomat(verschautomat.nummer).setPosition(verschautomat.anfangsposition);
                            repaint();
                            verschautomat.nummer = -1;
                            Nachrichten.WarnungAusgeben("5");
                            return;
                        }
                    }
                }
            }
            verschautomat.nummer = -1;

            System.out.println("Automat gesetzt an " + neuePosition.toString());

            repaint();
        }

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse entered");
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse exited");
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseDragged(MouseEvent e) {
        //System.out.println("Mouse dragged");

        if (verschautomat.gefangen) {
            _zustand.getAutomat(verschautomat.nummer).setPosition(new Koordinate((int) (e.getX() / scale + minx), (int) (maxy - e.getY() / scale)));
            this.repaint();
        }

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseMoved(MouseEvent e) {
        //System.out.println("Mouse moved");
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
