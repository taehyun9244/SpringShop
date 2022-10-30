package com.example.mvcprac.dto.visa;

import lombok.Data;

@Data
public class VisaSearchDto {

    private String searchDateType;
    private String country;
    private String searchBy;
    private String searchQuery = "";
}
