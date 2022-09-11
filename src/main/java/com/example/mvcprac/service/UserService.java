package com.example.mvcprac.service;

import com.example.mvcprac.dto.user.EditDto;
import com.example.mvcprac.dto.user.SignupDto;
import com.example.mvcprac.model.User;

import java.util.List;

public interface UserService {

    User createUser(SignupDto dto);
    User editUser(EditDto dto);
    User deleteUser(Long id);
    User findById(Long id);
    List<User> findByAll();
}
