package bg.sofia.uni.fmi.mjt.burnout.subject;

/**
 *
 * @param category the academic category this subject belongs to
 * @param minAmountEnrolled minimum amount of subject enrolled for the category
 *
 * @throws IllegalArgumentException if the category is null
 * @throws IllegalArgumentException if the minAmountEnrolled is negative
 */
public record SubjectRequirement(Category category, int minAmountEnrolled) {
    public SubjectRequirement {
        if (category == null) {
            throw new IllegalArgumentException("Null category in SubjectRequirement constructor.");
        }
        else if (minAmountEnrolled < 0) {
            throw new IllegalArgumentException("Negative minAmountEnrolled in SubjectRequirement constructor.");
        }
    }
}
