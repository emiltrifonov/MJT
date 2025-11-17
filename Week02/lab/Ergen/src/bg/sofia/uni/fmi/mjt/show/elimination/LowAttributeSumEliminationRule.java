package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

import java.util.Arrays;

public final class LowAttributeSumEliminationRule extends AbstractEliminationRule {
    private final int threshold;

    public LowAttributeSumEliminationRule(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0) {
            return ergenkas;
        }

        int remainingErgenkasCount = getCountOfErgenkasAboveThreshold(ergenkas);

        if (remainingErgenkasCount == 0) {
            return new Ergenka[0];
        }

        Ergenka[] remainingErgenkas = new Ergenka[remainingErgenkasCount];
        Ergenka[] ergenkasCopy = Arrays.copyOf(ergenkas, ergenkas.length);

        int remainingErgenkasNonNullIndex = 0;
        int remainingErgenkasNullIndex = remainingErgenkas.length - 1;
        for (Ergenka ergenka : ergenkasCopy) {
            if (remainingErgenkasNonNullIndex >= remainingErgenkasCount
                    || remainingErgenkasNullIndex < 0) {
                break;
            }
            if (isErgenkaAboveThreshold(ergenka, threshold)) {
                if (ergenka == null) {
                    remainingErgenkas[remainingErgenkasNullIndex--] = null;
                }
                else {
                    remainingErgenkas[remainingErgenkasNonNullIndex++] = ergenka;
                }
            }
        }

        return remainingErgenkas;
    }

    private int getCountOfErgenkasAboveThreshold(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0) {
            return 0;
        }

        int countOfErgenkasBelowThreshold = 0;

        for (Ergenka ergenka : ergenkas) {
            if (isErgenkaAboveThreshold(ergenka, threshold)) {
                countOfErgenkasBelowThreshold++;
            }
        }

        return countOfErgenkasBelowThreshold;
    }

    private boolean isErgenkaAboveThreshold(Ergenka ergenka, int threshold) {
        return ergenka == null || (ergenka.getHumorLevel() + ergenka.getRomanceLevel()) >= threshold;
    }
}