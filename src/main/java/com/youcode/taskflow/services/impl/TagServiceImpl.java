package com.youcode.taskflow.services.impl;

import com.youcode.taskflow.dto.TagDto;
import com.youcode.taskflow.entities.Tag;
import com.youcode.taskflow.repository.TagRepository;
import com.youcode.taskflow.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    @Override
    public TagDto createTag(TagDto tagDto) {
        Tag tag = modelMapper.map(tagDto, Tag.class);
        tag = tagRepository.save(tag);
        if (tag.getId() != null){
            throw new RuntimeException("Error Creating tag");
        }
        return tagDto;
    }

    @Override
    public List<TagDto> getAllTags() {
        List<TagDto> list = new ArrayList<>();

        tagRepository.findAll().forEach(tag -> {
            list.add(modelMapper.map(tag, TagDto.class));
        });

        return list;
    }

    @Override
    public void deleteTag(Integer id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
        tagRepository.delete(tag);

    }
}
