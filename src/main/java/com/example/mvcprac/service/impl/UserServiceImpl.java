package com.example.mvcprac.service.impl;

import com.example.mvcprac.dto.user.EditDto;
import com.example.mvcprac.dto.user.SignupDto;
import com.example.mvcprac.model.User;
import com.example.mvcprac.repository.UserRepository;
import com.example.mvcprac.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(SignupDto dto) {

        log.info("user = {}", dto);

        User findUsername = userRepository.findByEmail(dto.getEmail());
        if (findUsername != null){
            throw new RuntimeException("이미 등록된 이메일입니다");
        }
        User user = new User(dto);
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public User editUser(EditDto dto) {
        return null;
    }

    @Override
    public User deleteUser(Long id) {
        return null;
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public List<User> findByAll() {
        return null;
    }
}
