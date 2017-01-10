/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informaticup.mathematik;

/**
 *
 * 
 */
public class KoordinateDouble {

    private double _x;
    private double _y;
/**
 * Konstruktor
 * @param x, y
 */
    public KoordinateDouble(double x, double y) {
        _x = x;
        _y = y;
    }

    public KoordinateDouble() {
    }
   /** Getterfunktion
     * @param
     * @return X
     */
    public double getX() {
        return _x;
    }
    /** Getterfunktion
     * @param
     * @return Y
     */
    public double getY() {
        return _y;
    }
    /**
     * Settermethode
     * @param x
     * @return
     */
    public void setX(double x) {
        _x = x;
    }
   /**
     * Settermethode
     * @param y
     * @return
     */
    public void setY(double y) {
        _y = y;
    }
}
