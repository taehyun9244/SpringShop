package com.example.mvcprac.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemListDto {

    private Long id;
    private String itemName;
    private String itemSellStatus;
    private String deliveryChoice;
    private int price;
}
