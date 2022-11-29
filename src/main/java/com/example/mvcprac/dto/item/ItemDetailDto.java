package com.example.mvcprac.dto.item;

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
    private List<ItemImageDto> itemImageDto;
    private String nickname;


    public ItemDetailDto(Item item, List<ItemImageDto> imageDtoList) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemBody = item.getItemBody();
        this.itemSellStatus = item.getItemSellStatus();
        this.deliveryChoice = item.getDeliveryChoice();
        this.price = item.getPrice();
        this.itemImageDto = imageDtoList;
        this.nickname = item.getAccount().getNickname();
    }
}
