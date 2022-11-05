package com.example.mvcprac.dto.visa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaSearchDto {

    private String searchDateType;
    private String country;
    private String searchBy;
    private String searchQuery = "";
}
