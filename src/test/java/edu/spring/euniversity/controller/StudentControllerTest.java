package edu.spring.euniversity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Student;
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

import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StudentRepository studentRepository;

    List<Student> studentList;

    @BeforeEach
    void setUp() {
        studentList = List.of(
                new Student("1", "Roman Bozhko"),
                new Student("2", "John Smith"),
                new Student("3", "Mark Duwn"),
                new Student("4", "Elisabeth Brems"),
                new Student("5", "Nansy Pollock"),
                new Student("6", "Carys Thomson"),
                new Student("7", "Loren Whitfield"),
                new Student("8", "Jacob Whyte"),
                new Student("9", "Jonathan Harrell"),
                new Student("10", "Donte Broadhurst"),
                new Student("11", "Hope Duke"),
                new Student("12", "Ralphy Mcgill"),
                new Student("13", "Kennedy Ahmad"));
    }

    @Test
    void createStudents() throws Exception {
        Mockito.when(studentRepository.saveAll(studentList)).thenReturn(studentList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .accept(MediaType.APPLICATION_JSON)
                                                                        .content(this.mapper.writeValueAsString(studentList));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[0].name", is(studentList.get(0).getName())));
    }

    @Test
    void getStudents() throws Exception {
        Mockito.when(studentRepository.findAll()).thenReturn(studentList);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(studentList.size())))
                .andExpect(jsonPath("$[2].name", is("Mark Duwn")));
    }

    @Test
    void getStudentByIdWithCorrectId() throws Exception {
        Student student = studentList.get(new Random().nextInt(studentList.size()));
        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(java.util.Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/students/" + student.getId())
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(student.getName())));
    }

    @Test
    void getStudentByIdWithWrongId() throws Exception {
        Student student = new Student("-1", "Test");
        Mockito.when(studentRepository.findById(student.getId()))
                .thenThrow(new FoundNoInstanceException("No student found with id: " + student.getId()));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/students/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No student found with id: " + student.getId())));
    }

    @Test
    void deleteStudents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/students")
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudentByIdSuccess() throws Exception {
        Student student = studentList.get(new Random().nextInt(studentList.size()));
        Mockito.when(studentRepository.existsById(student.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/students/" + student.getId())
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudentByIdWithWrongId() throws Exception {
        Student student = new Student("-1", "Test");
        Mockito.when(studentRepository.existsById(student.getId()))
                .thenThrow(new FoundNoInstanceException("No student found with id: " + student.getId()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/students/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStudentByIdWithCorrectId() throws Exception {
        Student student = studentList.get(new Random().nextInt(studentList.size()));
        student.setName("Friedrich Gauss");
        Mockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
        Mockito.when(studentRepository.save(student)).thenReturn(student);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/students/" + student.getId())
                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                            .accept(MediaType.APPLICATION_JSON)
                                                                            .content(this.mapper.writeValueAsString(student));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(student.getName())));
    }

    @Test
    void updateStudentByIdWithWrongId() throws Exception {
        Student student = new Student("-1", "Test");
        Mockito.when(studentRepository.findById(student.getId()))
                .thenThrow(new FoundNoInstanceException("No student found with id: " + student.getId()));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/students/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(student));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No student found with id: " + student.getId())));
    }
}