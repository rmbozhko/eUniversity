package edu.spring.euniversity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Course;
import edu.spring.euniversity.repository.CourseRepository;
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

    List<Course> courseList;

    @BeforeEach
    void setUp() {
        courseList = List.of(
                new Course("1", "Physics 101",
                        LocalDate.of(2021, 9, 2),
                        LocalDate.of(2022, 6, 25), null, null),
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
        Mockito.when(courseRepository.existsById(courseId))
                .thenThrow(new FoundNoInstanceException("No course found with id: " + courseId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/courses/" + courseId)
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}