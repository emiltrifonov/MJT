package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public final class HumorousErgenka extends AbstractErgenka {
    private static final int SHORT_DATE_LENGTH = 30;
    private final int SHORT_DATE_PENALTY = -2;
    private static final int LONG_DATE_LENGTH = 90;
    private static final int LONG_DATE_PENALTY = -3;

    private static final int NORMAL_DATE_REWARD = 4;

    public HumorousErgenka(String name, short age, int romanceLevel, int humorLevel, int rating) {
        super(name, age, romanceLevel, humorLevel, rating);
    }

    @Override
    protected int calculateBonuses(DateEvent dateEvent) {
        int penalty = getDateLengthPenalty(dateEvent.getDuration(), SHORT_DATE_LENGTH, LONG_DATE_LENGTH,
                                        SHORT_DATE_PENALTY, LONG_DATE_PENALTY);

        return penalty == 0 ? NORMAL_DATE_REWARD : penalty;
    }

    @Override
    protected void modifyRating(DateEvent dateEvent) {
        setRating(((getHumorLevel() * 5) / (dateEvent.getTensionLevel()))
                + (getRomanceLevel() / 3) + calculateBonuses(dateEvent));
    }
}
