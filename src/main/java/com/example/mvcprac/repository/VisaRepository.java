package com.example.mvcprac.repository;

import com.example.mvcprac.model.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface VisaRepository extends JpaRepository<Visa, Long> {
    List<Visa> findAllByOrderByCreatedAtDesc();
}
