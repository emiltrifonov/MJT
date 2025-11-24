package bg.sofia.uni.fmi.mjt.burnout.subject;

/**
 * @param name the name of the subject
 * @param credits number of credit hours for this subject
 * @param rating difficulty rating of the subject (1-5)
 * @param category the academic category this subject belongs to
 * @param neededStudyTime estimated study time in days required for this subject
 *
 * @throws IllegalArgumentException if the name of the subject is null or blank
 * @throws IllegalArgumentException if the credits are not positive
 * @throws IllegalArgumentException if the rating is not in its bounds
 * @throws IllegalArgumentException if the Category is null
 * @throws IllegalArgumentException if the neededStudy time is not positive
 */
public record UniversitySubject(String name, int credits, int rating, Category category, int neededStudyTime) {
    private static final int RATING_MIN = 1;
    private static final int RATING_MAX = 5;

    public UniversitySubject {
        if (name == null) {
            throw new IllegalArgumentException("Null name in UniversitySubject constructor");
        }
        else if (name.isBlank()) {
            throw new IllegalArgumentException("Blank name in UniversitySubject constructor");
        }
        else if (credits <= 0) {
            throw new IllegalArgumentException("Negative credits in UniversitySubject constructor");
        }
        else if (rating < RATING_MIN || rating > RATING_MAX) {
            throw new IllegalArgumentException("Rating outside valid range in UniversitySubject constructor");
        }
        else if (category == null) {
            throw new IllegalArgumentException("Null category in UniversitySubject constructor");
        }
        else if (neededStudyTime <= 0) {
            throw new IllegalArgumentException("Negative neededStudyTime in UniversitySubject constructor");
        }
    }
}