package com.example.mvcprac.model;

import com.example.mvcprac.dto.item.ItemCreateDto;
import com.example.mvcprac.util.Timestamped;
import com.example.mvcprac.util.status.DeliveryEnum;
import com.example.mvcprac.util.status.ItemEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "items")
public class Item extends Timestamped {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemBody;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private ItemEnum itemEnum;

    @Enumerated(EnumType.STRING)
    private DeliveryEnum deliveryEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public Item(ItemCreateDto itemCreateDto, Account account) {
        this.itemName = itemCreateDto.getItemName();
        this.itemBody = itemCreateDto.getItemBody();
        this.price = itemCreateDto.getPrice();
        this.itemEnum = itemCreateDto.getItemEnum();
        this.deliveryEnum = itemCreateDto.getDeliveryEnum();
        this.account = account;
    }

    public Item(Long id, ItemCreateDto itemCreateDto, Account account) {
        this.id = id;
        this.itemName = itemCreateDto.getItemName();
        this.itemBody = itemCreateDto.getItemBody();
        this.price = itemCreateDto.getPrice();
        this.itemEnum = itemCreateDto.getItemEnum();
        this.deliveryEnum = itemCreateDto.getDeliveryEnum();
        this.account = account;
    }
}
