package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class SongAlreadyExistsException extends Exception {
    public SongAlreadyExistsException(String message) {
        super(message);
    }

    public SongAlreadyExistsException(Throwable cause, String message) {
        super(message);
    }
}
