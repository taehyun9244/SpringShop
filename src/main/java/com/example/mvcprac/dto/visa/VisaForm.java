package com.example.mvcprac.dto.visa;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisaForm {

    private String title;
    private String body;
    private String country;
}
