package edu.spring.euniversity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.euniversity.controller.command.MongoDocumentReferencer;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Course;
import edu.spring.euniversity.model.Professor;
import edu.spring.euniversity.model.Student;
import edu.spring.euniversity.repository.CourseRepository;
import edu.spring.euniversity.repository.ProfessorRepository;
import edu.spring.euniversity.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private ProfessorRepository professorRepository;

    @MockBean
    private StudentRepository studentRepository;

    List<Course> courseList;

    @BeforeEach
    void setUp() {
        courseList = List.of(
                new Course("1", "Physics 101",
                        LocalDate.of(2021, 9, 2),
                        LocalDate.of(2022, 6, 25), new Professor("1", "Enrico Fermi"), null),
                new Course("2", "Mathematics 101",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 6, 25), null, null),
                new Course("3", "Astronomy 101",
                        LocalDate.of(2021, 9, 2),
                        LocalDate.of(2022, 6, 25), null, null),
                new Course("4", "Chemistry 101",
                        LocalDate.of(2021, 9, 2),
                        LocalDate.of(2021, 12, 31), null, null)
        );
    }

    @Test
    void createCourses() throws Exception {
        Mockito.when(courseRepository.saveAll(courseList)).thenReturn(courseList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(courseList));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[1].name", is(courseList.get(1).getName())));
    }

    @Test
    void getCourses() throws Exception {
        Mockito.when(courseRepository.findAll()).thenReturn(courseList);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/courses")
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(courseList.size())))
                .andExpect(jsonPath("$[2].name", is(courseList.get(2).getName())));
    }

    @Test
    void getCourseByIdWithCorrectId() throws Exception {
        Course course = courseList.get(new Random().nextInt(courseList.size()));
        Mockito.when(courseRepository.findById(course.getId()))
                .thenReturn(java.util.Optional.of(course));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/courses/" + course.getId())
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(course.getName())))
                .andExpect(jsonPath("$.startDate", is(course.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(course.getEndDate().toString())));
    }

    @Test
    void getCourseByIdWithWrongId() throws Exception {
        String courseId = "-1";
        Mockito.when(courseRepository.findById(courseId))
                .thenThrow(new FoundNoInstanceException("No course found with id: " + courseId));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/courses/" + courseId)
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No course found with id: " + courseId)));
    }

    @Test
    void deleteCourses() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/courses")
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCourseByIdWithCorrectId() throws Exception {
        Course course = courseList.get(new Random().nextInt(courseList.size()));
        Mockito.when(courseRepository.existsById(course.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/courses/" + course.getId())
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCourseByIdWithWrongId() throws Exception {
        String courseId ="-1";
        Mockito.when(courseRepository.existsById(courseId))
                .thenThrow(new FoundNoInstanceException("No course found with id: " + courseId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/courses/" + courseId)
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCourseByIdWithCorrectId() throws Exception {
        Course course = courseList.get(new Random().nextInt(courseList.size()));
        course.setName("Music 101");
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));
        Mockito.when(courseRepository.save(course)).thenReturn(course);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + course.getId())
                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                            .accept(MediaType.APPLICATION_JSON)
                                                                            .content(this.mapper.writeValueAsString(course));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(course.getName())))
                .andExpect(jsonPath("$.startDate", is(course.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(course.getEndDate().toString())));
    }

    @Test
    void updateCourseByIdWithWrongId() throws Exception {
        String courseId = "-1";
        Course course = courseList.get(new Random().nextInt(courseList.size()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/courses/" + courseId)
                                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                                .accept(MediaType.APPLICATION_JSON)
                                                                                .content(this.mapper.writeValueAsString(course));
        Mockito.when(courseRepository.existsById(courseId))
                .thenThrow(new FoundNoInstanceException("No course found with id: " + courseId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No course found with id: " + courseId)));
    }

    @Test
    void assignProfessorToCourseWithWrongCourseId() throws Exception {
        String courseId = "-1";
        MongoDocumentReferencer professorReferencer = new MongoDocumentReferencer("-1");
        Mockito.when(courseRepository.findById(courseId))
                .thenThrow(new FoundNoInstanceException("No course found with id: " + courseId));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + courseId + "/professor")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(professorReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No course found with id: " + courseId)));
    }

    @Test
    void assignProfessorToCourseWithAlreadyAssignedProfessor() throws Exception {
        Course course = courseList.get(0);
        MongoDocumentReferencer professorReferencer = new MongoDocumentReferencer("1");
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + course.getId() + "/professor")
                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                            .accept(MediaType.APPLICATION_JSON)
                                                                            .content(this.mapper.writeValueAsString(professorReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("This course already has an assigned professor")));
    }

    @Test
    void assignProfessorToCourseWithWrongProfessorId() throws Exception {
        Course course = courseList.get(1);
        MongoDocumentReferencer professorReferencer = new MongoDocumentReferencer("-1");
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));
        Mockito.when(professorRepository.findById(professorReferencer.getId()))
                .thenThrow(new FoundNoInstanceException("No professor found with id: " + professorReferencer.getId()));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + course.getId() + "/professor")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(professorReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No professor found with id: " + professorReferencer.getId())));
    }

    @Test
    void assignProfessorToCourseSuccessfully() throws Exception {
        Professor professor = new Professor("2", "Marie Curie");
        Course course = courseList.get(3);
        MongoDocumentReferencer professorReferencer = new MongoDocumentReferencer(professor.getId());
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));
        Mockito.when(professorRepository.findById(professor.getId())).thenReturn(java.util.Optional.of(professor));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + course.getId() + "/professor")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(professorReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(course.getName())))
                .andExpect(jsonPath("$.startDate", is(course.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(course.getEndDate().toString())))
                .andExpect(jsonPath("$.professor", is(professor.getName())));
    }

    @Test
    void assignStudentsToCourseWithWrongCourseId() throws Exception {
        String courseId = "-1";
        List<MongoDocumentReferencer> studentsReferencer = List.of(new MongoDocumentReferencer("1"));
        Mockito.when(courseRepository.findById(courseId))
                .thenThrow(new FoundNoInstanceException("No course found with id: " + courseId));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + courseId + "/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(studentsReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No course found with id: " + courseId)));
    }

    @Test
    void assignStudentsToCourseWithWrongStudentId() throws Exception {
        Course course = courseList.get(1);
        List<MongoDocumentReferencer> studentsReferencer = List.of(new MongoDocumentReferencer("1"));
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));
        Mockito.when(studentRepository.findById(studentsReferencer.get(0).getId()))
                .thenThrow(new FoundNoInstanceException("No student found with id: " + studentsReferencer.get(0).getId()));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + course.getId() + "/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(studentsReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No student found with id: " + studentsReferencer.get(0).getId())));
    }

    @Test
    void assignStudentsToCourseSuccessfully() throws Exception {
        Student student = new Student("1", "Roman Bozhko");
        Course course = courseList.get(1);
        List<MongoDocumentReferencer> studentsReferencer = List.of(new MongoDocumentReferencer(student.getId()));
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));
        Mockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/" + course.getId() + "/students")
                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                            .accept(MediaType.APPLICATION_JSON)
                                                                            .content(this.mapper.writeValueAsString(studentsReferencer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(course.getName())))
                .andExpect(jsonPath("$.startDate", is(course.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(course.getEndDate().toString())))
                .andExpect(jsonPath("$.numberOfStudents", is(course.getStudents().size())));
    }

}