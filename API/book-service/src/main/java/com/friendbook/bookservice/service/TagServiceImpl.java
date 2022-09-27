package com.friendbook.bookservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.DTO.TagForBook;
import com.friendbook.bookservice.model.Tag;
import com.friendbook.bookservice.repository.TagRepository;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Override
    public TagForBook getTagById(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            return new TagForBook(optionalTag.get().getId(), optionalTag.get().getName());
        }
        throw new EntityNotFoundException("Tag not found.");
    }

    @Override
    public List<TagForBook> getAllTags() {
        List<TagForBook> tags = tagRepository.getAll();
        if (tags != null && !tags.isEmpty()) {
            return tags;
        }
        throw new EntityNotFoundException("Tags not found.");
    }
}
