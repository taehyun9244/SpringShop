package com.example.mvcprac.dto.item;

import com.example.mvcprac.model.Item;
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

    public ItemListDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemSellStatus = item.getItemSellStatus();
        this.deliveryChoice = item.getDeliveryChoice();
        this.deliveryChoice = item.getDeliveryChoice();
        this.storeFileName = item.getImages().get(0).getStoreFileName();
    }
}
