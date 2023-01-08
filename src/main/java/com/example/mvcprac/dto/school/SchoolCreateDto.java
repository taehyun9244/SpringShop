package com.example.mvcprac.dto.school;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolCreateDto {

    private String title;
    private String body;
    private String schoolName;
    private LocalDateTime createAt;
}
