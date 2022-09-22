package com.example.mvcprac.repository;

import com.example.mvcprac.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByItemIdOrderByIdAsc(Long id);
}
