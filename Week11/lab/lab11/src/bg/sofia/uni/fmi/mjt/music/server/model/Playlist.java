package bg.sofia.uni.fmi.mjt.music.server.model;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Playlist {

    private final String name;
    private final Map<Song, Integer> songs = new ConcurrentHashMap<>();

    public Playlist(String name) {
        this.name = name;
    }

    public boolean hasSong(Song song) {
        return songs.containsKey(song);
    }

    public Song addSong(Song song) {
        songs.put(song, 0);

        return song;
    }

    public Integer like(Song song) {
        songs.compute(song, (_, oldLikes) -> oldLikes + 1);

        return songs.get(song);
    }

    public Integer unlike(Song song) {
        songs.compute(song, (_, oldLikes) -> {
            if (oldLikes == 0) {
                return 0;
            } else {
                return oldLikes - 1;
            }
        });

        return songs.get(song);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Playlist playlist)) return false;
        return Objects.equals(name, playlist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
