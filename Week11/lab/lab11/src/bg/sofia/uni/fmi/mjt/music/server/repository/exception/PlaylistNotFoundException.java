package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class PlaylistNotFoundException extends Exception {
    public PlaylistNotFoundException(String message) {
        super(message);
    }

    public PlaylistNotFoundException(Throwable cause, String message) {
        super(message);
    }
}
