package com.example.mvcprac.service;

import com.example.mvcprac.dto.school.SchoolDetailDto;
import com.example.mvcprac.dto.school.SchoolListDto;
import com.example.mvcprac.model.School;
import com.example.mvcprac.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<SchoolListDto> findListSchool() {
        List<School> schools = schoolRepository.findAllByOrderByCreateAtDesc();
        List<SchoolListDto> schoolListDtos = schools.stream()
                .map(school -> new SchoolListDto(school))
                .collect(Collectors.toList());
        return schoolListDtos;
    }

    @Transactional(readOnly = true)
    public SchoolDetailDto findOneSchool(Long id) {
        School school = schoolRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        SchoolDetailDto schoolDetailDto =
                new SchoolDetailDto(school.getId(), school.getTitle(), school.getBody(), school.getSchoolName(),
                        school.getAccount().getNickname(), school.getCreateAt());
        return schoolDetailDto;
    }
}
