package com.example.mvcprac.service;

import com.example.mvcprac.dto.visa.VisaDetailDto;
import com.example.mvcprac.dto.visa.VisaForm;
import com.example.mvcprac.dto.visa.VisaListDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Visa;
import com.example.mvcprac.repository.VisaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisaService {

    private final VisaRepository visaRepository;

    @Transactional(readOnly = true)
    public List<VisaListDto> findVisaList() {
        List<Visa> visas = visaRepository.findAllByOrderByCreatedAtDesc();
        List<VisaListDto> visaListDtos = visas.stream()
                .map(visa -> new VisaListDto(visa))
                .collect(Collectors.toList());
        return visaListDtos;
    }

    @Transactional(readOnly = true)
    public VisaDetailDto findOneVisa(Long id) {
        Visa visa = visaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        VisaDetailDto visaDetailDto =
                new VisaDetailDto(visa.getId(), visa.getTitle(), visa.getBody(), visa.getCountry(),
                        visa.getAccount().getNickname(), visa.getCreatedAt());
        return visaDetailDto;
    }

    public Long createVisa(VisaForm visaForm, Account account) {
        Visa visa = new Visa(visaForm, account);
        Visa saveVisa = visaRepository.save(visa);
        return saveVisa.getId();
    }


    public Long deleteVisa(Account account, Long id) {
        Visa findVisa = visaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if(findVisa.getAccount().equals(account)) {
            visaRepository.delete(findVisa);
        } else throw new IllegalArgumentException("게시글 작성자만이 삭제할 수 있습니다.");
        return findVisa.getId();
    }
}
