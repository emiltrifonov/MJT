package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public final class HumorousErgenka extends AbstractErgenka {
    private static final int SHORT_DATE_LENGTH = 30;
    private static final int SHORT_DATE_PENALTY = -2;
    private static final int NORMAL_DATE_MAX_LENGTH = 90;
    private static final int LONG_DATE_LENGTH = 120;
    private static final int LONG_DATE_PENALTY = -3;
    private static final int HUMOR_LEVEL_MULTIPLIER = 5;
    private static final int ROMANCE_LEVEL_DIVISOR = 3;
    private static final int NORMAL_DATE_REWARD = 4;

    public HumorousErgenka(String name, short age, int romanceLevel, int humorLevel, int rating) {
        super(name, age, romanceLevel, humorLevel, rating);
    }

    @Override
    protected void modifyRating(DateEvent dateEvent) {
        setRating(getRating() +
                ( (getHumorLevel() * HUMOR_LEVEL_MULTIPLIER) / (dateEvent.getTensionLevel()) )
                + Math.floorDiv(getRomanceLevel(), ROMANCE_LEVEL_DIVISOR)
                        + calculateBonuses(dateEvent));
    }

    @Override
    protected int calculateBonuses(DateEvent dateEvent) {
        return getDateLengthPenaltyOrBonus(dateEvent.getDuration());
    }

    @Override
    protected int getDateLengthPenaltyOrBonus(int dateLength) {
        if (dateLength < SHORT_DATE_LENGTH) {
            return SHORT_DATE_PENALTY;
        }  else if (dateLength <= NORMAL_DATE_MAX_LENGTH) {
            return NORMAL_DATE_REWARD;
        }  else if (dateLength > LONG_DATE_LENGTH) {
            return LONG_DATE_PENALTY;
        } else {
            return 0;
        }
    }
}
