//package com.example.mvcprac.util;
//
//
//import com.example.mvcprac.model.Item;
//import com.example.mvcprac.repository.ItemRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@RequiredArgsConstructor
//public class TestDataInit {
//
//    private final ItemRepository itemRepository;
//
//    /**
//     * 테스트용 데이터 추가
//     */
//    @PostConstruct
//    public void init() {
//        itemRepository.save(new Item(1L,"맥북", "신형 16인치 실버", 300000, "판매중", "우체국", null));
//        itemRepository.save(new Item(2L,"아이패드", "신형 12.9 스페이스 그레이", 200000, "품절", "CJ", null));
//        itemRepository.save(new Item(3L,"아이폰14", "프로맥스 최신", 100000, "판매중", "롯데", null));
//        itemRepository.save(new Item(4L,"게이밍 컴퓨터", "중고 감안 싸게 함", 50000, "판매중", "CJ", null));
//        itemRepository.save(new Item(5L,"삼성 갤럭시", "신형 삼성 갤럭시", 100000, "품절", "CJ", null));
//
//    }
//}
