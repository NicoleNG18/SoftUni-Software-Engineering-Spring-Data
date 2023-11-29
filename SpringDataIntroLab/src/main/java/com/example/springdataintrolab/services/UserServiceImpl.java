package com.example.springdataintrolab.services;

import com.example.springdataintrolab.models.Account;
import com.example.springdataintrolab.models.User;
import com.example.springdataintrolab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(String username,
                         int age) {
        //validate
        if (username.isBlank() || age < 18) {
            throw new RuntimeException("Validation failed!");
        }
        //check if the user is present
        Optional<User> isPresentUser = this.userRepository.findByUsername(username);
        if (isPresentUser.isPresent()) {
            throw new RuntimeException("User already in use!");
        }
       //try to add user
        Account account = new Account();
        User user = new User("first", 17, account);

        //save user
        this.userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).get();
    }
}

