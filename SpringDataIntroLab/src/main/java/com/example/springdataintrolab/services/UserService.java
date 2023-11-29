package com.example.springdataintrolab.services;

import com.example.springdataintrolab.models.User;

public interface UserService {
    void register(String username,int age);

    User findByUsername(String username);
}

