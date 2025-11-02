package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class CaloriesWorkoutFilter implements WorkoutFilter {
    private final int minCalories;
    private final int maxCalories;

    public CaloriesWorkoutFilter(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Invalid arguments in CaloriesWorkoutFilter constructor.");
        }

        minCalories = min;
        maxCalories = max;
    }

    @Override
    public boolean matches(Workout workout) {
        return (minCalories <= workout.getCaloriesBurned() && workout.getCaloriesBurned() <= maxCalories);
    }
}
