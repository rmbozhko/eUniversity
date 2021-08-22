package edu.spring.euniversity.service;

import edu.spring.euniversity.controller.command.MongoDocumentReferencer;
import edu.spring.euniversity.dto.CourseDto;
import edu.spring.euniversity.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> createCourses(List<CourseDto> courses);

    List<Course> getCourses();

    Course getCourseById(String courseId);

    void deleteCourses();

    void deleteCourse(String courseId);

    Course updateCourseById(CourseDto courseWithUpdates, String courseId);

    Course assignProfessorToCourse(MongoDocumentReferencer professor, String courseId);

    Course assignStudentsToCourse(List<MongoDocumentReferencer> studentList, String courseId);
}
