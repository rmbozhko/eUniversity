package edu.spring.euniversity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@RequiredArgsConstructor
@Document(value = "students")
public class Student {
    @Id
    private final String id;

    @NonNull
    private String name;

    @Override
    public String toString() {
        return "{\n" +
                "\"id\":\"" + id +
                "\",\n\"name\":\"" + name + "\"}\n";
    }
}
