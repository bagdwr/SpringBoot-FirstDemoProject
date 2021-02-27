package com.example.springbootdemo.services.impl;

import com.example.springbootdemo.entities.Roles;
import com.example.springbootdemo.entities.Users;
import com.example.springbootdemo.repositories.RoleRepository;
import com.example.springbootdemo.repositories.UsersRepository;
import com.example.springbootdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) {
        Users myUser=usersRepository.findByEmail(s);
        if (myUser!=null){
             User secUser=new User(myUser.getEmail(),myUser.getPassword(),myUser.getRoles());
             return secUser;
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users createUsers(Users user) {
        Users checkUser=usersRepository.findByEmail(user.getEmail());
        if (checkUser==null){
           Roles role=roleRepository.findByRole("ROLE_USER");
           if (role!=null){
               ArrayList<Roles>roles=new ArrayList<>();
               roles.add(role);
               user.setRoles(roles);
               user.setPassword(passwordEncoder.encode(user.getPassword()));
               return usersRepository.save(user);
           }
        }
        return null;
    }
}
