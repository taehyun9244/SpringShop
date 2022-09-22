package com.example.mvcprac.dto.item;

import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemDetailDto {


    private Long id;
    private String itemName;
    private String itemBody;
    private String itemSellStatus;
    private String deliveryChoice;
    private int price;
    private List<Image> imageFiles;

    public ItemDetailDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemBody = item.getItemBody();
        this.itemSellStatus = item.getItemSellStatus();
        this.deliveryChoice = item.getDeliveryChoice();
        this.price = item.getPrice();
        this.imageFiles = item.getImages();
    }
}
