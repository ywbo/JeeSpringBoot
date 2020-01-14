package com.springboot.jeenoob.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.Book;
import com.springboot.jeenoob.pojo.Category;

public interface BookDAO extends JpaRepository<Book,Integer> {
    List<Book> findAllByCategory(Category category);
    List<Book> findAllByTitleLikeOrAuthorLike(String keyword1, String keyword2);
}
