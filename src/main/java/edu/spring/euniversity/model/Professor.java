package edu.spring.euniversity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Setter
@Document(value = "professors")
public class Professor {
    @Id
    private String id;

    private String name;

    @Override
    public String toString() {
        return "{\n" +
                "\"id\":\"" + id +
                "\",\n\"name\":\"" + name + "\"}\n";
    }
}
