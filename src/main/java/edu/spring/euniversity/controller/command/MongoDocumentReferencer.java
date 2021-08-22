package edu.spring.euniversity.controller.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class MongoDocumentReferencer {
    @NonNull
    private String id;
}
