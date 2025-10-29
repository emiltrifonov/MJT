package bg.sofia.uni.fmi.mjt.burnout.util;

import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

import java.util.Arrays;

public final class UniversitySubjectsSortByCreditsDescending {
    public static UniversitySubject[] execute(UniversitySubject[] subjects) {
        UniversitySubject[] sorted = Arrays.copyOf(subjects, subjects.length);

        for (int i = 0; i < subjects.length; i++) {
            int maxCreditIndex = i;

            for (int j = i + 1; j < subjects.length; j++) {
                if (sorted[j].credits() > sorted[maxCreditIndex].credits()) {
                    maxCreditIndex = j;
                }
            }

            if (maxCreditIndex != i) {
                UniversitySubject temp = sorted[maxCreditIndex];
                sorted[maxCreditIndex] = sorted[i];
                sorted[i] = temp;
            }
        }

        return sorted;
    }
}
