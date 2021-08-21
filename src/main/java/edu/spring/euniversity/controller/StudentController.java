package edu.spring.euniversity.controller;

import edu.spring.euniversity.model.Student;
import edu.spring.euniversity.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @PostMapping
    public ResponseEntity<String>   createStudents(@RequestBody final List<Student> students) {

    }

    @GetMapping
    public ResponseEntity<String>   getStudents() {
        studentService.getStudents()
    }

    @GetMapping("{studentId}")
    public ResponseEntity<String>   getStudentById(@PathVariable String studentId) {

    }
}
