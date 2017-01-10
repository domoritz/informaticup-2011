package informaticup;

import informaticup.datenstruktur.*;

/**
 * Parst eine Eingabedatei und erstellt eine Welt-Instanz.
 */
public class Parser {

    /**
     * Erstellt eine Welt-Instanz aus einer Zeichenfolge, die die Eingabedaten enthält.
     * @param input Eingabe-Zeichenfolge
     * @return Welt Instanz einer neuen Welt
     */
    public static Welt Parse(String input) throws ParserException {
        int _shift = 0;
        input = input.replace("\r", "\n");
        input = input.replace("\n\n", "\n");
        input = input.replace("\n\n", "\n");
        String[] lines = input.split("\n");

        int numAttr = 0;
        try {
            numAttr = Integer.parseInt(lines[0 + _shift]);
        } catch (Exception e) {
            throw new ParserException(ParserErrorCode.FILE_INVALID);
        }

        _shift += 1;
        String[] _attributnamen = new String[numAttr];
        for (int i = 0; i < numAttr; i++) {
            _attributnamen[i] = lines[i + _shift];
        }
        _shift += numAttr;
        int numViertel = Integer.parseInt(lines[0 + _shift]);
        _shift += 1;

        int _minLines = 1 + numAttr + 1 + (numViertel * 3) + 2;
        if (lines.length < _minLines) {
            throw (new ParserException(Parser.ParserErrorCode.NOT_ENOUGH_LINES));
        }

        Welt welt = new Welt(numViertel);

        for (int i = 0; i < numViertel; i++) {
            int _numPoly = Integer.parseInt(lines[(i * 3) + _shift]);
            String _poly = lines[(i * 3) + 1 + _shift];
            String _attr = lines[(i * 3) + 2 + _shift];

            Stadtteil _stadtteil = new Stadtteil(_numPoly, numAttr);

            for (int j = 0; j < numAttr; j++) {
                _stadtteil.setGewichtname(j, _attributnamen[j]);
            }

            String[] _attrs = _attr.split(" ");
            for (int j = 0; j < numAttr; j++) {
                //_stadtteil.setGewicht(j, Integer.parseInt(_attrs[j]));
                if (Parser.sollteNumerischerWertSein(_attrs[j])) {
                    _stadtteil.setGewicht(j, Integer.parseInt(_attrs[j]));
                } else {
                    _stadtteil.setAttribut(_attributnamen[j], _attrs[j]);
                }
            }

            String[] _koords = _poly.split(" ");
            for (int j = 0; j < _numPoly; j++) {
                Koordinate _koord = new Koordinate(
                        Integer.parseInt(_koords[(2 * j) + 0]),
                        Integer.parseInt(_koords[(2 * j) + 1]));
                _stadtteil.setPunkt(j, _koord);
            }

            welt.setStadtteil(i, _stadtteil);
        }
        _shift += (3 * numViertel);

        int numAutomaten = Integer.parseInt(lines[0 + _shift]);
        _shift += 1;

        int radiusAutomaten = Integer.parseInt(lines[0 + _shift]);
        _shift += 1;

        welt.setSollAnzahlAutomaten(numAutomaten);
        welt.setRadiusAutomaten(radiusAutomaten);

        return welt;
    }

    /**
     * Ausnahme, die ausgelöst wird, wenn der Parser fehlschlägt.
     */
    public static class ParserException extends Exception {

        // Fehlercode der Ausnahme
        public Parser.ParserErrorCode errorCode;

        /**
         * Konstruktor der Ausnahme.
         * @param _message Fehlernachricht
         * @param _errorCode Fehlercode
         */
        public ParserException(String _message, Parser.ParserErrorCode _errorCode) {
            super(_message);
            this.errorCode = _errorCode;
        }

        /**
         * Konstruktor der Ausnahme.
         * @param _errorCode Fehlercode
         */
        public ParserException(Parser.ParserErrorCode _errorCode) {
            super(_errorCode.toString());
        }
    }

    /**
     * Überprüft, ob es sich um einen numerischen Integer-Wert handelt.
     * @param a Zeichenfolge
     * @return Wahrheitswert
     */
    private static boolean sollteNumerischerWertSein(String a) {
        try {
            Integer.parseInt(a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Auflistung aller definierten Fehlercodes für den Parser.
     */
    public static enum ParserErrorCode {

        FILE_INVALID, UNSPECIFIED, NOT_ENOUGH_LINES;

        /**
         * Gibt die Beschreibung eines Fehlercodes zurück.
         * @return Zeichenfolge (Beschreibung)
         */
        @Override
        public String toString() {
            switch (this) {
                case FILE_INVALID:
                    return "Parser: invalid file.";
                case NOT_ENOUGH_LINES:
                    return "Parser: input file has not enough lines.";
            }
            return "Parser: Unspecified error.";
        }
    }
}
