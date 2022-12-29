package com.example.mvcprac.dto.item;

import com.example.mvcprac.util.status.DeliveryStatus;
import com.example.mvcprac.util.status.ItemSellStatus;
import com.example.mvcprac.validation.customize.EnumNamePattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ItemForm {

    private Long id;
    @NotBlank(message = "상품의 이름을 입력해 주세요")
    @Length(max = 50)
    private String itemName;

    @NotBlank(message = "상품의 상세내용을 입력해 주세요")
    private String itemBody;

    @NotNull(message = "상품의 가격을 입력해 주세요")
    @Range(min = 100)
    private int price;

    @EnumNamePattern(regexp = "SELL|SOLD", message = "상품의 재고 상태를 알려주세요")
    private ItemSellStatus itemSellStatus;
    @EnumNamePattern(regexp = "LOTTE|POST", message = "배송업체 롯데 또는 우체국을 선택해 주세요")
    private DeliveryStatus deliveryStatus;
    @NotNull(message = "최소한 1장의 이미지를 등록해 주세요")
    @Size(min = 1)
    private List<MultipartFile> imageFiles;
}
