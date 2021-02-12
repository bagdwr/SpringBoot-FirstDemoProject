package com.example.springbootdemo.repositories;

import com.example.springbootdemo.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories,Long> {
}
