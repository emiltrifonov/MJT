package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.algorithms.MajorityVoteAlgorithm;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

import java.util.Arrays;

public final class PublicVoteEliminationRule extends AbstractEliminationRule {
    private boolean isVoteSuccessful = false;
    private final String[] votes;

    public PublicVoteEliminationRule(String[] votes) {
        votes = removeNullVotesFromArray(votes);

        if (votes == null || votes.length == 0) {
            this.votes = new String[0];
        }
        else {
            this.votes = Arrays.copyOf(votes, votes.length);
        }
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0 || votes == null || votes.length == 0) {
            return ergenkas;
        }

        String eliminatedErgenkaName = getEliminatedErgenkaName();
        Ergenka[] ergenkasCopy = Arrays.copyOf(ergenkas, ergenkas.length);

        if (isVoteSuccessful) {
            int remainingErgenkasCount = ergenkas.length;
            if (isEliminatedErgenkaPartOfErgenkas(eliminatedErgenkaName, ergenkas)) {
                remainingErgenkasCount--;
            }
            Ergenka[] remainingErgenkas = new Ergenka[remainingErgenkasCount];
            int remainingErgenkasIndex = 0;

            for (Ergenka ergenka : ergenkasCopy) {
                if (remainingErgenkasIndex >= remainingErgenkasCount) {
                    break;
                }

                if (!eliminatedErgenkaName.equals(ergenka.getName())) {
                    remainingErgenkas[remainingErgenkasIndex++] = ergenka;
                }
            }

            return remainingErgenkas;
        }
        else {
            return ergenkasCopy;
        }
    }

    private String getEliminatedErgenkaName() {
        String eliminatedErgenkaName = MajorityVoteAlgorithm.execute(votes);

        if (isEliminationMajorityVote(eliminatedErgenkaName)) {
            isVoteSuccessful = true;
        }

        return eliminatedErgenkaName;
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

    private String[] removeNullVotesFromArray(String[] votes) {
        if (votes == null || votes.length == 0) {
            return votes;
        }

        int nonNullVotesCount = getCountOfNonNullVotes(votes);
        String[] nonNullVotes = new String[nonNullVotesCount];
        int index = 0;

        for (String vote : votes) {
            if (vote != null) {
                nonNullVotes[index++] = vote;
            }
        }

        return nonNullVotes;
    }

    private int getCountOfNonNullVotes(String[] votes) {
        int count = 0;

        for (String vote : votes) {
            if (vote != null) {
                count++;
            }
        }

        return count;
    }

    private boolean isEliminatedErgenkaPartOfErgenkas(String eliminatedErgenka, Ergenka[] ergenkas) {
        for (Ergenka ergenka : ergenkas) {
            if (ergenka.getName().equals(eliminatedErgenka)) {
                return true;
            }
        }

        return false;
    }
}
