package com.springboot.jeenoob.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.AdminUserRole;

public interface AdminUserRoleDAO extends JpaRepository<AdminUserRole,Integer> {
    List<AdminUserRole> findAllByUid(int uid);
    void deleteAllByUid(int uid);
}
