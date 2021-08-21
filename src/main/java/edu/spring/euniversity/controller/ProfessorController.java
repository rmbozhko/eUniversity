package edu.spring.euniversity.controller;

import edu.spring.euniversity.dto.ProfessorDto;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Professor;
import edu.spring.euniversity.service.ProfessorServiceImpl;
import edu.spring.euniversity.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/professors")
public class ProfessorController {
    private final ProfessorServiceImpl professorService;

    private final Utility utility;

    public ProfessorController(@Autowired ProfessorServiceImpl professorService, @Autowired Utility utility) {
        this.professorService = professorService;
        this.utility = utility;
    }

    @PostMapping
    public ResponseEntity<String> createProfessors(@RequestBody final List<ProfessorDto> professors) {

        List<Professor> professorsList = professorService.createProfessors(professors);
        return new ResponseEntity<>(professorsList.toString(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String>   getProfessors() {
        return new ResponseEntity<>(utility.buildJson(professorService.getProfessors()), HttpStatus.OK);
    }

    @GetMapping("{professorId}")
    public ResponseEntity<String>   getProfessorById(@PathVariable final String professorId) {
        try {
            return new ResponseEntity<>(utility.buildJson(professorService.getProfessorById(professorId)), HttpStatus.OK);
        } catch (FoundNoInstanceException e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void>   deleteStudents() {
        professorService.deleteProfessors();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{professorId}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable final String professorId) {
        try {
            professorService.deleteProfessor(professorId);
            return ResponseEntity.noContent().build();
        } catch (FoundNoInstanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("{professorId}")
    public ResponseEntity<String>   updateStudentById(@PathVariable final String professorId,
                                                      @RequestBody final ProfessorDto professorWithUpdates) {
        try {
            Professor professor = professorService.updateProfessorById(professorWithUpdates, professorId);
            return new ResponseEntity<>(professor.toString(), HttpStatus.OK);
        } catch (FoundNoInstanceException e) {
            return new ResponseEntity<>("{\"error\" : \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}

