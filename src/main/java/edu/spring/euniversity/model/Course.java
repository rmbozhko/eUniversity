package edu.spring.euniversity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // for Mongo
@RequiredArgsConstructor // for creation
@AllArgsConstructor // for tests
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

    @DBRef
    private Professor professor;
    
    @DBRef
    private List<Student> students;

    @Override
    public String toString() {
        return "{\n" +
                "\"id\":\"" + id +
                "\",\n\"name\":\"" + name +
                "\",\n\"startDate\":\"" + startDate.toString() +
                "\",\n\"endDate\":\"" + endDate.toString() +
                "\",\n\"professor\":\"" + ((professor != null) ? professor.getName() : "") +
                "\",\n\"numberOfStudents\":" + ((students != null) ? students.size() : "")+
                "}\n";
    }
}

