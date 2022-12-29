package com.example.mvcprac.dto.item;

import com.example.mvcprac.util.status.DeliveryStatus;
import com.example.mvcprac.util.status.ItemSellStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemListDto {

    private Long id;
    private String itemName;
    private ItemSellStatus itemSellStatus;
    private DeliveryStatus deliveryChoice;
    private int price;
    private String storeFileName;

    public ItemListDto(Long id, String itemName, ItemSellStatus itemSellStatus, DeliveryStatus deliveryChoice, int price, String storeFileName) {
        this.id = id;
        this.itemName = itemName;
        this.itemSellStatus = itemSellStatus;
        this.deliveryChoice = deliveryChoice;
        this.price = price;
        this.storeFileName = storeFileName;
    }
}
