package com.springboot.jeenoob.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.Category;


public interface CategoryDAO extends JpaRepository<Category, Integer> {

}
