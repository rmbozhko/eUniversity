package edu.spring.euniversity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ProfessorDto {
    @NonNull
    private String name;
}
