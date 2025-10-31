package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;
import bg.sofia.uni.fmi.mjt.burnout.util.UniversitySubjectsSortByRatingDescending;

import java.util.Arrays;

public final class ComputerScienceSemesterPlanner extends AbstractSemesterPlanner {
    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException {
        if (semesterPlan == null) {
            throw new IllegalArgumentException("Null semesterPlan in ComputerScienceSemesterPlanner constructor.");
        }
        else if(subjectRequirementsContainDuplicates(semesterPlan)) {
            throw new InvalidSubjectRequirementsException("Duplicates in semesterPlan in ComputerScienceSemesterPlanner constructor.");
        }

        UniversitySubject[] sortedByRatingDesc = UniversitySubjectsSortByRatingDescending.execute(semesterPlan.subjects());
        int[] remainingSubjectsPerCategory = new int[Category.count];
        for (SubjectRequirement sr : semesterPlan.subjectRequirements()) {
            remainingSubjectsPerCategory[sr.category().getIndex()] = sr.minAmountEnrolled();
        }

        int remainingCredits = semesterPlan.minimalAmountOfCredits();
        int numberOfSubjectsEnrolled = 0;
        for (int i = 0; i < sortedByRatingDesc.length && categoriesAreNotCovered(remainingSubjectsPerCategory) && remainingCredits > 0; i++) {
            remainingSubjectsPerCategory[sortedByRatingDesc[i].category().getIndex()]--;
            remainingCredits -= sortedByRatingDesc[i].credits();
            numberOfSubjectsEnrolled = i;
        }

        if (categoriesAreNotCovered(remainingSubjectsPerCategory) || remainingCredits > 0) {
            throw new CryToStudentsDepartmentException("CS student unable to cover semester credits.");
        }

        return Arrays.copyOf(sortedByRatingDesc, numberOfSubjectsEnrolled);
    }
}
