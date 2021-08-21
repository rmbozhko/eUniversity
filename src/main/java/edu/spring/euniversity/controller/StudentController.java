package edu.spring.euniversity.controller;

import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Student;
import edu.spring.euniversity.service.StudentServiceImpl;
import edu.spring.euniversity.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private Utility utility;

    @PostMapping
    public ResponseEntity<String>   createStudents(@RequestBody final List<Student> students) {
        studentService.createStudents(students);
        return new ResponseEntity<>("{\"message\": \"Created " + students.size() +  " student(s)}", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String>   getStudents() {
        return new ResponseEntity<>(utility.buildJson(studentService.getStudents()), HttpStatus.OK);
    }

    @GetMapping("{studentId}")
    public ResponseEntity<String>   getStudentById(@PathVariable final String studentId) {
        try {
            return new ResponseEntity<>(utility.buildJson(studentService.getStudentById(studentId)), HttpStatus.OK);
        } catch (FoundNoInstanceException e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}
