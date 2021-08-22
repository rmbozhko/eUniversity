package edu.spring.euniversity.service;

import edu.spring.euniversity.dto.CourseDto;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Course;
import edu.spring.euniversity.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(@Autowired CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> createCourses(List<CourseDto> courses) {
        List<Course> courseList = courses.stream()
                .map(courseDto -> new Course(courseDto.getName(), courseDto.getStartDate(), courseDto.getEndDate()))
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
}
