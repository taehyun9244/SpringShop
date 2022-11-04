package com.example.mvcprac.dto.visa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaDetailDto {

    private String title;
    private String body;
    private String country;
    private String nickName;
    private LocalDateTime createAt;
}
