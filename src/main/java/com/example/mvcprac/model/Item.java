package com.example.mvcprac.model;

import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.util.Timestamped;
import com.example.mvcprac.util.status.DeliveryStatus;
import com.example.mvcprac.util.status.ItemSellStatus;
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
    private ItemSellStatus itemSellStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public Item(ItemForm form, Account account) {
        this.itemName = form.getItemName();
        this.itemBody = form.getItemBody();
        this.price = form.getPrice();
        this.itemSellStatus = form.getItemSellStatus();
        this.deliveryChoice = form.getDeliveryStatus();
        this.account = account;
    }

    public Item(Long id, ItemForm form, Account account) {
        this.id = id;
        this.itemName = form.getItemName();
        this.itemBody = form.getItemBody();
        this.price = form.getPrice();
        this.itemSellStatus = form.getItemSellStatus();
        this.deliveryChoice = form.getDeliveryStatus();
        this.account = account;
    }
}
