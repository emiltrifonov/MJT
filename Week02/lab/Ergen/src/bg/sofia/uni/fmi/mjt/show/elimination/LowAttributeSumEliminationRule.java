package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowAttributeSumEliminationRule implements EliminationRule {
    final int threshold;

    public LowAttributeSumEliminationRule(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        Ergenka[] remainingErgenkas = new Ergenka[getCountOfErgenkasBelowThreshold(ergenkas)];

        int remainingErgenkasIndex = 0;
        for (Ergenka ergenka : ergenkas) {
            if (ergenka.getHumorLevel() + ergenka.getRomanceLevel() >= threshold) {
                remainingErgenkas[remainingErgenkasIndex++] = ergenka;
            }
        }

        return remainingErgenkas;
    }

    private int getCountOfErgenkasBelowThreshold(Ergenka[] ergenkas) {
        int countOfErgenkasBelowThreshold = 0;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka.getHumorLevel() + ergenka.getRomanceLevel() < threshold) {
                countOfErgenkasBelowThreshold++;
            }
        }

        return countOfErgenkasBelowThreshold;
    }
}
