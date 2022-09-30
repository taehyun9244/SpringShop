package com.example.mvcprac.service;

import com.example.mvcprac.dto.item.*;
import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ImageRepository;
import com.example.mvcprac.repository.ItemQueryRepository;
import com.example.mvcprac.repository.ItemRepository;
import com.example.mvcprac.service.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ItemQueryRepository queryRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;


    /**
     * find all item
     */

    @Transactional(readOnly = true)

    public Page<ItemListDto> findAllItem(ItemSearchDto itemSearchDto, Pageable pageable) {
        return queryRepository.findAllItem(itemSearchDto, pageable);
    }

    /**
     * findById item
     */

    @Transactional(readOnly = true)
    public ItemDetailDto findById(Long id) {

        List<Image> findImageByItemId = imageRepository.findAllByItemIdOrderByCreatedAtAsc(id);
        List<ItemImageDto> imageDtoList = findImageByItemId.stream()
                .map(image -> new ItemImageDto(image))
                .collect(Collectors.toList());

        Item item = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );

        ItemDetailDto itemDetailDto = new ItemDetailDto(item, imageDtoList);
        return itemDetailDto;
    }


    /**
     * create item
     */
    @Transactional
    public Long createItem(ItemForm form) throws IOException {

        Item item = new Item(form);
        Item saveItem = itemRepository.save(item);

        List<MultipartFile> imageFiles = form.getImageFiles();
        List<Image> images = fileStore.saveImages(imageFiles, saveItem);
        imageRepository.saveAll(images);

        return saveItem.getId();
    }


    /**
     *  editItem
     */
//    @Transactional
//    public Long edit(ItemForm form, Long id) {
//        Item item = itemRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
//        );
//    }

    /**
     * deleteItem
     */
    @Transactional
    public void deleteItem(Long id) {

        Item item = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );
        itemRepository.delete(item);
    }
}
