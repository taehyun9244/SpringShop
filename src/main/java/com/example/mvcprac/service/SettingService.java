package com.example.mvcprac.service;

import com.example.mvcprac.dto.tag.TagRegisterDto;
import com.example.mvcprac.dto.zone.ZoneRegisterDto;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.example.mvcprac.repository.TagRepository;
import com.example.mvcprac.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettingService {

    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;

    public List<String> findAllTagTitle() {
        List<String> findAllTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        return findAllTags;
    }

    public Tag findTagTitle(TagRegisterDto tagForm) {

        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle);

        if (tag == null) {
            Tag saveTag = new Tag(tagForm.getTagTitle());
            tag = tagRepository.save(saveTag);
        }
        return tag;
    }

    public Tag removeTag(TagRegisterDto tagForm) {
        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle);

        return tag;
    }


    public List<String> findAllZones() {
        List<String> findALlZones = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        return findALlZones;
    }


    public Zone addZone(ZoneRegisterDto zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        log.info("zone ={}", zone);

        if (zone == null) {
            throw new IllegalArgumentException("시스템에 등록된 지역만 등록하실 수 있습니다");
        }
        return zone;
    }

    public Zone removeZone(ZoneRegisterDto zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());

        return zone;
    }
}
