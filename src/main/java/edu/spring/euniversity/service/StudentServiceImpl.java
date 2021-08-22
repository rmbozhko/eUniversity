package edu.spring.euniversity.service;

import edu.spring.euniversity.dto.StudentDto;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Student;
import edu.spring.euniversity.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<Student> createStudents(List<StudentDto> students) {
        List<Student> studentList = students.stream()
                .map(studentDto -> new Student(studentDto.getName()))
                .collect(Collectors.toList());
        studentRepository.saveAll(studentList);
        return studentList;
    }

    @Override
    public void deleteStudents() {
        studentRepository.deleteAll();
    }

    @Override
    public void deleteStudent(String studentId) throws FoundNoInstanceException {
        if (!studentRepository.existsById(studentId))
            throw new FoundNoInstanceException("No student found with id: " + studentId);
        studentRepository.deleteById(studentId);
    }

    @Override
    public Student updateStudentById(StudentDto studentWithUpdates, String studentId) throws FoundNoInstanceException {
        Student student = studentRepository.findById(studentId)
                                            .orElseThrow(() -> new FoundNoInstanceException("No student found with id: " + studentId));
        student.setName(studentWithUpdates.getName());
        studentRepository.save(student);
        return student;
    }
}
