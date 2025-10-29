package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

import java.lang.ref.Reference;

public sealed abstract class AbstractSemesterPlanner implements SemesterPlannerAPI permits ComputerScienceSemesterPlanner, SoftwareEngineeringSemesterPlanner {
    private static final int DAYS_PER_JAR = 5;

    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) {
        if (maximumSlackTime <= 0) {
            throw new IllegalArgumentException("Non-positive maximumSlackTime in calculateJarCount function.");
        }
        else if (semesterDuration <= 0) {
            throw new IllegalArgumentException("Non-positive semesterDuration in calculateJarCount function.");
        }
        validateSubjects(subjects);

        int totalStudyTime = 0;
        int totalRestTime = 0;

        for (UniversitySubject us : subjects) {
            totalStudyTime += us.neededStudyTime();
            totalRestTime += (int)Math.ceil(us.neededStudyTime() * us.category().getCoefficient());
        }

        if (totalRestTime > maximumSlackTime) {
            throw new DisappointmentException("Ne na baba tiq!");
        }

        int jarsCount = totalStudyTime / DAYS_PER_JAR;

        if (totalStudyTime + totalRestTime > semesterDuration) {
            jarsCount *= 2;
        }

        return jarsCount;
    }

    protected boolean subjectRequirementsContainDuplicates(SemesterPlan semesterPlan) {
        boolean[] isMet = new boolean[Category.count];

        for (SubjectRequirement sr : semesterPlan.subjectRequirements()) {
            int index = sr.category().getIndex();
            if (isMet[index]) {
                return true;
            }
            else {
                isMet[index] = true;
            }
        }

        return false;
    }

    protected boolean categoriesAreNotCovered(int[] remainingSubjectsPerCategory) {
        for (int count : remainingSubjectsPerCategory) {
            if (count != 0) {
                return true;
            }
        }

        return false;
    }

    private void validateSubjects(UniversitySubject[] subjects) {
        if (subjects == null) {
            throw new IllegalArgumentException("Null subjects in calculateJarCount function.");
        }

        for (UniversitySubject us : subjects) {
            if (us == null) {
                throw new IllegalArgumentException("Null subject in subjects array in calculateJarCount function.");
            }
        }
    }
}
