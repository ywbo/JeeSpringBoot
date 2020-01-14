package com.springboot.jeenoob.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.AdminMenu;


public interface AdminMenuDAO extends JpaRepository<AdminMenu, Integer> {
     AdminMenu findById(int id);
     List<AdminMenu> findAllByParentId(int parentId);
}

