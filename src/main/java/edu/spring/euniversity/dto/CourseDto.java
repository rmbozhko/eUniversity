package edu.spring.euniversity.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CourseDto {
    @NonNull
    private String name;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;
}
