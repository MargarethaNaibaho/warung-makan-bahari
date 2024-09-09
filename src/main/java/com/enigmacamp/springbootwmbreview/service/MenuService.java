package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {
    Menu createNewMenu(Menu menu);
    Menu getMenuById(String id);
    List<Menu> getAllMenus(String name, Long minPrice, Long maxPrice);
    Menu updateMenu(Menu menu);
    void deleteMenuById(String id);
}
