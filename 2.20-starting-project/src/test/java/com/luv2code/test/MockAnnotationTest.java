package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
class MockAnnotationTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CollegeStudent studentOne;

    @Autowired
    private StudentGrades studentGrades;

    //@Mock
    @MockBean
    private ApplicationDao applicationDao;

    //@InjectMocks
    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    void beforeEach() {
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Roby");
        studentOne.setEmailAddress("eric.roby@luv2code_school.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    void should_add_grades() {
        when(applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()))
                .thenReturn(100D);

        assertEquals(100, applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()));

        verify(applicationDao, times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @DisplayName("Find GPA")
    @Test
    void should_find_gpa() {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);

        assertEquals(88.31, applicationService.findGradePointAverage(
                studentOne.getStudentGrades().getMathGradeResults()));

        verify(applicationDao, times(1)).findGradePointAverage(studentGrades.getMathGradeResults());
    }

    @DisplayName("Not null")
    @Test
    void should_check_notNull() {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);
        when(applicationDao.checkNull(studentOne.getFirstname()))
                .thenReturn(true);

        assertNotNull(applicationService.checkNull(
                studentOne.getStudentGrades().getMathGradeResults()), "Object should not be null");

        assertNotNull(applicationService.checkNull(studentOne.getFirstname()));
    }

    @DisplayName("Throw runtime exception")
    @Test
    void should_throw_runtimeExpection() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException())
                .when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class, () -> applicationService.checkNull(nullStudent));

        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @DisplayName("Multiple Stubbing")
    @Test
    void should_test_multiple_stubbing() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        String expected = "Do not throw exception second time";

        when(applicationDao.checkNull(nullStudent))
                .thenThrow(RuntimeException.class)
                .thenReturn(expected);

        assertThrows(RuntimeException.class, () -> applicationService.checkNull(nullStudent));

        assertEquals(expected, applicationService.checkNull(nullStudent));

        verify(applicationDao, times(2)).checkNull(nullStudent);
    }


}
