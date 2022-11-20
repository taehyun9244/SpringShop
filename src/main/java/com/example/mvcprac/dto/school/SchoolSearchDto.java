package com.example.mvcprac.dto.school;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolSearchDto {

    private String searchDateType;
    private String country;
    private String searchBy;
    private String searchQuery = "";
}
