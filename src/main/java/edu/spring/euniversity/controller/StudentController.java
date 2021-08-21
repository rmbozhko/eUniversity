package edu.spring.euniversity.controller;

import edu.spring.euniversity.dto.StudentDto;
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
    private final StudentServiceImpl studentService;

    private final Utility utility;

    public StudentController(@Autowired StudentServiceImpl studentService, @Autowired Utility utility) {
        this.studentService = studentService;
        this.utility = utility;
    }

    @PostMapping
    public ResponseEntity<String>   createStudents(@RequestBody final List<StudentDto> students) {

        List<Student> studentList = studentService.createStudents(students);
        return new ResponseEntity<>(studentList.toString(), HttpStatus.OK);
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

    @DeleteMapping
    public ResponseEntity<Void>   deleteStudents() {
        studentService.deleteStudents();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable final String studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.noContent().build();
        } catch (FoundNoInstanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("{studentId}")
    public ResponseEntity<String>   updateStudentById(@PathVariable final String studentId,
                                                      @RequestBody final StudentDto studentWithUpdates) {
        try {
            Student student = studentService.updateStudentById(studentWithUpdates, studentId);
            return new ResponseEntity<>(student.toString(), HttpStatus.OK);
        } catch (FoundNoInstanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
