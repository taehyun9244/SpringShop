package com.example.mvcprac.util.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemEnum {

    SELL("SELL", "SELL", "판매중"),
    SOLD("SOLD", "SOLD", "재고없음");

    private final String code;
    private final String name;
    private final String nameKr;
}
