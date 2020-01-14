package com.springboot.jeenoob.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.AdminRole;

public interface AdminRoleDAO extends JpaRepository<AdminRole, Integer> {
    AdminRole findById(int id);
}
