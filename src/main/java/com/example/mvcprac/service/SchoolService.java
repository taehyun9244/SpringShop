package com.example.mvcprac.service;

import com.example.mvcprac.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
}
