package com.example.Taupyk.lt.Repositories;

import com.example.Taupyk.lt.Models.CustomUser;
import com.example.Taupyk.lt.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String username);
}