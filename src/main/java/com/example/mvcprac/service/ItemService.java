package com.example.mvcprac.service;

import com.example.mvcprac.dto.item.ItemAddDto;
import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ItemRepository;
import com.example.mvcprac.repository.queryRepository.ItemQueryRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final ModelMapper modelMapper;

    /**
     *  아이템 리스트 전체
     */
    public Page<ItemListDto> findAll(Pageable pageable) {
        Page<ItemListDto> findAllItem = itemQueryRepository.findItemList(pageable);
        for (ItemListDto itemListDto : findAllItem){
            log.info("itemListDto", itemListDto);
        }
        return findAllItem;
    }

    /**
     * 아이템 하나만 조회
     */
    @Transactional(readOnly = true)
    public ItemDetailDto findById(Long id) throws NotFoundException {
        Item findItem = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시글 입니다")
        );
        ItemDetailDto itemDetailDto = modelMapper.map(findItem, ItemDetailDto.class);
        return itemDetailDto;
    }

    /**
     * 아이템 생성
     */
    @Transactional
    public Long createItem(ItemAddDto addDto) {
        Item item = new Item(addDto);
        Item saveItem = itemRepository.save(item);
        return saveItem.getId();
    }

    /**
     * 아이템 수정
     */
    @Transactional
    public Long editItem(ItemAddDto editDto, Long id) throws NotFoundException {
        Item findItem = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시글 입니다")
        );
        Item item = new Item(editDto);
        Item editItem = itemRepository.save(item);
        return editItem.getId();
    }

    /**
     * 아이템 삭제
     */
    @Transactional
    public void delete(Long id) throws NotFoundException {
        Item findItem = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시글 입니다")
        );
        itemRepository.delete(findItem);
    }
}
