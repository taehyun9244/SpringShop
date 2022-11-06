package com.example.mvcprac.dto.visa;

import com.example.mvcprac.model.Visa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaListDto {

    private Long id;
    private String title;
    private String nickname;
    private String country;
    private LocalDateTime createdAt;

    public VisaListDto(Visa visa) {
        this.id = visa.getId();
        this.title = visa.getTitle();
        this.nickname = visa.getAccount().getNickname();
        this.country = visa.getCountry();
        this.createdAt = visa.getCreatedAt();
    }


    //TODO 댓글수, 조회수 내려주기
}
