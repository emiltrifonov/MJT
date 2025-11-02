package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;
import bg.sofia.uni.fmi.mjt.burnout.util.SortUniversitySubjects;
import bg.sofia.uni.fmi.mjt.burnout.util.SortUniversitySubjectsByRatingDescending;

import java.util.Arrays;

public final class ComputerScienceSemesterPlanner extends AbstractSemesterPlanner {
    private static final SortUniversitySubjects sort;

    static {
        sort = new SortUniversitySubjectsByRatingDescending();
    }

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException {
        if (semesterPlan == null) {
            throw new IllegalArgumentException("Null semesterPlan in ComputerScienceSemesterPlanner constructor.");
        }
        else if(subjectRequirementsContainDuplicates(semesterPlan)) {
            throw new InvalidSubjectRequirementsException("Duplicates in semesterPlan in ComputerScienceSemesterPlanner constructor.");
        }

        UniversitySubject[] subjectsSortedByRatingDesc = sort.execute(semesterPlan.subjects());
        int numberOfSubjectsEnrolled = getNumberOfSubjectsEnrolled(semesterPlan, subjectsSortedByRatingDesc);

        return Arrays.copyOf(subjectsSortedByRatingDesc, numberOfSubjectsEnrolled);
    }

    private int getNumberOfSubjectsEnrolled(SemesterPlan semesterPlan, UniversitySubject[] subjectsSortedByRatingDesc) {
        int[] remainingSubjectsPerCategory = createRemainingSubjectPerCategoryArray(semesterPlan);

        int remainingCredits = semesterPlan.minimalAmountOfCredits();
        int numberOfSubjectsEnrolled = 0;
        for (int i = 0; i < subjectsSortedByRatingDesc.length && categoriesAreNotCovered(remainingSubjectsPerCategory) && remainingCredits > 0; i++) {
            remainingSubjectsPerCategory[subjectsSortedByRatingDesc[i].category().ordinal()]--;
            remainingCredits -= subjectsSortedByRatingDesc[i].credits();
            numberOfSubjectsEnrolled = i;
        }

        if (categoriesAreNotCovered(remainingSubjectsPerCategory) || remainingCredits > 0) {
            throw new CryToStudentsDepartmentException("CS student unable to cover semester credits.");
        }

        return numberOfSubjectsEnrolled;
    }
}
