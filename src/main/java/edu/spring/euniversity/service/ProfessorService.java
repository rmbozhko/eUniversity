package edu.spring.euniversity.service;

import edu.spring.euniversity.dto.ProfessorDto;
import edu.spring.euniversity.model.Professor;
import edu.spring.euniversity.model.Student;

import java.util.List;

public interface ProfessorService {
    List<Professor> createProfessors(List<ProfessorDto> professors);

    List<Professor> getProfessors();

    Professor getProfessorById(String professorId);

    void deleteProfessors();

    void deleteProfessor(String professorId);

    Professor updateProfessorById(ProfessorDto professorWithUpdates, String professorId);
}
