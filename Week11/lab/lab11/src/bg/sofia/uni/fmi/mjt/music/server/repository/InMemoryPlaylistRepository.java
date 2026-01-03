package bg.sofia.uni.fmi.mjt.music.server.repository;

import bg.sofia.uni.fmi.mjt.music.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.music.server.model.Song;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPlaylistRepository implements PlaylistRepository {

    private final Map<String, Playlist> playlists = new ConcurrentHashMap<>();

    @Override
    public void createPlaylist(String playlistName) throws PlaylistAlreadyExistsException {
        checkPlaylistAlreadyExists(playlistName);

        playlists.put(playlistName, new Playlist(playlistName));
    }

    @Override
    public Song addSong(String playlistName, String songTitle, String artistName, int duration)
            throws PlaylistNotFoundException, SongAlreadyExistsException {
        checkPlaylistNotFound(playlistName);

        Song song = new Song(songTitle, artistName, duration);
        checkSongAlreadyExists(playlistName, song);

        return playlists.get(playlistName).addSong(song);
    }

    @Override
    public int likeSong(String playlistName, String songTitle, String artistName)
            throws PlaylistNotFoundException, SongNotFoundException {
        checkPlaylistNotFound(playlistName);

        Song song = new Song(songTitle, artistName, 1);
        checkSongNotFound(playlistName, song);

        return playlists.get(playlistName).like(song);
    }

    @Override
    public int unlikeSong(String playlistName, String songTitle, String artistName)
            throws PlaylistNotFoundException, SongNotFoundException {
        checkPlaylistNotFound(playlistName);

        Song song = new Song(songTitle, artistName, 1);
        checkSongNotFound(playlistName, song);

        return playlists.get(playlistName).unlike(song);
    }

    @Override
    public Collection<String> getAllPlaylists() {
        return playlists.keySet();
    }

    @Override
    public Playlist getPlaylist(String playlistName) throws PlaylistNotFoundException {
        checkPlaylistNotFound(playlistName);

        return playlists.get(playlistName);
    }

    private void checkPlaylistAlreadyExists(String playlistName) throws PlaylistAlreadyExistsException {
        if (playlists.containsKey(playlistName)) {
            throw new PlaylistAlreadyExistsException("Playlist with this name already exists.");
        }
    }

    private void checkPlaylistNotFound(String playlistName) throws PlaylistNotFoundException {
        if (!playlists.containsKey(playlistName)) {
            throw new PlaylistNotFoundException("Playlist with this name does not exist.");
        }
    }

    private void checkSongNotFound(String playlistName, Song song) throws SongNotFoundException {
        if (!playlists.get(playlistName).hasSong(song)) {
            throw new SongNotFoundException("Song not found in playlist.");
        }
    }

    private void checkSongAlreadyExists(String playlistName, Song song) throws SongAlreadyExistsException {
        if (playlists.get(playlistName).hasSong(song)) {
            throw new SongAlreadyExistsException("Song already in playlist.");
        }
    }

}
