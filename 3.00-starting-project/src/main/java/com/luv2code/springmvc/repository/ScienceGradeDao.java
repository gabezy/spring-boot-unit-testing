package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradeDao extends CrudRepository<ScienceGrade, Integer> {

    Iterable<ScienceGrade> findByStudentId(int studentId);

    void deleteByStudentId(int studentId);

}
