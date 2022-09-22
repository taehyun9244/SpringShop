package com.example.mvcprac.model;

import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item extends Timestamped {

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<Image>();

    public Item(ItemForm addDto, List<Image> storeImageFiles) {
        this.itemName = addDto.getItemName();
        this.itemBody = addDto.getItemBody();
        this.price = addDto.getPrice();
        this.itemSellStatus = addDto.getItemSellStatus();
        this.deliveryChoice = addDto.getDeliveryChoice();
        this.images = storeImageFiles;
    }
}
