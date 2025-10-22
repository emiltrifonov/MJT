package bg.sofia.uni.fmi.mjt.show.algorithms;

public class MajorityVoteAlgorithm {
    /**
     * Boyer-Moore algorithm
     */
    public static String execute(String[] votes) {
        String majorityElement = votes[0];

        int counter = 0;

        for (String vote : votes) {
            if (vote.equals(majorityElement)) {
                counter++;
            }
            else {
                counter--;
                if (counter == 0) {
                    majorityElement = vote;
                }
            }
        }

        return majorityElement;
    }
}
