package edu.spring.euniversity.service;

import edu.spring.euniversity.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student getStudentById(String studentId);
    List<Student> createStudents(final List<Student> students);
}
