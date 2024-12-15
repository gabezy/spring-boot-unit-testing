package com.luv2code.component;

import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationExampleTest {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.description}")
    private String description;

    @Value("${info.app.version}")
    private String version;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades studentGrades;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void beforeEach() {
        count++;
        System.out.printf("Testing: %s which is %s  Version: %s. Execution of test method %d",
                appName, description, version, count);

        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("eric.roby@luv2code_school.com");
        studentGrades.setMathGradeResults(List.of(100D, 85D, 76.50, 91.75));
        student.setStudentGrades(studentGrades);
    }

    @Test
    void should_add_results_for_studentGrades() {
        double expected = 353.25;
        double result = studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults());

        assertEquals(expected, result);
    }

    @Test
    void should_add_results_for_studentGradesNotEqual() {
        double expected = 0;
        double result = studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults());

        assertNotEquals(expected, result);
    }

    @Test
    void should_test_gradeOne_is_greater_than_gradeTwo() {
        double gradeOne = 90;
        double gradeTwo = 70;

        assertTrue(studentGrades.isGradeGreater(gradeOne, gradeTwo), "failure - should be true");
    }

    @Test
    void should_test_gradeTwo_isNot_greater_than_gradeOne() {
        double gradeOne = 90;
        double gradeTwo = 70;

        assertFalse(studentGrades.isGradeGreater(gradeTwo, gradeOne), "failure - should be false");
    }

    @Test
    void should_check_null_for_studentGrades() {
        double result = studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults());

        assertNotNull(studentGrades.checkNull(result));
    }

    @Test
    void should_create_student_without_grades() {
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        studentTwo.setFirstname("Chad");
        studentTwo.setLastname("Darby");
        studentTwo.setEmailAddress("chad.darby@luv2code_school.com");

        assertNotNull(studentTwo.getFirstname());
        assertNotNull(studentTwo.getLastname());
        assertNotNull(studentTwo.getEmailAddress());

        assertNull(studentTwo.getStudentGrades());
    }

    @Test
    void should_verify_students_are_prototypes() {
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        assertNotSame(student, studentTwo);
    }

    @Test
    void should_find_grade_pointAverage() {
        List<Double> mathGrades = student.getStudentGrades().getMathGradeResults();
        assertAll("Testing all assertEquals",
                () -> assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(mathGrades)),
                () -> assertEquals(88.31, studentGrades.findGradePointAverage(mathGrades))
        );
    }


}
