package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;
import bg.sofia.uni.fmi.mjt.burnout.util.UniversitySubjectsSortByCreditsDescending;

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
        UniversitySubject[] sortedByCreditsDesc =
                UniversitySubjectsSortByCreditsDescending.execute(semesterPlan.subjects());

        int remainingCredits = semesterPlan.minimalAmountOfCredits();
        int[] remainingSubjectsPerCategory = new int[Category.count];
        boolean[] isSubjectTaken = new boolean[semesterPlan.subjects().length];
        for (SubjectRequirement sr : semesterPlan.subjectRequirements()) {
            remainingSubjectsPerCategory[sr.category().getIndex()] = sr.minAmountEnrolled();
        }

        int countOfChosenSubjects = 0;
        for (int i = 0; i < sortedByCreditsDesc.length && categoriesAreNotCovered(remainingSubjectsPerCategory); i++) {
            if (!isSubjectTaken[i]) {
                if (remainingSubjectsPerCategory[sortedByCreditsDesc[i].category().getIndex()] > 0) {
                    remainingSubjectsPerCategory[sortedByCreditsDesc[i].category().getIndex()]--;
                    remainingCredits -= sortedByCreditsDesc[i].credits();
                    isSubjectTaken[i] = true;
                    countOfChosenSubjects++;
                }
            }
        }

        if (categoriesAreNotCovered(remainingSubjectsPerCategory)) {
            return null;
        }

        for (int i = 0; i < sortedByCreditsDesc.length && remainingCredits > 0; i++) {
            if (!isSubjectTaken[i]) {
                isSubjectTaken[i] = true;
                countOfChosenSubjects++;
                remainingCredits -= sortedByCreditsDesc[i].credits();
            }
        }

        if (remainingCredits > 0) {
            return null;
        }

        UniversitySubject[] chosenSubjects = new UniversitySubject[countOfChosenSubjects];
        int index = 0;
        for (int i = 0; i < sortedByCreditsDesc.length; i++) {
            if (isSubjectTaken[i]) {
                chosenSubjects[index++] = sortedByCreditsDesc[i];
            }
        }

        return chosenSubjects;
    }
}
