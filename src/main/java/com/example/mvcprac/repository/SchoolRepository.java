package com.example.mvcprac.repository;

import com.example.mvcprac.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<Long, School> {
}
