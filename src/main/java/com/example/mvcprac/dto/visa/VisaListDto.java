package com.example.mvcprac.dto.visa;

import com.example.mvcprac.model.Visa;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisaListDto {

    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;

    public VisaListDto(Visa visa) {
        this.id = visa.getId();
        this.title = visa.getTitle();;
        this.writer = visa.getAccount().getNickname();
        this.createdAt = visa.getCreatedAt();
    }


    //TODO 댓글수, 조회수 내려주기
}
