package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public abstract class AbstractEliminationRule implements EliminationRule {
    protected Ergenka[] removeNullErgenkasFromArray(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0) {
            return ergenkas;
        }

        int nonNullErgenkasCount = getCountOfNonNullErgenkas(ergenkas);
        Ergenka[] nonNullErgenkas = new Ergenka[nonNullErgenkasCount];
        int index = 0;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka != null) {
                nonNullErgenkas[index++] = ergenka;
            }
        }

        return nonNullErgenkas;
    }

    private int getCountOfNonNullErgenkas(Ergenka[] ergenkas) {
        int count = 0;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka != null) {
                count++;
            }
        }

        return count;
    }
}
