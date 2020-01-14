package com.springboot.jeenoob.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.jeenoob.dao.CategoryDAO;
import com.springboot.jeenoob.pojo.Category;

@Service
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    public Category get(int id) {
        Category c= categoryDAO.findById(id).orElse(null);
        return c;
    }
}
