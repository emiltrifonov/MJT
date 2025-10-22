package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.utils.FavoriteLocation;

public final class RomanticErgenka extends AbstractErgenka {
    static final int SHORT_DATE_LENGTH = 30;
    static final int SHORT_DATE_PENALTY = -3;
    static final int LONG_DATE_LENGTH = 120;
    static final int LONG_DATE_PENALTY = -2;

    static final int FAVORITE_PLACE_REWARD = 5;

    private final FavoriteLocation favoriteLocation;

    public RomanticErgenka(String name, short age, int romanceLevel, int humorLevel, int rating, String favoriteDateLocation) {
        favoriteLocation = new FavoriteLocation(favoriteDateLocation);
        super(name, age, romanceLevel, humorLevel, rating);
    }

    @Override
    protected int calculateBonuses(DateEvent dateEvent) {
        int penalty = getDateLengthPenalty(dateEvent.getDuration(), SHORT_DATE_LENGTH, LONG_DATE_LENGTH,
                                            SHORT_DATE_PENALTY, LONG_DATE_PENALTY);

        if (favoriteLocation.isLocationOfDate(dateEvent)) {
            return penalty + FAVORITE_PLACE_REWARD;
        }
        else {
            return penalty;
        }
    }

    @Override
    protected void modifyRating(DateEvent dateEvent) {
        setRating(((getRomanceLevel() * 7) / dateEvent.getTensionLevel())
                + (getHumorLevel() / 3) + calculateBonuses(dateEvent));
    }
}