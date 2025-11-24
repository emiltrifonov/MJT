package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class NameWorkoutFilter implements WorkoutFilter {
    private final String keyword;
    private final boolean caseSensitive;

    public NameWorkoutFilter(String keyword, boolean caseSensitive) {
        if (keyword == null) {
            throw new IllegalArgumentException("Null keyword in NameWorkoutFilter constructor.");
        } else if (keyword.isBlank()) {
            throw new IllegalArgumentException("Blank keyword in NameWorkoutFilter constructor.");
        }

        if (caseSensitive) {
            this.keyword = String.copyValueOf(keyword.toCharArray());
        } else {
            this.keyword = String.copyValueOf(keyword.toCharArray()).toLowerCase();
        }
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(Workout workout) {
        return caseSensitive ? matchesCaseSensitive(workout) : matchesCaseInsensitive(workout);
    }

    private boolean matchesCaseSensitive(Workout workout) {
        return workout.getName().contains(keyword);
    }

    private boolean matchesCaseInsensitive(Workout workout) {
        return workout.getName().toLowerCase().contains(keyword);
    }
}
