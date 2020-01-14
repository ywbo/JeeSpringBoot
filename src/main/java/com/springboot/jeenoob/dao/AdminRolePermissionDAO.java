package com.springboot.jeenoob.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.AdminRolePermission;

public interface AdminRolePermissionDAO extends JpaRepository<AdminRolePermission, Integer> {
    List<AdminRolePermission> findAllByRid(int rid);
    void deleteAllByRid(int rid);
}
