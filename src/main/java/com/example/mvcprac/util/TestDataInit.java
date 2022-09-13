package com.example.mvcprac.util;


import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item(1L,"맥북", "신형 16인치 실버", 300000, "판매중", "우체국"));
        itemRepository.save(new Item(2L,"아이패드", "신형 12.9 스페이스 그레이", 200000, "품절", "CJ"));

    }
}
