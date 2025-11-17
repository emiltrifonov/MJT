package bg.sofia.uni.fmi.mjt.show.utils;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public final class FavoriteLocation {
    private final String favoriteLocation;
    private final boolean isNull;

    public FavoriteLocation (String favoriteLocation) {
        if (favoriteLocation == null) {
            this.favoriteLocation = null;
            isNull = true;
        }
        else {
            this.favoriteLocation = favoriteLocation.toLowerCase();
            isNull = false;
        }
    }

    public boolean isLocationOfDate(DateEvent dateEvent) {
        return !isNull && favoriteLocation.equals(dateEvent.getLocation().toLowerCase());
    }
}
