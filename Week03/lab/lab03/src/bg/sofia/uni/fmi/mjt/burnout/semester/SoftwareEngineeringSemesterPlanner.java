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

        UniversitySubject[] selectedSubjects = chooseOptimalSubjects(semesterPlan);

        if (selectedSubjects == null) {
            throw new CryToStudentsDepartmentException("SE student unable to cover semester credits.");
        }

        return selectedSubjects;
    }

    private UniversitySubject[] chooseOptimalSubjects(SemesterPlan semesterPlan) {
        UniversitySubject[] subjectsSortedByCreditsDesc = SortUniversitySubjectsByCreditsDescending.execute(semesterPlan.subjects());

        int remainingCredits = semesterPlan.minimalAmountOfCredits();
        int[] remainingSubjectsPerCategory = createRemainingSubjectPerCategoryArray(semesterPlan);
        boolean[] isSubjectTaken = new boolean[semesterPlan.subjects().length];

        int countOfChosenSubjects = 0;
        for (int i = 0; i < subjectsSortedByCreditsDesc.length && categoriesAreNotCovered(remainingSubjectsPerCategory); i++) {
            if (!isSubjectTaken[i]) {
                if (remainingSubjectsPerCategory[subjectsSortedByCreditsDesc[i].category().ordinal()] > 0) {
                    remainingSubjectsPerCategory[subjectsSortedByCreditsDesc[i].category().ordinal()]--;
                    remainingCredits -= subjectsSortedByCreditsDesc[i].credits();
                    isSubjectTaken[i] = true;
                    countOfChosenSubjects++;
                }
            }
        }

        if (categoriesAreNotCovered(remainingSubjectsPerCategory)) {
            return null;
        }

        for (int i = 0; i < subjectsSortedByCreditsDesc.length && remainingCredits > 0; i++) {
            if (!isSubjectTaken[i]) {
                isSubjectTaken[i] = true;
                countOfChosenSubjects++;
                remainingCredits -= subjectsSortedByCreditsDesc[i].credits();
            }
        }

        if (remainingCredits > 0) {
            return null;
        }

        UniversitySubject[] chosenSubjects = new UniversitySubject[countOfChosenSubjects];
        int index = 0;
        for (int i = 0; i < subjectsSortedByCreditsDesc.length; i++) {
            if (isSubjectTaken[i]) {
                chosenSubjects[index++] = subjectsSortedByCreditsDesc[i];
            }
        }

        return chosenSubjects;
    }
}
