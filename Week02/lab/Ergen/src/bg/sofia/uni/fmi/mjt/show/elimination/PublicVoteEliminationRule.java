package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.algorithms.MajorityVoteAlgorithm;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

import java.util.Arrays;

public final class PublicVoteEliminationRule implements EliminationRule {
    private static final String INVALID_ERGENKA_NAME = "";
    private final String[] votes;

    public PublicVoteEliminationRule(String[] votes) {
        this.votes = Arrays.copyOf(votes, votes.length);
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        String eliminatedErgenkaName = getEliminatedErgenkaName();

        if (!eliminatedErgenkaName.equals(INVALID_ERGENKA_NAME)) {
            Ergenka[] remainingErgenkas = new Ergenka[ergenkas.length - 1];
            int remainingErgenkasIndex = 0;

            for (Ergenka ergenka : ergenkas) {
                if (!eliminatedErgenkaName.equals(ergenka.getName())) {
                    remainingErgenkas[remainingErgenkasIndex++] = ergenka;
                }
            }

            return remainingErgenkas;
        }
        else {
            return ergenkas;
        }
    }

    private String getEliminatedErgenkaName() {
        String eliminatedErgenkaName = MajorityVoteAlgorithm.execute(votes);

        if (isEliminationMajorityVote(eliminatedErgenkaName)) {
            return eliminatedErgenkaName;
        }
        else {
            return INVALID_ERGENKA_NAME;
        }
    }

    private boolean isEliminationMajorityVote(String elimination) {
        int counter = 0;

        for (String vote : votes) {
            if (vote.equals(elimination)) {
                counter++;
            }
        }

        return counter > votes.length / 2;
    }
}
