package com.example.mvcprac.service;

import com.example.mvcprac.dto.item.ItemAddDto;
import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ItemRepository;
import com.example.mvcprac.repository.queryRepository.ItemQueryRepository;
import com.example.mvcprac.service.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ModelMapper modelMapper;

    /**
     * 아이템 리스트 전체
     */
    @Transactional(readOnly = true)
    public List<ItemListDto> findAll() {
        List<Item> itemAll = itemRepository.findAll();
        List<ItemListDto> itemListDtos = itemAll.stream()
                .map(item -> new ItemListDto(item))
                .collect(Collectors.toList());
        return itemListDtos;
    }

    /**
     * 아이템 하나만 조회
     */
    @Transactional(readOnly = true)
    public ItemDetailDto findById(Long id) {
        Item findItem = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다")
        );
        ItemDetailDto itemDetailDto = new ItemDetailDto(findItem);
        return itemDetailDto;
    }

    /**
     * 아이템 생성
     */
    @Transactional
    public Long createItem(ItemAddDto addDto) throws IOException {

        List<Image> saveImages = fileStore.saveImages(addDto.getImageFiles());
        log.info("saveImages = {}", saveImages);

        Item item = new Item(addDto, saveImages);
        Item saveItem = itemRepository.save(item);
        log.info("saveItem = {}", saveItem);

        return saveItem.getId();
    }


    /**
     * 아이템 수정
     */
    @Transactional
    public Long editItem(ItemAddDto editDto, Long id) throws IOException {

        itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다")
        );

        List<Image> editImages = fileStore.saveImages(editDto.getImageFiles());

        Item item = new Item(editDto, editImages);
        Item editItem = itemRepository.save(item);
        return editItem.getId();
    }

    /**
     * 아이템 삭제
     */
    @Transactional
    public void delete(Long id) {
        Item findItem = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다")
        );
        itemRepository.delete(findItem);
    }
}
