package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class DurationNonPositiveException extends RuntimeException {
    public DurationNonPositiveException(String message) {
        super(message);
    }

    public DurationNonPositiveException(Throwable cause, String message) {
        super(message);
    }
}
