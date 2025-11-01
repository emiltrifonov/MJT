package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;
import bg.sofia.uni.fmi.mjt.burnout.util.SortUniversitySubjectsByCreditsDescending;

public final class SoftwareEngineeringSemesterPlanner extends AbstractSemesterPlanner {
    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException {
        if (semesterPlan == null) {
            throw new IllegalArgumentException("Null semesterPlan in SoftwareEngineeringSemesterPlanner constructor.");
        }
        else if (subjectRequirementsContainDuplicates(semesterPlan)) {
            throw new InvalidSubjectRequirementsException("Duplicates in semesterPlan in SoftwareEngineeringSemesterPlanner constructor.");
        }

        UniversitySubject[] subjectsSortedByCreditsDesc = SortUniversitySubjectsByCreditsDescending.execute(semesterPlan.subjects());
        int remainingCredits = semesterPlan.minimalAmountOfCredits();
        int[] remainingSubjectsPerCategory = createRemainingSubjectPerCategoryArray(semesterPlan);
        boolean[] isSubjectTaken = new boolean[semesterPlan.subjects().length];
        int countOfChosenSubjects = attemptToCoverCategories(subjectsSortedByCreditsDesc, remainingSubjectsPerCategory, isSubjectTaken);

        remainingCredits -= getCreditsAcquiredWithBatchOfSubjects(subjectsSortedByCreditsDesc, isSubjectTaken);

        countOfChosenSubjects += attemptToCoverRemainingCredits(subjectsSortedByCreditsDesc, remainingCredits, isSubjectTaken);

        return getFinalSubjects(subjectsSortedByCreditsDesc, countOfChosenSubjects, isSubjectTaken);
    }

    /**
     * @return Count of covered subjects
     */
    private int attemptToCoverCategories(UniversitySubject[] subjectsSortedByCreditsDesc, int[] remainingSubjectsPerCategory, boolean[] isSubjectTaken) {
        int countOfChosenSubjects = 0;
        for (int i = 0; i < subjectsSortedByCreditsDesc.length && categoriesAreNotCovered(remainingSubjectsPerCategory); i++) {
            if (!isSubjectTaken[i]) {
                if (remainingSubjectsPerCategory[subjectsSortedByCreditsDesc[i].category().ordinal()] > 0) {
                    remainingSubjectsPerCategory[subjectsSortedByCreditsDesc[i].category().ordinal()]--;
                    isSubjectTaken[i] = true;
                    countOfChosenSubjects++;
                }
            }
        }

        if (categoriesAreNotCovered(remainingSubjectsPerCategory)) {
            throw new CryToStudentsDepartmentException("SE student unable to cover all categories of subjects.");
        }

        return countOfChosenSubjects;
    }

    /**
     * @return Count of covered subjects
     */
    private int attemptToCoverRemainingCredits(UniversitySubject[] subjectsSortedByCreditsDesc, int remainingCredits, boolean[] isSubjectTaken) {
        int countOfChosenSubjects = 0;
        for (int i = 0; i < subjectsSortedByCreditsDesc.length && remainingCredits > 0; i++) {
            if (!isSubjectTaken[i]) {
                isSubjectTaken[i] = true;
                countOfChosenSubjects++;
                remainingCredits -= subjectsSortedByCreditsDesc[i].credits();
            }
        }

        if (remainingCredits > 0) {
            throw new CryToStudentsDepartmentException("SE student unable to cover semester credits.");
        }

        return countOfChosenSubjects;
    }

    private int getCreditsAcquiredWithBatchOfSubjects(UniversitySubject[] subjects, boolean[] isTaken) {
        int totalCredits = 0;
        for (int i = 0; i < subjects.length; i++) {
            if (isTaken[i]) {
                totalCredits += subjects[i].credits();
            }
        }

        return totalCredits;
    }

    private UniversitySubject[] getFinalSubjects(UniversitySubject[] subjectsSortedByCreditsDesc, int count, boolean[] isTaken) {
        UniversitySubject[] chosenSubjects = new UniversitySubject[count];
        int index = 0;
        for (int i = 0; i < subjectsSortedByCreditsDesc.length; i++) {
            if (isTaken[i]) {
                chosenSubjects[index++] = subjectsSortedByCreditsDesc[i];
            }
        }

        return chosenSubjects;
    }
}
