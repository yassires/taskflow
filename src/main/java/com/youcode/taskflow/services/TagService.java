package com.youcode.taskflow.services;

import com.youcode.taskflow.dto.TagDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {

    TagDto createTag(TagDto tagDto);
    List<TagDto> getAllTags();
    void deleteTag(Integer id);
}
