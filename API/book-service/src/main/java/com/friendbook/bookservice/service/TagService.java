package com.friendbook.bookservice.service;

import java.util.List;

import com.friendbook.bookservice.DTO.TagForBook;

public interface TagService {
    public TagForBook getTagById(Long id);
    public List<TagForBook> getAllTags();
}
