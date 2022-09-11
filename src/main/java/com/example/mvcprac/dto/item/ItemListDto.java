package com.example.mvcprac.dto.item;

import lombok.Data;

@Data
public class ItemListDto {

    private Long id;
    private String itemName;
    private String itemBody;
    private int price;
}
