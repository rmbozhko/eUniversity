package edu.spring.euniversity.exception;

public class AlreadyAssignedProfessorException extends RuntimeException {
    public AlreadyAssignedProfessorException(String message) {
        super(message);
    }
}
