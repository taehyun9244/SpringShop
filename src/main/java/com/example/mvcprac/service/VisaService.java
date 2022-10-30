package com.example.mvcprac.service;

import com.example.mvcprac.dto.visa.VisaListDto;
import com.example.mvcprac.model.Visa;
import com.example.mvcprac.repository.VisaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisaService {

    private final VisaRepository visaRepository;

    public List<VisaListDto> findVisaList() {
        List<Visa> visas = visaRepository.findAllByOrderByCreatedAtDesc();
        List<VisaListDto> visaListDtos = visas.stream()
                .map(visa -> new VisaListDto(visa))
                .collect(Collectors.toList());
        return visaListDtos;
    }
}
