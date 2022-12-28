package com.example.mvcprac.dto.item;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemForm {

    private Long id;
    @NotBlank(message = "상품의 이름을 입력해 주세요")
    private String itemName;

    @NotNull(message = "상품의 상세내용을 입력해 주세요")
    private String itemBody;

    @NotNull(message = "상품의 가격을 입력해 주세요")
    private int price;

    @NotNull
    private String itemSellStatus;
    @NotNull
    private String deliveryChoice;
    private List<MultipartFile> imageFiles;
}
