package com.example.mvcprac.service;

import com.example.mvcprac.model.Tag;
import com.example.mvcprac.repository.TagRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateNew(String tagTitle) {
        Tag tag = tagRepository.findByTitle(tagTitle);
        if (tag == null) {
            Tag saveTag = new Tag(tagTitle);
            tag = tagRepository.save(saveTag);
        }
        return tag;
    }
}
