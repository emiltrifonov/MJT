package bg.sofia.uni.fmi.mjt.fittrack.comparator;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

import java.util.Comparator;

public class WorkoutByCaloriesComparator implements Comparator<Workout> {
    @Override
    public int compare(Workout o1, Workout o2) {
        return Integer.compare(o1.getCaloriesBurned(), o2.getCaloriesBurned());
    }
}
