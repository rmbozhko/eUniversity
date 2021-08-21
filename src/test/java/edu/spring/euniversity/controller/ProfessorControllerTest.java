package edu.spring.euniversity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Professor;
import edu.spring.euniversity.repository.ProfessorRepository;
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
class ProfessorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ProfessorRepository professorRepository;

    List<Professor> professorList;

    @BeforeEach
    void setUp() {
        professorList = List.of(
                new Professor("1", "Enrico Fermi"),
                new Professor("2", "Yukawa Hideki"),
                new Professor("3", "Owen Chamberlain"),
                new Professor("4", "Richard P. Feynman"),
                new Professor("5", "Roger Penrose"));
    }

    @Test
    void createProfessors() throws Exception {
        Mockito.when(professorRepository.saveAll(professorList)).thenReturn(professorList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(professorList));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[0].name", is(professorList.get(0).getName())));
    }

    @Test
    void getProfessors() throws Exception {
        Mockito.when(professorRepository.findAll()).thenReturn(professorList);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/professors")
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(professorList.size())))
                .andExpect(jsonPath("$[2].name", is(professorList.get(2).getName())));
    }

    @Test
    void getProfessorByIdWithCorrectId() throws Exception {
        Professor professor = professorList.get(new Random().nextInt(professorList.size()));
        Mockito.when(professorRepository.findById(professor.getId()))
                .thenReturn(java.util.Optional.of(professor));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/professors/" + professor.getId())
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(professor.getName())));
    }

    @Test
    void getProfessorByIdWithWrongId() throws Exception {
        String professorId = "-1";
        Mockito.when(professorRepository.findById(professorId))
                .thenThrow(new FoundNoInstanceException("No professor found with id: " + professorId));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/professors/" + professorId)
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.error", is("No professor found with id: " + professorId)));
    }

    @Test
    void deleteProfessors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/professors")
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProfessorByIdWithCorrectId() throws Exception {
        Professor professor = professorList.get(new Random().nextInt(professorList.size()));
        Mockito.when(professorRepository.existsById(professor.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/professors/" + professor.getId())
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProfessorByIdWithWrongId() throws Exception {
        String professorId ="-1";
        Mockito.when(professorRepository.existsById(professorId))
                .thenThrow(new FoundNoInstanceException("No professor found with id: " + professorId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/professors/" + professorId)
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStudentByIdWithCorrectId() throws Exception {
        Professor professor = professorList.get(new Random().nextInt(professorList.size()));
        Mockito.when(professorRepository.findById(professor.getId())).thenReturn(java.util.Optional.of(professor));
        Mockito.when(professorRepository.save(professor)).thenReturn(professor);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/professors/" + professor.getId())
                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                            .accept(MediaType.APPLICATION_JSON)
                                                                            .content(this.mapper.writeValueAsString(professor));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(professor.getName())));
    }

    @Test
    void updateStudentByIdWithWrongId() throws Exception {
        String professorId = "-1";
        Mockito.when(professorRepository.existsById(professorId))
                .thenThrow(new FoundNoInstanceException("No professor found with id: " + professorId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/professors/" + professorId)
                                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}