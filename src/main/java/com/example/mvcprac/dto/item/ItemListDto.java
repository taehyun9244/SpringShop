package com.example.mvcprac.dto.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemListDto {

    private Long id;
    private String itemName;
    private String itemSellStatus;
    private String deliveryChoice;
    private int price;
    private String storeFileName;

    public ItemListDto(Long id, String itemName, String itemSellStatus, String deliveryChoice, int price, String storeFileName) {
        this.id = id;
        this.itemName = itemName;
        this.itemSellStatus = itemSellStatus;
        this.deliveryChoice = deliveryChoice;
        this.price = price;
        this.storeFileName = storeFileName;
    }
}
