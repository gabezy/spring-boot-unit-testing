package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.models.enumeration.GradeType;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class StudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;

    @Autowired
    private HistoryGradeDao historyGradeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupDatabase() {
        String studentSql = "INSERT INTO student(id, firstname, lastname, email_address) " +
                "VALUES (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')";

        String mathGrade = "INSERT INTO math_grade(id, student_id, grade) VALUES (1, 1, 100.00)";
        String scienceGrade = "INSERT INTO science_grade(id, student_id, grade) VALUES (1, 1, 87.50)";
        String historyGrade = "INSERT INTO history_grade(id, student_id, grade) VALUES (1, 1, 90.00)";

        jdbcTemplate.execute(studentSql);
        jdbcTemplate.execute(mathGrade);
        jdbcTemplate.execute(scienceGrade);
        jdbcTemplate.execute(historyGrade);

    }

    @Test
    void should_create_student() {
        studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

        String email = "chad.darby@luv2code_school.com";

        CollegeStudent student = studentDao.findByEmailAddress(email);

        assertEquals(email, student.getEmailAddress());
        assertEquals("Chad", student.getFirstname());
    }

    @Test
    void should_check_if_isStudentNull() {
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    void should_delete_student_by_id() {
        Optional<CollegeStudent> studentOptional = studentDao.findById(1);
        Iterable<MathGrade> mathGrades = mathGradeDao.findByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findByStudentId(1);

        assertTrue(studentOptional.isPresent());
        assertTrue(mathGrades.iterator().hasNext());
        assertTrue(scienceGrades.iterator().hasNext());
        assertTrue(historyGrades.iterator().hasNext());

        studentService.deleteStudent(1);

        assertFalse(studentDao.findById(1).isPresent());
        assertFalse(mathGradeDao.findByStudentId(1).iterator().hasNext());
        assertFalse(scienceGradeDao.findByStudentId(1).iterator().hasNext());
        assertFalse(historyGradeDao.findByStudentId(1).iterator().hasNext());
    }

    @Sql("/insert_data.sql")
    @Test
    void should_get_gradebook() {
        Iterable<CollegeStudent> iterableCollegaStudent = studentService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for(CollegeStudent collegeStudent : iterableCollegaStudent) {
            collegeStudents.add(collegeStudent);
        }

        assertEquals(5, collegeStudents.size());
    }

    @Test
    void should_create_grade_to_student() {
        studentService.createGrade(80.50, 1, GradeType.MATH);
        studentService.createGrade(90.10, 1, GradeType.SCIENCE);
        studentService.createGrade(100D, 1, GradeType.HISTORY);

        Iterable<MathGrade> mathGrades = mathGradeDao.findByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findByStudentId(1);

        assertTrue(mathGrades.iterator().hasNext());
        assertTrue(scienceGrades.iterator().hasNext());
        assertTrue(historyGrades.iterator().hasNext());

        assertEquals(2, ((Collection<MathGrade>) mathGrades).size());
        assertEquals(2, ((Collection<ScienceGrade>) scienceGrades).size());
        assertEquals(2, ((Collection<HistoryGrade>) historyGrades).size());
    }

    @Test
    void should_not_create_grade_with_invalid_inputs_to_student() {
        studentService.createGrade(120, 1, GradeType.MATH);
        studentService.createGrade(100D, 2, GradeType.SCIENCE);
        studentService.createGrade(-6, 1, GradeType.HISTORY);

        Iterable<MathGrade> mathGrades = mathGradeDao.findByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findByStudentId(1);

        assertTrue(mathGrades.iterator().hasNext());
        assertTrue(scienceGrades.iterator().hasNext());
        assertTrue(historyGrades.iterator().hasNext());

        assertEquals(1, ((Collection<MathGrade>) mathGrades).size());
        assertEquals(1, ((Collection<ScienceGrade>) scienceGrades).size());
        assertEquals(1, ((Collection<HistoryGrade>) historyGrades).size());
    }

    @Test
    void should_delete_grade_by_id() {
        assertTrue(mathGradeDao.findById(1).isPresent());
        assertTrue(scienceGradeDao.findById(1).isPresent());
        assertTrue(historyGradeDao.findById(1).isPresent());

        assertEquals(1, studentService.deleteGrade(1, GradeType.MATH));
        assertEquals(1, studentService.deleteGrade(1, GradeType.SCIENCE));
        assertEquals(1, studentService.deleteGrade(1, GradeType.HISTORY));

        assertFalse(mathGradeDao.findById(1).isPresent());
        assertFalse(scienceGradeDao.findById(1).isPresent());
        assertFalse(historyGradeDao.findById(1).isPresent());
    }

    @Test
    void should_not_delete_grade_idZero() {
        assertFalse(mathGradeDao.findById(0).isPresent());
        assertFalse(scienceGradeDao.findById(0).isPresent());
        assertFalse(historyGradeDao.findById(0).isPresent());

        assertEquals(0, studentService.deleteGrade(0, GradeType.MATH));
        assertEquals(0, studentService.deleteGrade(0, GradeType.SCIENCE));
        assertEquals(0, studentService.deleteGrade(0, GradeType.HISTORY));
    }

    @Test
    void should_get_student_information() {
        GradebookCollegeStudent gradebookCollegeStudent = studentService.getStudentInformation(1);

        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Eric", gradebookCollegeStudent.getFirstname());
        assertEquals("Roby", gradebookCollegeStudent.getLastname());
        assertEquals("eric.roby@luv2code_school.com", gradebookCollegeStudent.getEmailAddress());

        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size());
    }

    @AfterEach
    void tearDownDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "student", "math_grade", "science_grade", "history_grade");
    }
}
