package edu.spring.euniversity.service;

import edu.spring.euniversity.exception.FoundNoInstanceException;
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
    public Student getStudentById(String studentId) throws FoundNoInstanceException {
        return studentRepository.findById(studentId)
                                .orElseThrow(() -> new FoundNoInstanceException("No student found with id: " + studentId));
    }

    @Override
    public void createStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }
}
