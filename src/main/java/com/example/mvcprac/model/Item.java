package com.example.mvcprac.model;

import com.example.mvcprac.dto.item.ItemDto;
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
    private int quantity;

    @Column(nullable = false)
    private String itemSellStatus;


    public Item(ItemDto addDto) {
        this.itemName = addDto.getItemName();
        this.itemBody = addDto.getItemBody();
        this.price = addDto.getPrice();
        this.quantity = addDto.getQuantity();
    }
}
