package com.example.springbootdemo.services.impl;

import com.example.springbootdemo.entities.Categories;
import com.example.springbootdemo.entities.Countries;
import com.example.springbootdemo.entities.shopItems;
import com.example.springbootdemo.repositories.CategoryRepository;
import com.example.springbootdemo.repositories.CountryRepository;
import com.example.springbootdemo.repositories.ShopItemRepository;
import com.example.springbootdemo.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public shopItems addItem(shopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public List<shopItems> getAllItems() {
        return shopItemRepository.findAllByAmountGreaterThanOrderByPriceDesc(0);
    }

    @Override
    public shopItems getItem(Long id) {
        return shopItemRepository.findByIdAndAmountGreaterThan(id,0);
    }

    @Override
    public void deleteItem(shopItems item) {
        shopItemRepository.delete(item);
    }

    @Override
    public shopItems saveItem(shopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }
}
