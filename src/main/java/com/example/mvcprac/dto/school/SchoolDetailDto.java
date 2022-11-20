package com.example.mvcprac.dto.school;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDetailDto {
    private Long id;
    private String title;
    private String body;
    private String schoolName;
    private String nickName;
    private LocalDateTime createAt;
}
