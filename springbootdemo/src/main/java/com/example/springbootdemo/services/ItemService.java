package com.example.springbootdemo.services;

import com.example.springbootdemo.entities.Categories;
import com.example.springbootdemo.entities.Countries;
import com.example.springbootdemo.entities.shopItems;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ItemService {
    shopItems addItem(shopItems item);
    List<shopItems>getAllItems();
    shopItems getItem(Long id);
    void deleteItem(shopItems item);
    shopItems saveItem(shopItems item);

    List<Countries>getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);

    List<Categories>getAllCategories();
    Categories getCategory(Long id);
    Categories saveCategory(Categories category);
    Categories addCategory(Categories category);

}
