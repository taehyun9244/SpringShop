package com.example.mvcprac.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailDto {

    private Long id;
    private String itemName;
    private String itemBody;
    private String itemSellStatus;
    private String deliveryChoice;
    private int price;

}
