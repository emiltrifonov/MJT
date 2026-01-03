package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class PlaylistAlreadyExistsException extends Exception {
    public PlaylistAlreadyExistsException(String message) {
        super(message);
    }

    public PlaylistAlreadyExistsException(Throwable cause, String message) {
        super(message);
    }
}
