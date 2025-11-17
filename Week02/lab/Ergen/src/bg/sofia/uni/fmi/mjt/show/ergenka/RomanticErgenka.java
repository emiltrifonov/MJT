package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.utils.FavoriteLocation;

public final class RomanticErgenka extends AbstractErgenka {
    private static final int SHORT_DATE_LENGTH = 30;
    private static final int SHORT_DATE_PENALTY = -3;
    private static final int LONG_DATE_LENGTH = 120;
    private static final int LONG_DATE_PENALTY = -2;

    private static final int FAVORITE_PLACE_REWARD = 5;

    private final FavoriteLocation favoriteLocation;

    public RomanticErgenka(String name, short age, int romanceLevel, int humorLevel, int rating, String favoriteDateLocation) {
        // class FavoriteLocation handles null comparison logic
        favoriteLocation = new FavoriteLocation(favoriteDateLocation);
        super(name, age, romanceLevel, humorLevel, rating);
    }

    @Override
    protected void modifyRating(DateEvent dateEvent) {
        setRating(getRating() +
                ( (getRomanceLevel() * 7) / dateEvent.getTensionLevel() )
                + Math.floorDiv(getHumorLevel(), 3)
                        + calculateBonuses(dateEvent));
    }

    @Override
    protected int calculateBonuses(DateEvent dateEvent) {
        int penalty = getDateLengthPenaltyOrBonus(dateEvent.getDuration());

        if (favoriteLocation.isLocationOfDate(dateEvent)) {
            return penalty + FAVORITE_PLACE_REWARD;
        }
        else {
            return penalty;
        }
    }

    @Override
    protected int getDateLengthPenaltyOrBonus(int dateLength) {
        if (dateLength < SHORT_DATE_LENGTH) {
            return SHORT_DATE_PENALTY;
        }
        else if (dateLength > LONG_DATE_LENGTH) {
            return LONG_DATE_PENALTY;
        }
        else {
            return 0;
        }
    }
}