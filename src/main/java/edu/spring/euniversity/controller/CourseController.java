package edu.spring.euniversity.controller;

import edu.spring.euniversity.controller.command.MongoDocumentReferencer;
import edu.spring.euniversity.dto.CourseDto;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Course;
import edu.spring.euniversity.service.CourseServiceImpl;
import edu.spring.euniversity.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    private final CourseServiceImpl courseService;

    private final Utility utility;

    public CourseController(@Autowired CourseServiceImpl courseService, @Autowired Utility utility) {
        this.courseService = courseService;
        this.utility = utility;
    }

    @PostMapping
    public ResponseEntity<String> createCourses(@RequestBody final List<CourseDto> courses) {

        List<Course> coursesList = courseService.createCourses(courses);
        return new ResponseEntity<>(coursesList.toString(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String>   getCourses() {
        return new ResponseEntity<>(utility.buildJson(courseService.getCourses()), HttpStatus.OK);
    }

    @GetMapping("{courseId}")
    public ResponseEntity<String>   getCourseById(@PathVariable final String courseId) {
        try {
            return new ResponseEntity<>(courseService.getCourseById(courseId).toString(), HttpStatus.OK);
        } catch (FoundNoInstanceException e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void>   deleteCourses() {
        courseService.deleteCourses();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{courseId}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable final String courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.noContent().build();
        } catch (FoundNoInstanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("{courseId}")
    public ResponseEntity<String>   updateCourseById(@PathVariable final String courseId,
                                                      @RequestBody final CourseDto courseWithUpdates) {
        try {
            Course course = courseService.updateCourseById(courseWithUpdates, courseId);
            return new ResponseEntity<>(course.toString(), HttpStatus.OK);
        } catch (FoundNoInstanceException e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("{courseId}/professor")
    public ResponseEntity<String>   assignProfessorToCourse(@PathVariable final String courseId,
                                                            @RequestBody final MongoDocumentReferencer professor) {
        try {
            Course course = courseService.assignProfessorToCourse(professor, courseId);
            return new ResponseEntity<>(course.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{courseId}/students")
    public ResponseEntity<String>   assignStudentsToCourse(@PathVariable final String courseId,
                                                            @RequestBody final List<MongoDocumentReferencer> studentReferenceList) {
        try {
            Course course = courseService.assignStudentsToCourse(studentReferenceList, courseId);
            return new ResponseEntity<>(course.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}

