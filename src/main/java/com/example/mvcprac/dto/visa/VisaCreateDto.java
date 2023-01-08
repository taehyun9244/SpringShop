package com.example.mvcprac.dto.visa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaCreateDto {

    @NotBlank(message = "제목을 입력해 주세요")
    @Length(max = 50)
    private String title;
    @NotBlank(message = "내용을 입력해 주세요")
    private String body;
    @NotBlank(message = "비자정보 국가를 입력해 주세요")
    private String country;
}
