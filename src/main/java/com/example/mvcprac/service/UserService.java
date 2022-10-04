//package com.example.mvcprac.service;
//
//import com.example.mvcprac.dto.user.EditDto;
//import com.example.mvcprac.dto.user.SignUpForm;
//import com.example.mvcprac.model.User;
//import com.example.mvcprac.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//@Slf4j
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    public User createUser(SignUpForm dto) {
//
//        log.info("user = {}", dto);
//
//        User findUsername = userRepository.findByEmail(dto.getEmail());
//        if (findUsername != null){
//            throw new RuntimeException("이미 등록된 이메일입니다");
//        }
//        User user = new User(dto);
//        User saveUser = userRepository.save(user);
//        return saveUser;
//    }
//
//    public User editUser(EditDto dto) {
//        return null;
//    }
//
//    public User deleteUser(Long id) {
//        return null;
//    }
//
//    public User findById(Long id) {
//        return null;
//    }
//
//    public List<User> findByAll() {
//        return null;
//    }
//}
