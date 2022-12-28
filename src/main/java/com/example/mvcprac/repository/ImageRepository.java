package com.example.mvcprac.repository;

import com.example.mvcprac.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByItemIdOrderByCreatedAtAsc(Long id);
}
