package com.example.springbootdemo.services;

import com.example.springbootdemo.entities.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    Users getUserByEmail(String email);
    Users createUsers(Users user);
}
