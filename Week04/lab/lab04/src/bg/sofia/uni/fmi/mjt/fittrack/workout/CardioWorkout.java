package bg.sofia.uni.fmi.mjt.fittrack.workout;

public final class CardioWorkout extends AbstractWorkout implements Workout {
    private static final WorkoutType TYPE = WorkoutType.CARDIO;

    public CardioWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public int getDuration() {
        return super.getDuration();
    }

    @Override
    public int getCaloriesBurned() {
        return super.getCaloriesBurned();
    }

    @Override
    public int getDifficulty() {
        return super.getDifficulty();
    }

    @Override
    public WorkoutType getType() {
        return TYPE;
    }
}
