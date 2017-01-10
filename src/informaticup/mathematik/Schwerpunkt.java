/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informaticup.mathematik;

import informaticup.datenstruktur.Koordinate;

/**
 *
 *
 */
public class Schwerpunkt {

    public static KoordinateDouble SchwerpunktPolygon(Koordinate[] koordinate) {
        double A = 0.0;
        double Cx = 0.0;
        double Cy = 0.0;

        int len = koordinate.length;
        for (int i = 0; i < len; i++) {
            // Fläche des Polygons berechnen
            double temp = koordinate[i].getX() * koordinate[(i + 1) % len].getY() - koordinate[(i + 1) % len].getX() * koordinate[i].getY();
            A += temp;
            // Flächenschwerpunkt berechnen
            Cx += (koordinate[i].getX() + koordinate[(i + 1) % len].getX()) * temp;
            Cy += (koordinate[i].getY() + koordinate[(i + 1) % len].getY()) * temp;
        }

        A *= 0.5;
        Cx *= 1.0 / (6.0 * A);
        Cy *= 1.0 / (6.0 * A);

        return new KoordinateDouble(Cx, Cy);
    }
}
