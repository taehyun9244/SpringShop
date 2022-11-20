package com.example.mvcprac.repository;

import com.example.mvcprac.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {
    List<School> findAllByOrderByCreateAtDesc();
}