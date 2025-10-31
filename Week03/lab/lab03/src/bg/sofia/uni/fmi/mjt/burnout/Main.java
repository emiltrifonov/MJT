package bg.sofia.uni.fmi.mjt.burnout;

import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.semester.SoftwareEngineeringSemesterPlanner;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

import java.util.Arrays;


public class Main {

    public static void main(String... args) throws InvalidSubjectRequirementsException {
        SoftwareEngineeringSemesterPlanner planner = new SoftwareEngineeringSemesterPlanner();

        UniversitySubject[] subjects = {
                new UniversitySubject("Calculus", 6, 5, Category.MATH, 40),
                new UniversitySubject("Java Programming", 4, 4, Category.PROGRAMMING, 30),
                new UniversitySubject("Linear Algebra", 5, 3, Category.MATH, 35),
                new UniversitySubject("Data Structures", 3, 5, Category.PROGRAMMING, 25)
        };

        SubjectRequirement[] requirements = {
                new SubjectRequirement(Category.MATH, 1),
                new SubjectRequirement(Category.PROGRAMMING, 1)
        };

        SemesterPlan plan1 = new SemesterPlan(subjects, requirements, 5);

        printSubjects(planner.calculateSubjectList(plan1));
        //result1 = ["Calculus", "Java Programming"]

        SemesterPlan plan2 = new SemesterPlan(subjects, requirements, 10);

        //result2 = ["Calculus", "Java Programming"]
        printSubjects(planner.calculateSubjectList(plan2));

        SemesterPlan plan3 = new SemesterPlan(subjects, requirements, 15);

        //result3 = ["Calculus", "Linear Algebra", "Java Programming"]
        printSubjects(planner.calculateSubjectList(plan3));

        UniversitySubject[] selectedSubjects = planner.calculateSubjectList(plan1);

        int jarCount = planner.calculateJarCount(selectedSubjects, 11, 50);
        System.out.println("Jar count: " + jarCount);
        //jarCount = 28
    }

    private static void printSubjects(UniversitySubject[] subjects) {
        for (UniversitySubject us : subjects) {
            System.out.print(us.name() + " ");
        }
        System.out.println();
    }

}
