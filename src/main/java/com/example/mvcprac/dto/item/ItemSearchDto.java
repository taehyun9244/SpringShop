package com.example.mvcprac.dto.item;

import lombok.Data;

@Data
public class ItemSearchDto {

    private String searchDateType;
    private String itemSellStatus;
    private String searchBy;
    private String searchQuery = "";
}
