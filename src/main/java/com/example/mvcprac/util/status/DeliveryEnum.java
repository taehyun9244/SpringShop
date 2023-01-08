package com.example.mvcprac.util.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryEnum {

    LOTTE("LOTTE", "LOTTE", "롯데"),
    POST("POST", "Post office", "우체국");

    private final String code;
    private final String name;
    private final String nameKr;
}
