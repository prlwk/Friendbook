package com.friendbook.bookservice.service;

import java.util.ArrayList;
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

    @Override
    public Tag getTagByName(String name) {
        Optional<Tag> tagOptional = tagRepository.getByName(name);
        if (tagOptional.isPresent()) {
            return tagOptional.get();
        }
        throw new EntityNotFoundException("Tag not found.");
    }

    @Override
    public List<TagForBook> getPopularTags() throws EntityNotFoundException{
        List<TagForBook> list = new ArrayList<>();
        Tag tag = getTagByName("Любовная история");
        list.add(new TagForBook(tag.getId(), tag.getName()));
        tag = getTagByName("Магия");
        list.add(new TagForBook(tag.getId(), tag.getName()));
        tag = getTagByName("Юмор");
        list.add(new TagForBook(tag.getId(), tag.getName()));
        tag = getTagByName("В поисках счастья");
        list.add(new TagForBook(tag.getId(), tag.getName()));
        tag = getTagByName("Путешествие");
        list.add(new TagForBook(tag.getId(), tag.getName()));
        tag = getTagByName("Война");
        list.add(new TagForBook(tag.getId(), tag.getName()));
        return list;
    }
}
