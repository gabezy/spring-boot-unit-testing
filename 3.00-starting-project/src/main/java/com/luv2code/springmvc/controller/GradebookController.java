package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Gradebook;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GradebookController {

    @Autowired
    private Gradebook gradebook;

    @Autowired
    private StudentAndGradeService service;

    @GetMapping(value = "/")
    public String getStudents(Model m) {
        addStudentsToModel(m);
        return "index";
    }

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {
        return "studentInformation";
    }

    @PostMapping("/")
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {
        this.service.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
        addStudentsToModel(m);
        return "index";
    }

    @GetMapping("/delete/student/{id}")
    public String deleteStudent(@PathVariable("id") int id, Model m) {
        if (this.service.checkIfStudentIsNull(id)) {
            this.service.deleteStudent(id);
            addStudentsToModel(m);
            return "index";
        }

        return "error";
    }

    public void addStudentsToModel(Model m) {
        m.addAttribute("students", this.service.getGradebook());
    }

}
