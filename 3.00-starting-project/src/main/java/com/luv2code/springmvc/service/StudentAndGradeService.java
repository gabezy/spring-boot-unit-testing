package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.models.enumeration.GradeType;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;

    @Autowired
    private HistoryGradeDao historyGradeDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String emailAddress) {
        var student = new CollegeStudent(firstName, lastName, emailAddress);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent> studentOptional = studentDao.findById(id);
        return studentOptional.isPresent();
    }

    public void deleteStudent(int id) {
        if (checkIfStudentIsNull(id)) {
            studentDao.deleteById(id);
            mathGradeDao.deleteByStudentId(1);
            scienceGradeDao.deleteByStudentId(1);
            historyGradeDao.deleteByStudentId(1);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        return studentDao.findAll();
    }

    public void createGrade(double grade, int studentId, GradeType gradeType) {
        if (!checkIfStudentIsNull(studentId)) {
            return;
        }

        if (isValidGrade(grade)) {
            switch (gradeType) {
                case MATH -> addGrade(mathGradeDao, mathGrade, grade, studentId);
                case SCIENCE -> addGrade(scienceGradeDao, scienceGrade, grade, studentId);
                case HISTORY -> addGrade(historyGradeDao, historyGrade, grade, studentId);
            }
        }

    }

    // TODO: remove int return
    public int deleteGrade(int gradeId, GradeType gradeType) {
        int studentId = 0;

        switch (gradeType) {
            case MATH -> studentId = deleteGrade(mathGradeDao, gradeId);
            case SCIENCE -> studentId = deleteGrade(scienceGradeDao, gradeId);
            case HISTORY -> studentId = deleteGrade(historyGradeDao, gradeId);
        }

        return studentId;
    }

    public GradebookCollegeStudent getStudentInformation(int studentId) {
        Optional<CollegeStudent> optionalStudent = studentDao.findById(studentId);

        if (optionalStudent.isEmpty()) {
            return null;
        }

        List<Grade> mathGrades = new ArrayList<>();
        mathGradeDao.findByStudentId(studentId).forEach(mathGrades::add);

        List<Grade> scienceGrades = new ArrayList<>();
        scienceGradeDao.findByStudentId(studentId).forEach(scienceGrades::add);

        List<Grade> historyGrades = new ArrayList<>();
        historyGradeDao.findByStudentId(studentId).forEach(historyGrades::add);

        studentGrades.setMathGradeResults(mathGrades);
        studentGrades.setScienceGradeResults(scienceGrades);
        studentGrades.setHistoryGradeResults(historyGrades);

        var student = optionalStudent.get();

        return new GradebookCollegeStudent( student.getId(), student.getFirstname(), student.getLastname(),
                student.getEmailAddress(), studentGrades );
    }

    private boolean isValidGrade(double grade) {
        return grade >= 0 && grade <= 100;
    }

    private <E> void addGrade(CrudRepository<E, Integer> crudRepository, Grade gradeObj, double grade, int studentId) {
        gradeObj.setGrade(grade);
        gradeObj.setStudentId(studentId);
        crudRepository.save((E) gradeObj);
    }

    private <E> int deleteGrade(CrudRepository<E, Integer> repository, int gradeId) {
        var optionalGrade = repository.findById(gradeId);

        if (optionalGrade.isPresent()) {
            repository.delete(optionalGrade.get());
            var grade = (Grade) optionalGrade.get();
            return grade.getStudentId();
        }

        return 0;
    }

}
