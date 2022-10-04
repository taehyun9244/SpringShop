//package com.example.mvcprac.repository;
//
//import com.example.mvcprac.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.transaction.annotation.Transactional;
//
//@Transactional(readOnly = true)
//public interface UserRepository extends JpaRepository<User, Long> {
//    User findByEmail(String username);
//
//    boolean existsByEmail(String email);
//
//    boolean existsByNickname(String nickname);
//
//    boolean existsByPhoneNumber(String phoneNumber);
//}
