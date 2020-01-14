package com.springboot.jeenoob.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jeenoob.pojo.AdminRoleMenu;

public interface AdminRoleMenuDAO extends JpaRepository<AdminRoleMenu,Integer> {
    List<AdminRoleMenu> findAllByRid(int rid);
    void deleteAllByRid(int rid);
}
