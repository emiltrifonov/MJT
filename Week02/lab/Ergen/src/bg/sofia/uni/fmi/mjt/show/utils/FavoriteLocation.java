package bg.sofia.uni.fmi.mjt.show.utils;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public final class FavoriteLocation {
    private final String favoriteLocation;

    public FavoriteLocation (String favoriteLocation) {
        this.favoriteLocation = favoriteLocation.toLowerCase();
    }

    public boolean isLocationOfDate(DateEvent dateEvent) {
        return favoriteLocation.equals(dateEvent.getLocation().toLowerCase());
    }
}
