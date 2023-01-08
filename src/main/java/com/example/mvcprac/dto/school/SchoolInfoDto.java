package com.example.mvcprac.dto.school;

import com.example.mvcprac.model.School;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolInfoDto {

    private Long id;
    private String title;
    private String nickname;
    private String schoolName;
    private LocalDateTime createdAt;

    public SchoolInfoDto(School school) {
        this.id = school.getId();
        this.title = school.getTitle();
        this.nickname = school.getSchoolName();
        this.createdAt = school.getCreateAt();
    }
}
