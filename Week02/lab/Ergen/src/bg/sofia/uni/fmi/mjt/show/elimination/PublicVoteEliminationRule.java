package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class PublicVoteEliminationRule implements EliminationRule {
    static final String INVALID_ERGENKA_NAME = "";
    private final String[] votes;

    public PublicVoteEliminationRule(String[] votes) {
        this.votes = votes;
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

    /**
      * Boyer-Moore algorithm
      */
    private String getEliminatedErgenkaName() {
        String eliminatedErgenkaName = votes[0];

        int counter = 0;

        for (String vote : votes) {
            if (vote.equals(eliminatedErgenkaName)) {
                counter++;
            }
            else {
                counter--;
                if (counter == 0) {
                    eliminatedErgenkaName = vote;
                }
            }
        }

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
