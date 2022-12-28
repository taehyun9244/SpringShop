package com.example.mvcprac.repository;

import com.example.mvcprac.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)

public interface ItemRepository extends JpaRepository<Item, Long> {
}
