package com.example.mvcprac.dto.item;

import com.example.mvcprac.model.Item;
import com.example.mvcprac.util.status.DeliveryStatus;
import com.example.mvcprac.util.status.ItemSellStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemDetailDto {

    private Long id;
    private String itemName;
    private String itemBody;
    private ItemSellStatus itemSellStatus;
    private DeliveryStatus deliveryStatus;
    private int price;
    private List<ItemImageDto> itemImageDto;
    private String nickname;


    public ItemDetailDto(Item item, List<ItemImageDto> imageDtoList) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemBody = item.getItemBody();
        this.itemSellStatus = item.getItemSellStatus();
        this.deliveryStatus = item.getDeliveryChoice();
        this.price = item.getPrice();
        this.itemImageDto = imageDtoList;
        this.nickname = item.getAccount().getNickname();
    }
}
