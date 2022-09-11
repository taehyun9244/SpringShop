package com.example.mvcprac.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSellStatus {
    SELL("SELL", "SELL", "판매중"),
    SOLD("SOLD", "SOLD_OUT", "재고없음");

    private final String code;
    private final String name;
    private final String nameKr;
}
