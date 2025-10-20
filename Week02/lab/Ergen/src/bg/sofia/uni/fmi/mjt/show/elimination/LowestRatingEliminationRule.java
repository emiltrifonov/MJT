package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowestRatingEliminationRule implements EliminationRule {
    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        int lowestRating = getLowestRatingForErgenkas(ergenkas);
        int remainingErgenkasCount =
                ergenkas.length - getLowestRatedErgenkasCount(ergenkas,lowestRating);
        Ergenka[] remainingErgenkas = new Ergenka[remainingErgenkasCount];

        int remainingErgenkasIndex = 0;
        for (Ergenka ergenka : ergenkas) {
            if (ergenka.getRating() != lowestRating) {
                remainingErgenkas[remainingErgenkasIndex++] = ergenka;
            }
        }

        return remainingErgenkas;
    }

    private int getLowestRatingForErgenkas(Ergenka[] ergenkas) {
        int lowestRating = Integer.MAX_VALUE;

        for (Ergenka ergenka : ergenkas) {
            lowestRating = Math.min(lowestRating, ergenka.getRating());
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
