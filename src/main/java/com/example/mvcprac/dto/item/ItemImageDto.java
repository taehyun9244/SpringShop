package com.example.mvcprac.dto.item;

import com.example.mvcprac.model.Image;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemImageDto {

    private Long id;
    private String storeFileName;


    /**
     * findByItemId -> Image
     */
    public ItemImageDto(Image image) {
        this.id = image.getId();
        this.storeFileName = image.getStoreFileName();
    }
}
