package com.example.springbootdemo.repositories;

import com.example.springbootdemo.entities.shopItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ShopItemRepository extends JpaRepository<shopItems,Long> {
       List<shopItems>findAllByAmountGreaterThanOrderByPriceDesc(int amount);
       shopItems findByIdAndAmountGreaterThan(Long id,int amount);
}
