package edu.spring.euniversity.service;

import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student getStudentById(String studentId) throws FoundNoInstanceException;
    void createStudents(final List<Student> students);
}
