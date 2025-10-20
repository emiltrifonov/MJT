package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public abstract class AbstractErgenka implements Ergenka {
    private final String name;
    private final short age;
    private final int romanceLevel;
    private final int humorLevel;
    private int rating;

    public AbstractErgenka (String name, short age, int romanceLevel, int humorLevel, int rating) {
        this.name = name;
        this.age = age;
        this.romanceLevel = romanceLevel;
        this.humorLevel = humorLevel;
        this.rating = rating;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public short getAge() {
        return age;
    }

    @Override
    public int getRomanceLevel() {
        return romanceLevel;
    }

    @Override
    public int getHumorLevel() {
        return humorLevel;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public void reactToDate(DateEvent dateEvent) {
        modifyRating(dateEvent);
    }

    protected abstract int calculateBonuses(DateEvent dateEvent);
    protected abstract void modifyRating(DateEvent dateEvent);

    protected void setRating(int rating) {
        this.rating = rating;
    }

    protected int getDateLengthPenalty(int dateLength,
                                       int shortDateLength, int longDateLength,
                                       int shortDatePenalty, int longDatePenalty) {
        if (dateLength < shortDateLength) {
            return shortDatePenalty;
        }
        else if (dateLength > longDateLength) {
            return longDatePenalty;
        }
        else {
            // Date is neither too short nor too long
            return 0;
        }
    }
}
