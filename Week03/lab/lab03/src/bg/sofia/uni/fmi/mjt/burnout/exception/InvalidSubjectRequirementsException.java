package bg.sofia.uni.fmi.mjt.burnout.exception;

public class InvalidSubjectRequirementsException extends Exception {
    public InvalidSubjectRequirementsException() {
        super("Duplicate in required categories.");
    }
    public InvalidSubjectRequirementsException(String message) {
        super(message);
    }
}
