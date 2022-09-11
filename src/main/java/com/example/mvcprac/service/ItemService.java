package com.example.mvcprac.service;

import com.example.mvcprac.dto.item.ItemDto;
import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ItemRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    /**
     * 아이템 하나만 조회
     */
    @Transactional(readOnly = true)
    public Long findById(Long id) throws NotFoundException {
        Item findItem = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시글 입니다")
        );
        return findItem.getId();
    }

    /**
     * 아이템 생성
     */
    @Transactional
    public Long createItem(ItemDto addDto) {
        Item item = new Item(addDto);
        Item saveItem = itemRepository.save(item);
        return saveItem.getId();
    }

    /**
     * 아이템 수정
     */
    @Transactional
    public void editItem(ItemDto editDto, Long id) throws NotFoundException {
        Item editItem = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시글 입니다")
        );
        Item item = new Item(editDto);
        itemRepository.save(item);
    }

    /**
     * 아이템 삭제
     */
    @Transactional
    public void delete(Long id) throws NotFoundException {
        Item findById = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시글 입니다")
        );
        itemRepository.delete(findById);
    }

    /**
     *  아이템 리스트 전체
     */
    public List<ItemListDto> findAll() {
        List<Item> findAll = itemRepository.findByAllItemIdOrderByIdAsc();
        ItemListDto listDto = modelMapper.map(findAll, ItemListDto.class);
        for (ItemListDto findAl : findAll) {
            log.info("deliveryBoard = {}", deliveryBoard);
        }
        return
    }
}
