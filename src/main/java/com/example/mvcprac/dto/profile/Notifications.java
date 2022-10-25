package com.example.mvcprac.dto.profile;


import lombok.Data;

@Data
public class Notifications {

    private boolean shopCreatedByEmail;

    private boolean shopCreatedByWeb;

    private boolean shopEnrollmentResultByEmail;

    private boolean shopEnrollmentResultByWeb;

    private boolean shopUpdatedByEmail;

    private boolean shopUpdatedByWeb;

}
