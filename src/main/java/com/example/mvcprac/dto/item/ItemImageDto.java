package com.example.mvcprac.dto.item;

import com.example.mvcprac.model.Image;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
public class ItemImageDto {

    private Long id;
    private String storeFileName;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImageDto of(Image images) {

        return modelMapper.map(images, ItemImageDto.class);
    }
}
