package edu.spring.euniversity.service;

import edu.spring.euniversity.controller.command.MongoDocumentReferencer;
import edu.spring.euniversity.dto.CourseDto;
import edu.spring.euniversity.exception.AlreadyAssignedProfessorException;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Course;
import edu.spring.euniversity.model.Professor;
import edu.spring.euniversity.model.Student;
import edu.spring.euniversity.repository.CourseRepository;
import edu.spring.euniversity.repository.ProfessorRepository;
import edu.spring.euniversity.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    public CourseServiceImpl(@Autowired CourseRepository courseRepository, @Autowired ProfessorRepository professorRepository,
                             @Autowired StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Course> createCourses(List<CourseDto> courses) {
        List<Course> courseList = courses.stream()
                .map(courseDto -> new Course(courseDto.getName(), courseDto.getStartDate(), courseDto.getEndDate()))//,
//                                        new Professor(""), new ArrayList<>()))
                .collect(Collectors.toList());
        courseRepository.saveAll(courseList);
        return courseList;
    }

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(String courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new FoundNoInstanceException("No course found with id: " + courseId));
    }

    @Override
    public void deleteCourses() {
        courseRepository.deleteAll();
    }

    @Override
    public void deleteCourse(String courseId) {
        if (!courseRepository.existsById(courseId))
            throw new FoundNoInstanceException("No course found with id: " + courseId);
        courseRepository.deleteById(courseId);
    }

    @Override
    public Course updateCourseById(CourseDto courseWithUpdates, String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new FoundNoInstanceException("No course found with id: " + courseId));
        //TODO: How to handle situations when not all properties of object can be updated?
        course.setName(courseWithUpdates.getName());
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course assignProfessorToCourse(MongoDocumentReferencer professorReference, String courseId) {
        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new FoundNoInstanceException("No course found with id: " + courseId));
        if (course.getProfessor() != null)
            throw new AlreadyAssignedProfessorException("This course already has an assigned professor");
        Professor professor = professorRepository.findById(professorReference.getId())
                                                    .orElseThrow(() -> new FoundNoInstanceException("No professor found with id: " + professorReference.getId()));
        course.setProfessor(professor);
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course assignStudentsToCourse(List<MongoDocumentReferencer> studentReferenceList, String courseId) {
        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new FoundNoInstanceException("No course found with id: " + courseId));
        List<Student> students = studentReferenceList.stream()
                .map(studentReferencer -> studentRepository.findById(studentReferencer.getId())
                        .orElseThrow(() -> new FoundNoInstanceException("No student found with id: " + studentReferencer.getId()))).collect(Collectors.toList());
        course.setStudents(students);
        courseRepository.save(course);
        return course;
    }

    @Override
    public void generateCoursesReport() {
        try (PrintWriter pw = new PrintWriter("src/main/resources/report/report.csv")) {

            StringBuilder builder = new StringBuilder();
            builder.append("id,name,startDate,endDate,numberOfStudentsEnrolled,professor").append("\n");

            for (Course course : courseRepository.findAll()) {
                if (course.getStudents() != null && course.getStudents().size() > 2) {
                    builder.append(course.getReportData()).append("\n");
                }
            }

            pw.write(builder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
