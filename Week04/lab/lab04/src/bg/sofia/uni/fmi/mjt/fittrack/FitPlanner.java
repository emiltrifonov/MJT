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

public final class FitPlanner implements FitPlannerAPI {
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

        List<Workout> workoutsInPlan = new ArrayList<>(getOptimalWorkouts(totalMinutes));

        if (workoutsInPlan.isEmpty()) {
            throw new OptimalPlanImpossibleException("No possible workout plan.");
        }

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
            workoutsByType.putIfAbsent(workout.getType(), new ArrayList<>());
            workoutsByType.get(workout.getType()).add(workout);
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
     * @return Returns a Collection containing the optimal workouts
     */
    private Collection<Workout> getOptimalWorkouts(int totalMinutes) {
        boolean[][] isUsed = new boolean[availableWorkouts.size()][totalMinutes+1];
        int[] dp = new int[totalMinutes + 1];

        for (int i = 1; i <= availableWorkouts.size(); i++) {
            for (int j = totalMinutes; j >= availableWorkouts.get(i - 1).getDuration(); j--) {
                Workout workout = availableWorkouts.get(i - 1);

                int prev = dp[j - workout.getDuration()] + workout.getCaloriesBurned();

                if (prev > dp[j]) {
                    dp[j] = prev;
                    isUsed[i-1][j] = true;
                }
            }
        }

        return getUsedWorkouts(totalMinutes, isUsed);
    }

    private Collection<Workout> getUsedWorkouts(int remainingMinutes, boolean[][] isUsed) {
        Collection<Workout> usedWorkouts = new ArrayList<>();

        for (int i = availableWorkouts.size() - 1; i >= 0 && remainingMinutes > 0; i--) {
            if (isUsed[i][remainingMinutes]) {
                Workout workout = availableWorkouts.get(i);
                usedWorkouts.add(workout);
                remainingMinutes -= workout.getDuration();
            }
        }

        return usedWorkouts;
    }
}
