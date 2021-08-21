package edu.spring.euniversity.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "students")
public class Student {
    @Id
    private String id;

    private String name;
}
