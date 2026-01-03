package bg.sofia.uni.fmi.mjt.music.server.model;

import bg.sofia.uni.fmi.mjt.music.server.repository.exception.DurationNonPositiveException;

import java.util.Objects;

public record Song(String title, String artist, int duration) {

    public Song {
        if (duration <= 0) {
            throw new DurationNonPositiveException("Duration must be a positive integer.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song song)) return false;
        return Objects.equals(title(), song.title()) && Objects.equals(artist(), song.artist());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title(), artist());
    }
}
