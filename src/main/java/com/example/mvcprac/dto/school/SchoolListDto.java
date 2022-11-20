package com.example.mvcprac.dto.school;

import com.example.mvcprac.model.School;
import com.example.mvcprac.model.Visa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolListDto {

    private Long id;
    private String title;
    private String nickname;
    private String schoolName;
    private LocalDateTime createdAt;

    public SchoolListDto(School school) {
        this.id = school.getId();
        this.title = school.getTitle();
        this.schoolName = school.getSchoolName();
        this.nickname = school.getAccount().getNickname();
        this.createdAt = school.getCreateAt();
    }

}
