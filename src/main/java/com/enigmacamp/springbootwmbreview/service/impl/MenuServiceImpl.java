package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.NewMenuRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingMenuRequest;
import com.enigmacamp.springbootwmbreview.dto.response.MenuResponse;
import com.enigmacamp.springbootwmbreview.entity.Menu;
import com.enigmacamp.springbootwmbreview.repository.MenuRepository;
import com.enigmacamp.springbootwmbreview.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse createNewMenu(NewMenuRequest newMenuRequest) {
        Menu menu = Menu.builder()
                .name(newMenuRequest.getName())
                .price(newMenuRequest.getPrice())
                .build();
        menuRepository.save(menu);
        return MenuResponse.builder()
                .menuId(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }

    @Override
    public Menu getMenuById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public Page<Menu> getAllMenus(PagingMenuRequest pagingMenuRequest) {
        Pageable pageable = PageRequest.of(pagingMenuRequest.getPage() - 1, pagingMenuRequest.getSize());

        if((pagingMenuRequest.getName() == null || pagingMenuRequest.getName().isEmpty()) &&
                (pagingMenuRequest.getMinPrice() == null || pagingMenuRequest.getMinPrice().describeConstable().isEmpty()) &&
                (pagingMenuRequest.getMaxPrice() == null || pagingMenuRequest.getMaxPrice().describeConstable().isEmpty())){
            return menuRepository.findAll(pageable);
        } else if ((pagingMenuRequest.getName() == null || pagingMenuRequest.getName().isEmpty()) &&
                (pagingMenuRequest.getMaxPrice() == null || pagingMenuRequest.getMaxPrice().describeConstable().isEmpty()) ) {
            return menuRepository.findAllByPriceGreaterThanEqual(pagingMenuRequest.getMinPrice(), pageable);
        }

        String name = pagingMenuRequest.getName();
        Long minPrice = pagingMenuRequest.getMinPrice();
        Long maxPrice = pagingMenuRequest.getMaxPrice();
        return menuRepository.findAllByNameLikeIgnoreCaseOrPriceBetween("%" + name + "%", minPrice, maxPrice, pageable);
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
