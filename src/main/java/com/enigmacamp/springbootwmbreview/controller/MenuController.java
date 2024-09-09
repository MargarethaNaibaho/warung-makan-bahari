package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.entity.Menu;
import com.enigmacamp.springbootwmbreview.service.MenuService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {
    @NonNull
    private MenuService menuService;

    @PostMapping()
    public Menu createNewMenu(@RequestBody Menu menu){
        return menuService.createNewMenu(menu);
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable String id){
        return menuService.getMenuById(id);
    }

    @GetMapping()
    public List<Menu> getAllMenus(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Long minPrice,
                                  @RequestParam(required = false) Long maxPrice){
        return menuService.getAllMenus(name, minPrice, maxPrice);
    }

    @PutMapping
    public Menu updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/{id}")
    public void deleteMenuById(@PathVariable String id){
        menuService.deleteMenuById(id);
    }

}
