package com.example.mvcprac.service;

import com.example.mvcprac.dto.item.*;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ImageRepository;
import com.example.mvcprac.repository.ItemRepository;
import com.example.mvcprac.repository.query.ItemQueryRepository;
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
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemQueryRepository queryRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;



    public Page<ItemListDto> findAllItem(ItemSearchDto itemSearchDto, Pageable pageable) {
        return queryRepository.findAllItem(itemSearchDto, pageable);
    }

    public ItemDetailDto findById(Long id) {

        List<Image> findImageByItemId = imageRepository.findAllByItemIdOrderByCreatedAtAsc(id);
        List<ItemImageDto> imageDtoList = findImageByItemId.stream()
                .map(image -> new ItemImageDto(image))
                .collect(Collectors.toList());

        Item item = getItemId(id);
        ItemDetailDto itemDetailDto = new ItemDetailDto(item, imageDtoList);
        return itemDetailDto;
    }

    public Long createItem(ItemForm form, Account account) throws IOException {

        Item item = new Item(form, account);
        Item saveItem = itemRepository.save(item);

        List<MultipartFile> imageFiles = form.getImageFiles();
        fileStore.saveImages(imageFiles, saveItem);

        return saveItem.getId();
    }


    public Long editItem(Item findItem, ItemForm itemForm, Account account) throws IOException {
        Item item = new Item(findItem.getId(), itemForm, account);
        Item editItem = itemRepository.save(item);

        List<MultipartFile> imageFiles = itemForm.getImageFiles();
        fileStore.saveImages(imageFiles, editItem);

        return editItem.getId();
    }

    public void deleteItem(Long id) {
        Item item = getItemId(id);
        itemRepository.delete(item);
    }

    public Item getItemId(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );
        return item;
    }

    public void deleteImages(Item item) {
        List<Image> findAllImages = imageRepository.findAllByItemIdOrderByCreatedAtAsc(item.getId());
        imageRepository.deleteAll(findAllImages);
    }
}
