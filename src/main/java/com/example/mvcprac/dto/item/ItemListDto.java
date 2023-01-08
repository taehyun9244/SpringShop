package com.example.mvcprac.dto.item;

import com.example.mvcprac.util.status.DeliveryEnum;
import com.example.mvcprac.util.status.ItemEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemListDto {

    private Long id;
    private String itemName;
    private ItemEnum itemEnum;
    private DeliveryEnum deliveryEnum;
    private int price;
    private String storeFileName;
 }

