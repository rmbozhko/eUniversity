package edu.spring.euniversity.service;

import edu.spring.euniversity.dto.StudentDto;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student getStudentById(String studentId) throws FoundNoInstanceException;
    List<Student> createStudents(final List<StudentDto> students);

    void deleteStudents();

    void deleteStudent(String studentId) throws FoundNoInstanceException;

    Student updateStudentById(StudentDto studentWithUpdates, String studentId) throws FoundNoInstanceException;
}
