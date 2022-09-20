package com.example.mvcprac.dto.item;

import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemListDto {

    private Long id;
    private String itemName;
    private String itemSellStatus;
    private String deliveryChoice;
    private int price;
    private List<Image> imageFiles;

    public ItemListDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemSellStatus = item.getItemSellStatus();
        this.deliveryChoice = item.getDeliveryChoice();
        this.price = item.getPrice();
        this.imageFiles = item.getImages();
    }
}
