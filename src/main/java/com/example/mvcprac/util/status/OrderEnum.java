package com.example.mvcprac.util.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderEnum {
    ORDER("ORDER", "ORDER", "주문"),
    CANCEL("CANCEL", "CANCEL", "주문취소");

    private final String code;
    private final String name;
    private final String nameKr;
}
