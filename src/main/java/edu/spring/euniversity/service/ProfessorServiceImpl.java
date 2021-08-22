package edu.spring.euniversity.service;

import edu.spring.euniversity.dto.ProfessorDto;
import edu.spring.euniversity.exception.FoundNoInstanceException;
import edu.spring.euniversity.model.Professor;
import edu.spring.euniversity.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl(@Autowired ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public List<Professor> getProfessors() {
        return professorRepository.findAll();
    }

    @Override
    public Professor getProfessorById(String professorId) {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new FoundNoInstanceException("No professor found with id: " + professorId));
    }

    @Override
    public List<Professor> createProfessors(List<ProfessorDto> professors) {
        List<Professor> professorList = professors.stream()
                .map(professorDto -> new Professor(professorDto.getName()))
                .collect(Collectors.toList());
        professorRepository.saveAll(professorList);
        return professorList;
    }

    @Override
    public void deleteProfessors() {
        professorRepository.deleteAll();
    }

    @Override
    public void deleteProfessor(String professorId) {
        if (!professorRepository.existsById(professorId))
            throw new FoundNoInstanceException("No professor found with id: " + professorId);
        professorRepository.deleteById(professorId);
    }

    @Override
    public Professor updateProfessorById(ProfessorDto professorWithUpdates, String professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new FoundNoInstanceException("No professor found with id: " + professorId));
        professor.setName(professorWithUpdates.getName());
        professorRepository.save(professor);
        return professor;
    }
}
