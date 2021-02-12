package com.example.springbootdemo.repositories;

import com.example.springbootdemo.entities.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users,Long> {
     Users findByEmail(String email);
}
