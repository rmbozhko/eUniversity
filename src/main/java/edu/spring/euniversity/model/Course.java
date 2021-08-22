package edu.spring.euniversity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Document(value = "courses")
public class Course {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;

    private Professor professor;

    private List<Student> students;
}

