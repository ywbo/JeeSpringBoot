package com.springboot.jeenoob.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.AdminPermission;

public interface AdminPermissionDAO extends JpaRepository<AdminPermission, Integer> {
    AdminPermission findById(int id);
}
