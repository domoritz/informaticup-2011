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
public class Vektorrechnung {

    /**
     * Prüft, ob die (unendliche) Gerade b die Strecke a schneidet.
     * @param a1, a2, b1, b2
     * @return int für entspechende Schnittvariationen
     */

    public static int StreckenSchnittVorhanden(KoordinateDouble a1, KoordinateDouble a2, KoordinateDouble b1, KoordinateDouble b2) {
        // Berechne Vektoren
        KoordinateDouble a = new KoordinateDouble();
        KoordinateDouble b = new KoordinateDouble();

        a.setX(a2.getX() - a1.getX());
        a.setY(a2.getY() - a1.getY());
        b.setX(b2.getX() - b1.getX());
        b.setY(b2.getY() - b1.getY());

        // Berechne Winkel
        double winkel = (a.getX() * b.getX() + a.getY() * b.getY()) / (Math.sqrt(a.getX() * a.getX() + a.getY() * a.getY()) * Math.sqrt(b.getX() * b.getX() + b.getY() * b.getY()));

        if (Gleichheit(winkel, Math.PI) || Gleichheit(winkel, 0.0)) {
            // Vektoren sind parallel
            return -1;
        }

        KoordinateDouble P = new KoordinateDouble(a1.getX() - b1.getX(), a1.getY() - b1.getY());
        
        double lambda = (P.getX() * b.getY() - P.getY() * b.getX()) / (a.getY() * b.getX() - a.getX() * b.getY());
        
        double mu = 0.0;

        if (!Gleichheit(b.getX(), 0.0))
        {
             mu = (P.getX() + lambda * a.getX()) / b.getX();
        }
        else
        {
            if (!Gleichheit(b.getY(), 0.0)) // Division durch 0 verhindern
            mu = (P.getY() + lambda * a.getY()) / b.getY();
        }

        if (Gleichheit(lambda, 0.0) || Gleichheit(lambda, 1.0)) {
            // Gerade verläuft durch eine Ecke
            return -1;
        }

        if ((lambda > 0.0) && (lambda < 1.0) && (mu > 0.0)) {
            return 1;
        } else {
            return 0;
        }
    }
   /**
    * Sind zwei Doubles auf eine gewisse Ungenauigkeit gleich?
    * @param x
    * @param y
    * @return Gleichheit oder Ungleichheit
    */
    public static boolean Gleichheit(double x, double y) {
        return (Math.abs(x - y) < 0.0001);
    }
}
