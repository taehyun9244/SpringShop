package com.example.mvcprac.model;

import com.example.mvcprac.dto.item.ItemAddDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemBody;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String itemSellStatus;

    @Column(nullable = false)
    private String deliveryChoice;

    public Item(ItemAddDto addDto) {
        this.itemName = addDto.getItemName();
        this.itemBody = addDto.getItemBody();
        this.price = addDto.getPrice();
        this.itemSellStatus = addDto.getItemSellStatus();
        this.deliveryChoice = addDto.getDeliveryChoice();
    }
}
