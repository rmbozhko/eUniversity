package edu.spring.euniversity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Setter
@Document(value = "students")
public class Student {
    @Id
    private final String id;

    private String name;

    @Override
    public String toString() {
        return "{\n" +
                "\"id\":\"" + id +
                "\",\n\"name\":\"" + name + "\"}\n";
    }
}
