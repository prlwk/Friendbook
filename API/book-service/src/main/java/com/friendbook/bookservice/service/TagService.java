package com.friendbook.bookservice.service;

import java.util.List;

import com.friendbook.bookservice.DTO.TagForBook;
import com.friendbook.bookservice.model.Tag;

public interface TagService {
    TagForBook getTagById(Long id);
    List<TagForBook> getAllTags();
    Tag getTagByName(String name);
    List<TagForBook> getPopularTags();
}
