package bg.sofia.uni.fmi.mjt.fittrack.workout;

public final class StrengthWorkout extends AbstractWorkout implements Workout {
    private static final WorkoutType type = WorkoutType.STRENGTH;

    public StrengthWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    public StrengthWorkout(StrengthWorkout other) {
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
