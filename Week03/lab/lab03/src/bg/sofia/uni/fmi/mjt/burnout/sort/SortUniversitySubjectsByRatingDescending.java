package bg.sofia.uni.fmi.mjt.burnout.sort;

import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

import java.util.Arrays;

public final class SortUniversitySubjectsByRatingDescending implements SortUniversitySubjects{
        @Override
        public UniversitySubject[] execute(UniversitySubject[] subjects) {
        UniversitySubject[] sorted = Arrays.copyOf(subjects, subjects.length);

        for (int i = 0; i < subjects.length; i++) {
            int maxRatingIndex = i;

            for (int j = i + 1; j < subjects.length; j++) {
                if (sorted[j].rating() > sorted[maxRatingIndex].rating()) {
                    maxRatingIndex = j;
                }
            }

            if (maxRatingIndex != i) {
                UniversitySubject temp = sorted[maxRatingIndex];
                sorted[maxRatingIndex] = sorted[i];
                sorted[i] = temp;
            }
        }

        return sorted;
    }
}
