package bg.sofia.uni.fmi.mjt.fittrack.workout;

import bg.sofia.uni.fmi.mjt.fittrack.exception.InvalidWorkoutException;

import java.util.Objects;

public abstract class AbstractWorkout {
    private static final int MIN_DIFF = 1;
    private static final int MAX_DIFF = 5;

    private final String name;
    private final int duration;
    private final int caloriesBurned;
    private final int difficulty;

    public AbstractWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        if (name == null) {
            throw new InvalidWorkoutException("Null name in workout constructor.");
        } else if (name.isBlank()) {
            throw new InvalidWorkoutException("Blank name in workout constructor.");
        } else if (duration <= 0) {
            throw new InvalidWorkoutException("Non-positive duration in workout constructor.");
        } else if (caloriesBurned <= 0) {
            throw new InvalidWorkoutException("Non-positive caloriesBurned in workout constructor.");
        } else if (difficulty < MIN_DIFF || difficulty > MAX_DIFF) {
            throw new InvalidWorkoutException("Difficulty outside expected range in workout constructor.");
        }

        this.name = name;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public abstract WorkoutType getType();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractWorkout that)) return false;
        return getType() == that.getType() && getDuration() == that.getDuration() &&
                getCaloriesBurned() == that.getCaloriesBurned() && getDifficulty() == that.getDifficulty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getDuration(), getCaloriesBurned(), getDifficulty());
    }
}
