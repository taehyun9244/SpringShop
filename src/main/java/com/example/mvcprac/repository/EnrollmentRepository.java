package com.example.mvcprac.repository;

import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Enrollment;
import com.example.mvcprac.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
