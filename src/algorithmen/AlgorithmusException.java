package algorithmen;

/**
 * Diese Exception kann ausgelöst werden, wenn ein Algorithmus fehltschlägt.
 */
public class AlgorithmusException extends Exception {

    // Fehlercode der Ausnahme
    public AlgorithmusException.AlgorithmusErrorCode errorCode;

    /**
     * Konstruktur für die Ausnahme.
     * @param _message Fehlernachricht
     * @param _errorCode Fehlercode
     */
    public AlgorithmusException(String _message, AlgorithmusException.AlgorithmusErrorCode _errorCode) {
        super(_message);
        this.errorCode = _errorCode;
    }

    /**
     * Konstruktor für die Ausnahme.
     * @param _errorCode Fehlercode
     */
    public AlgorithmusException(AlgorithmusException.AlgorithmusErrorCode _errorCode) {
        super(_errorCode.toString());
    }

    /**
     * Aufzählung der verschiedenen Fehlercodes.
     */
    public static enum AlgorithmusErrorCode {

        KEIN_ZULAESSIGER_PUNKT, UNSPECIFIED, NO_MORE_SPACE;

        /**
         * Gibt die Beschreibung (Erklärung) zu einem Fehlercode zurück.
         * @return Beschreibung
         */
        @Override
        public String toString() {
            switch (this) {
                case KEIN_ZULAESSIGER_PUNKT:
                    return "Algorithmus: Es konnte keine Position ermittelt werden, an dem ein neuer Automat platziert werden kann.";
                case NO_MORE_SPACE:
                    return "Algorithmus: Es konnte keine Position ermittelt werden, an dem ein neuer Automat platziert werden kann. Die karte ist voll.";

            }
            return "Algorithmus: Unbekannte Ausnahme.";
        }
    }
}
