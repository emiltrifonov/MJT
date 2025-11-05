package bg.sofia.uni.fmi.mjt.fittrack.workout;

public final class YogaSession extends AbstractWorkout implements Workout {
    private static final WorkoutType type = WorkoutType.YOGA;

    public YogaSession(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    public YogaSession(YogaSession other) {
        super(other);
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
        return type;
    }
}
