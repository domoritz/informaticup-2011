package informaticup;

import java.io.*;
import informaticup.datenstruktur.*;

/**
 * Übersetzt die errechneten Automaten in der vorgeschriebenen Art und Weise in eine .txt wie in der Aufgabenstellung beschrieben.
 */
public class Dateiexport {

    /**
     * Schreibt einen Zustand mit den Automaten in eine Datei.
     * @param pfad Dateiname (Pfad)
     * @param Endzustand Zustand mit Automaten
     */
    public static void dateiAusgeben(String pfad, Zustand Endzustand) {
        Writer fw = null;
        int Approxrate = Endzustand.getWelt().getApproxrate();
        if (Endzustand.getBewertung() != -1) {
            try {
                // Einlesen der Datei
                fw = new FileWriter(pfad);
                // Ausgabe Anzahl der Automaten
                fw.write(String.valueOf(Endzustand.getAnzahlAutomaten()));
                fw.append(System.getProperty("line.separator"));

                for (int i = 0; i < Endzustand.getAnzahlAutomaten(); i++) {
                    // Position und Gewichtsausgabe für jeden Automaten
                    int x = Endzustand.getAutomat(i).getPosition().getX();
                    int y = Endzustand.getAutomat(i).getPosition().getY();

                    fw.write(String.valueOf(x * Approxrate));
                    fw.write(" ");
                    fw.write(String.valueOf(y * Approxrate));
                    fw.write(" ");
                    //long gewicht = Endzustand.zus;
                    //int gewicht= Endzustand.getWeltfunktion().getGewicht(Endzustand.getAutomat(i).getPosition());

                    // Gewichte müssen nicht wieder zurückapproximiert werden!
                    fw.write(String.valueOf(Endzustand.getGewichtskarte().getWert(x, y)));
                   
                    fw.append(System.getProperty("line.separator")); // e.g. "\n"
                }

                // Gesamtgewicht
                long Gesamtgewicht = Endzustand.getBewertung();
                fw.write(String.valueOf(Gesamtgewicht));
            } catch (Exception ex) {
                Nachrichten.WarnungAusgeben("1");
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        // Datei konnte nicht geschrieben werden
                    }
                }
            }
        } else {
            System.out.println("Zu frueh für Ausgabe");
        }
    }
}
