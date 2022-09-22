package com.example.mvcprac.service;

import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;
    private Item item;

    @BeforeEach
    public void setUp() throws IOException {

        MockMultipartFile file = new MockMultipartFile("image",
                "test.png",
                "image/png",
                new FileInputStream("/Users/taehyunnam/study"));

        item = new Item(1L, "macBook", "new", 1000, "SELL", "POST", (List<Image>) file);
    }

    @Test
    @DisplayName("아이템 생성 + 이미지")
    void createItem() {


    }


}