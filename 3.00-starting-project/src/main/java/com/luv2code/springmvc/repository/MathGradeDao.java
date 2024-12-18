package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradeDao extends CrudRepository<MathGrade, Integer> {

    Iterable<MathGrade> findByStudentId(int studentId);

    void deleteByStudentId(int studentId);
}
