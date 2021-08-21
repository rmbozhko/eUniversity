package edu.spring.euniversity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "professors")
public class Professor {
    @Id
    private String id;

    private String name;
}
