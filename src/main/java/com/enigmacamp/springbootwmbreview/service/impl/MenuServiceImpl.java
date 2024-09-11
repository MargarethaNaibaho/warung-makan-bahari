package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.Menu;
import com.enigmacamp.springbootwmbreview.repository.MenuRepository;
import com.enigmacamp.springbootwmbreview.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Override
    public Menu createNewMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu getMenuById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Menu> getAllMenus(String name, Long minPrice, Long maxPrice) {
        if(name == null || name.isEmpty()){
            return menuRepository.findAll();
        }
        return menuRepository.findAllByNameLikeIgnoreCaseOrPriceBetween("%" + name + "%", minPrice, maxPrice);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        findByIdOrThrowNotFound(menu.getId());
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenuById(String id) {
        menuRepository.deleteById(id);
    }

    private Menu findByIdOrThrowNotFound(String id){
        //aku ganti si runtimeexception menjadi class error controller yg kubu
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu isn't found!"));
    }
}
