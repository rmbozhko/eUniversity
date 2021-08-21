package edu.spring.euniversity.service;

import edu.spring.euniversity.model.Student;
import edu.spring.euniversity.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElseThrow();
    }

    @Override
    public List<Student> createStudents(List<Student> students) {
        students.forEach(student -> studentRepository.save(student));
        return students;
    }
}
