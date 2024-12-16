package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
class StudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupDatabase() {
        String sql = "INSERT INTO student(id, firstname, lastname, email_address) " +
                "VALUES (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')";
        jdbcTemplate.execute(sql);
    }

    @Test
    void should_create_student() {
        studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

        String email = "chad.darby@luv2code_school.com";

        CollegeStudent student = studentDao.findByEmailAddress(email);

        assertEquals(email, student.getEmailAddress());
        System.out.println(student.getId());
    }

    @Test
    void should_check_if_isStudentNull() {
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    void should_delete_student() {
        Optional<CollegeStudent> studentOptional = studentDao.findById(1);

        assertTrue(studentOptional.isPresent());

        studentService.deleteStudent(1);

        Optional<CollegeStudent> deletedStudent = studentDao.findById(1);

        assertTrue(deletedStudent.isEmpty());
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

    @AfterEach
    void tearDownDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "student");
    }
}
