package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class DurationWorkoutFilter implements WorkoutFilter {
    private final int minDuration;
    private final int maxDuration;

    public DurationWorkoutFilter(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Invalid arguments in DurationWorkoutFilter constructor.");
        }

        minDuration = min;
        maxDuration = max;
    }

    @Override
    public boolean matches(Workout workout) {
        return (minDuration <= workout.getDuration() && workout.getDuration() <= maxDuration);
    }
}
