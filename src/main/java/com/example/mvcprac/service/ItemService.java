package com.example.mvcprac.service;

import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ItemRepository;
import com.example.mvcprac.repository.queryRepository.ItemQueryRepository;
import com.example.mvcprac.service.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final FileStore fileStore;


    /**
     * find all item of querydsl
     */
    @Transactional(readOnly = true)
    public List<ItemListDto> findAllItem() {
        List<Item> itemList = itemRepository.findAll();
        List<ItemListDto> itemListDto = itemList.stream()
                .map(item -> new ItemListDto(item))
                .collect(Collectors.toList());
        return itemListDto;
    }


    /**
     * findId item
     */
    @Transactional(readOnly = true)
    public ItemDetailDto findById(Long id) {

        Item item = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );

        ItemDetailDto itemDetailDto = new ItemDetailDto(item);
        return itemDetailDto;
    }



    /**
     * create item
     */
    @Transactional
    public Long createItem(ItemForm form) throws IOException {

        List<MultipartFile> imageFiles = form.getImageFiles();
        List<Image> storeImageFiles = fileStore.saveImages(imageFiles);

        Item item = new Item(form, storeImageFiles);
        Item saveItem = itemRepository.save(item);
        return saveItem.getId();
    }



}
