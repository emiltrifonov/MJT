package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

import java.util.Arrays;

public final class LowestRatingEliminationRule extends AbstractEliminationRule {
    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        ergenkas = removeNullErgenkasFromArray(ergenkas);

        if (ergenkas == null || ergenkas.length == 0) {
            return ergenkas;
        }

        int lowestRating = getLowestRatingForErgenkas(ergenkas);
        int remainingErgenkasCount =
                ergenkas.length - getLowestRatedErgenkasCount(ergenkas, lowestRating);

        Ergenka[] remainingErgenkas = new Ergenka[remainingErgenkasCount];
        Ergenka[] ergenkasCopy = Arrays.copyOf(ergenkas, ergenkas.length);

        int remainingErgenkasIndex = 0;

        for (Ergenka ergenka : ergenkasCopy) {
            if (remainingErgenkasIndex >= remainingErgenkasCount) {
                break;
            }
            if (ergenka != null && ergenka.getRating() != lowestRating) {
                remainingErgenkas[remainingErgenkasIndex++] = ergenka;
            }
        }

        return remainingErgenkas;
    }

    private int getLowestRatingForErgenkas(Ergenka[] ergenkas) {
        int lowestRating = Integer.MAX_VALUE;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka != null) {
                lowestRating = Math.min(lowestRating, ergenka.getRating());
            }
        }

        return lowestRating;
    }

    private int getLowestRatedErgenkasCount(Ergenka[] ergenkas, int lowestRating) {
        int lowestRatedErgenkasCount = 0;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka.getRating() == lowestRating) {
                lowestRatedErgenkasCount++;
            }
        }

        return lowestRatedErgenkasCount;
    }
}
