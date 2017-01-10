package informaticup;

import java.io.*;

/**
 * Liest eine Datei auf dem Dateisystem in das Programm ein.
 */
public class Dateiimport {

    /**
     * Liest eine Datei ein und schreibt ihn in eine Zeichenfolge.
     * @param pfad Dateiname (Pfad)
     * @return Zeichenfolge
     * @throws IOException Ausnahme, die ausgelöst wird, wenn die Datei nicht eingelesen werden konnte
     */
    public static String dateiEinlesen(String pfad) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try {
            BufferedReader Reader = new BufferedReader(new FileReader(new File(pfad)));
            try {

                String line = null;
                while ((line = Reader.readLine()) != null) {
                    fileContent.append(line);
                    fileContent.append("\n");
                }
            } catch (IOException e) {
                throw e;
            } finally {
                Reader.close();
            }
        } catch (IOException e) {
            throw e;
        }

        return fileContent.toString();
    }
}
