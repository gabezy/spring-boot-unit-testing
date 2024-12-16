package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
class ReflectionTestUtilTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades studentGrades;

    @BeforeEach
    void setUpBeforeEach() {
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("eric.roby@luv2code_school.com");
        student.setStudentGrades(studentGrades);

        ReflectionTestUtils.setField(student, "id", 1);
        ReflectionTestUtils.setField(student, "studentGrades",
                new StudentGrades(List.of(100D, 85D, 76.50, 91.75))
        );
    }

    @Test
    void should_get_privateField() {
        assertEquals(1, ReflectionTestUtils.getField(student, "id"));
    }

    @Test
    void should_invoke_privateMethod() {
        String expected = student.getFirstname() + " " + student.getId();
        assertEquals(expected, ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"));
    }

}
