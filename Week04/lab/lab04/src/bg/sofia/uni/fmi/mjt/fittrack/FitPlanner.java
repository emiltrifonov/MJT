package bg.sofia.uni.fmi.mjt.fittrack;

import bg.sofia.uni.fmi.mjt.fittrack.comparator.WorkoutByCaloriesAndDifficultyDescComparator;
import bg.sofia.uni.fmi.mjt.fittrack.comparator.WorkoutByCaloriesComparator;
import bg.sofia.uni.fmi.mjt.fittrack.comparator.WorkoutByDifficultyComparator;
import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;
import bg.sofia.uni.fmi.mjt.fittrack.workout.filter.WorkoutFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FitPlanner implements FitPlannerAPI {
    private final ArrayList<Workout> availableWorkouts;
    private final boolean returnEmptyWorkouts;

    public FitPlanner(Collection<Workout> availableWorkouts) {
        if (availableWorkouts == null) {
            throw new IllegalArgumentException("Null availableWorkouts in FitPlanner constructor.");
        }

        returnEmptyWorkouts = availableWorkouts.isEmpty();

        this.availableWorkouts = new ArrayList<>(availableWorkouts);
    }

    @Override
    public List<Workout> findWorkoutsByFilters(List<WorkoutFilter> filters) {
        if (returnEmptyWorkouts) {
            return List.of();
        }

        List<Workout> matching = new ArrayList<>();

        for (Workout availableWorkout : availableWorkouts) {
            if (matchesAllFilters(availableWorkout, filters)) {
                matching.add(availableWorkout);
            }
        }

        return matching;
    }

    @Override
    public List<Workout> generateOptimalWeeklyPlan(int totalMinutes) throws OptimalPlanImpossibleException {
        if (totalMinutes < 0) {
            throw new IllegalArgumentException("Negative totalMinutes in generateOptimalWeeklyPlan method.");
        }
        else if (totalMinutes == 0) {
            return List.of();
        }

        int optimalCaloriesBurned = getMostCaloriesBurned(totalMinutes);

        if (optimalCaloriesBurned == 0) {
            throw new OptimalPlanImpossibleException("No possible workout plan.");
        }

        List<Workout> workoutsInPlan = gatherPlanWithOptimalCalories(optimalCaloriesBurned);
        workoutsInPlan.sort(new WorkoutByCaloriesAndDifficultyDescComparator());

        return workoutsInPlan;
    }

    @Override
    public Map<WorkoutType, List<Workout>> getWorkoutsGroupedByType() {
        if (returnEmptyWorkouts) {
            return Map.of();
        }

        Map<WorkoutType, List<Workout>> workoutsByType = new EnumMap<>(WorkoutType.class);

        for (Workout workout : availableWorkouts) {
            workoutsByType.putIfAbsent(workout.getType(), new ArrayList<Workout>());
            List<Workout> workouts = workoutsByType.get(workout.getType());
            workouts.add(workout);
        }

        return workoutsByType;
    }

    @Override
    public List<Workout> getWorkoutsSortedByCalories() {
        if (returnEmptyWorkouts) {
            return List.of();
        }

        List<Workout> sortedByCalories = new ArrayList<>(availableWorkouts);
        sortedByCalories.sort(new WorkoutByCaloriesComparator());

        return sortedByCalories;
    }

    @Override
    public List<Workout> getWorkoutsSortedByDifficulty() {
        if (returnEmptyWorkouts) {
            return List.of();
        }

        List<Workout> sortedByDifficulty = new ArrayList<>(availableWorkouts);
        sortedByDifficulty.sort(new WorkoutByDifficultyComparator());

        return sortedByDifficulty;
    }

    @Override
    public Set<Workout> getUnmodifiableWorkoutSet() {
        return Set.copyOf(availableWorkouts);
    }

    private boolean matchesAllFilters(Workout workout, List<WorkoutFilter> filters) {
        for (WorkoutFilter filter : filters) {
            if (!filter.matches(workout)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Knapsack Algorithm
     * @return Returns the optimal calories burned in totalMinutes
     */
    private int getMostCaloriesBurned(int totalMinutes) {
        // Initializing dp array
        int[] dp = new int[totalMinutes + 1];

        // Taking first i elements
        for (int i = 1; i <= availableWorkouts.size(); i++) {

            // Starting from back, so that we also have data of
            // previous computation of i-1 items
            for (int j = totalMinutes; j >= availableWorkouts.get(i - 1).getDuration(); j--) {
                dp[j] = Math.max(dp[j], dp[j - availableWorkouts.get(i - 1).getDuration()]
                        + availableWorkouts.get(i - 1).getCaloriesBurned());
            }
        }

        return dp[totalMinutes];
    }

    private List<Workout> gatherPlanWithOptimalCalories(int optimalCalories) {

        return new ArrayList<>();
    }
}
