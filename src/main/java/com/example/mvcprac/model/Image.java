package com.example.mvcprac.model;

import com.example.mvcprac.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image extends Timestamped {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    //고객이 업로드한 파일명
    private String uploadFileName;

    @Column(nullable = false)
    ////서버에 저장하는 파일명
    private String storeFileName;

    private boolean exitFirstImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;


    public Image(String uploadFileName, String storeFileName, Item saveItem, boolean firstImage) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.item = saveItem;
        this.exitFirstImage = firstImage;
    }
}