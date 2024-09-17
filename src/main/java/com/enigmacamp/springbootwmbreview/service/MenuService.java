package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.NewMenuRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingMenuRequest;
import com.enigmacamp.springbootwmbreview.dto.response.MenuResponse;
import com.enigmacamp.springbootwmbreview.entity.Menu;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {
    MenuResponse createNewMenu(NewMenuRequest newMenuRequest);
    Menu getMenuById(String id);
    Page<Menu> getAllMenus(PagingMenuRequest pagingMenuRequest);
    Menu updateMenu(Menu menu);
    void deleteMenuById(String id);
    Resource getMenuImageById(String id);
}
