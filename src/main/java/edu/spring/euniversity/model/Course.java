package edu.spring.euniversity.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(value = "courses")
public class Course {
    @Id
    private String id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer numberOfStudentsEnrolled;

    private Professor professor;
}
