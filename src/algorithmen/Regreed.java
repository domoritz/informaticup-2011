package algorithmen;

import informaticup.Algorithmus;
import informaticup.Weltfunktion;
import informaticup.datenstruktur.Automat;
import informaticup.datenstruktur.Karte;
import informaticup.datenstruktur.Welt;
import informaticup.datenstruktur.Zustand;

/**
 * Der Regreed-Algorithmus ist ein abschließender Optimierungsalgorithmus,
 * der die schlechtestpositionierten Automaten versucht, per Greedy-Regeln
 * derart zu verschieben, dass sie eine bessere Position erhalten und somit
 * die Gesamtbewertung steigt.
 */
public class Regreed implements informaticup.Algorithmus {

    private Algorithmus.ProgressCallbackDelegate _progressDelegate;
    private Zustand _zustand;
    private Weltfunktion _weltfunktion;
    private Welt _welt;

    public Regreed(Weltfunktion weltfunktion, Zustand zustand) {
        this._zustand = zustand;
        this._weltfunktion = weltfunktion;
        this._welt = this._weltfunktion.getWelt();
    };

    public void Berechne(Algorithmus.ProgressCallbackDelegate delegate) throws AlgorithmusException {
        Karte gewichtskarte = _zustand.getGewichtskarte();

        boolean aenderungGemacht = true;

        Automat schlechtesterAutomat;
        long schlechtesteGuete;
        long automatGuete;

        while (aenderungGemacht) {
            schlechtesterAutomat = _zustand.getAutomat(0);
            schlechtesteGuete = gewichtskarte.getWert(schlechtesterAutomat.getPosition());
            automatGuete = -1;

            for (Automat A : _zustand.getAutomaten()) {
                automatGuete = gewichtskarte.getWert(A.getPosition());
                if (automatGuete < schlechtesteGuete) {
                    schlechtesterAutomat = A;
                    schlechtesteGuete = gewichtskarte.getWert(A.getPosition());
                }
            }

            aenderungGemacht = this.optimiereAutomat(schlechtesterAutomat);
        }
    }

    public boolean optimiereAutomat(Automat A) {
        Karte gewichtskarte = _zustand.getGewichtskarte();
        
        int erlaubtePositionen[][] = new int[gewichtskarte.getBreite()][gewichtskarte.getHoehe()];
        for (int x = 0; x < gewichtskarte.getBreite(); x++) {
            for (int y = 0; y < gewichtskarte.getHoehe(); y++) {

            }
        }

        return false;
    }

    public Zustand getErgebnis() {
        return this._zustand;
    }

    public int getFortschritt() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getStatusText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
