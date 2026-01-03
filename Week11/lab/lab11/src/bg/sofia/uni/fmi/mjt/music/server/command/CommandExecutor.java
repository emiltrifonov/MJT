package bg.sofia.uni.fmi.mjt.music.server.command;

import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongAlreadyExistsException;

public class CommandExecutor {

    private static final String CREATE = "create-playlist";
    private static final String ADD = "add-song";
    private static final String LIKE = "like-song";
    private static final String UNLIKE = "unlike-song";
    private static final String LIST = "list-playlists";
    private static final String GET = "get-playlist";
    private static final String DISCONNECT = "disconnect";

    private static final String CREATE_ARGS = "<playlist_name>";
    private static final int CREATE_ARGS_COUNT = 1;
    private static final String ADD_ARGS = "<playlist_name> <song_title> <artist_name> <duration>";
    private static final int ADD_ARGS_COUNT = 4;
    private static final String LIKE_ARGS = "<playlist_name> <song_title> <artist_name>";
    private static final int LIKE_ARGS_COUNT = 3;
    private static final String UNLIKE_ARGS = "<playlist_name> <song_title> <artist_name>";
    private static final int UNLIKE_ARGS_COUNT = 3;
    private static final String LIST_ARGS = "";
    private static final int LIST_ARGS_COUNT = 0;
    private static final String GET_ARGS = "<playlist_name>";
    private static final int GET_ARGS_COUNT = 1;
    private static final String DISCONNECT_ARGS = "";
    private static final int DISCONNECT_ARGS_COUNT = 0;

    private final PlaylistRepository playlistRepository;

    public CommandExecutor(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public String execute(Command command) {
        return switch (command.command()) {
            case CREATE -> create(command.args());
            case ADD -> add(command.args());
            case LIKE -> like(command.args());
            case UNLIKE -> unlike(command.args());
            case LIST -> list(command.args());
            case GET -> get(command.args());
            case DISCONNECT -> disconnect(command.args());
            default -> "{\"status\":\"ERROR\",\"message\":\"Unknown command\"}";
        };
    }

    private String create(String[] args) {
        if (args.length != CREATE_ARGS_COUNT) {
            return fail(CREATE, CREATE_ARGS);
        }

        try {
            playlistRepository.createPlaylist(args[0]);
        } catch (PlaylistAlreadyExistsException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Playlist already exists.\"}";
        }

        return "{\"status\":\"OK\",\"message\":\"Playlist" + args[0] + "created successfully\"}";
    }

    private String add(String[] args) {
        if (args.length != ADD_ARGS_COUNT) {
            return fail(ADD, ADD_ARGS);
        }

        final int playlistIndex = 0;
        final int titleIndex = 1;
        final int artistIndex = 2;
        final int durationIndex = 3;
        final String playlist = args[playlistIndex];
        final String title = args[titleIndex];
        final String artist = args[artistIndex];
        final int duration = Integer.parseInt(args[durationIndex]);

        try {
            playlistRepository.addSong(playlist, title, artist, duration);
        } catch (PlaylistNotFoundException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Playlist not found\"}";
        } catch (SongAlreadyExistsException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Song " + title +
                    " by " + artist + " already exists in " + playlist + "\"}";
        }

        return "{\"status\":\"OK\",\"message\":\"Song " + title + " by " + artist + " added successfully\"}";
    }

    private String like(String[] args) {
        if (args.length != LIKE_ARGS_COUNT) {
            return fail(LIKE, LIKE_ARGS);
        }
    }

    private String unlike(String[] args) {
        if (args.length != UNLIKE_ARGS_COUNT) {
            return fail(UNLIKE, UNLIKE_ARGS);
        }
    }

    private String list(String[] args) {
        if (args.length != LIST_ARGS_COUNT) {
            return fail(LIST, LIST_ARGS);
        }

        String playlists = playlistRepository.getAllPlaylists().toString();

        return "{\"status\":\"OK\",\"message\":\"\"playlists\":" + playlists + "\"}";
    }

    private String get(String[] args) {
        if (args.length != GET_ARGS_COUNT) {
            return fail(GET, GET_ARGS);
        }

        return "";
    }

    private String disconnect(String[] args) {
        if (args.length != DISCONNECT_ARGS_COUNT) {
            return fail(DISCONNECT, DISCONNECT_ARGS);
        }

        return "disconnect";
    }

    private String fail(String command, String args) {
        return "{\"status\":\"ERROR\",\"message\":\"Usage: " + command + " " + args + "\"}";
    }

}
