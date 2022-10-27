package com.example.mvcprac.service;

import com.example.mvcprac.dto.Tag.TagForm;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.repository.TagRepository;
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


    public Tag findTagTitle(TagForm tagForm) {

        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle);

        if (tag == null) {
            Tag saveTag = new Tag(tagForm.getTagTitle());
            tag = tagRepository.save(saveTag);
        }
        return tag;
    }

    public Tag removeTag(TagForm tagForm) {
        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle);

        return tag;
    }

    public List<String> findAllTagTitle() {
        List<String> findAllTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        return findAllTags;
    }
}
