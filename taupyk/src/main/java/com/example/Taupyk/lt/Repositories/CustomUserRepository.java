package com.example.Taupyk.lt.Repositories;

import com.example.Taupyk.lt.Models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findCustomUserByName(String username);
}
