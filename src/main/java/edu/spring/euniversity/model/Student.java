package edu.spring.euniversity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "students")
public class Student {
    @Id
    private String id;

    private String name;
}
