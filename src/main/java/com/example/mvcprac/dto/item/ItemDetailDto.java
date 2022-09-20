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
public class ItemDetailDto {


    private Long id;
    private String itemName;
    private String itemBody;
    private String itemSellStatus;
    private String deliveryChoice;
    private int price;
    private List<Image> imageFiles;

    public ItemDetailDto(Item findItem) {
        this.id = findItem.getId();
        this.itemName = findItem.getItemName();
        this.itemBody = findItem.getItemBody();
        this.itemSellStatus = findItem.getItemSellStatus();
        this.deliveryChoice = findItem.getDeliveryChoice();
        this.price = findItem.getPrice();
        this.imageFiles = findItem.getImages();
    }
}
