package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.request.NewMenuRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingMenuRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CommonResponse;
import com.enigmacamp.springbootwmbreview.dto.response.MenuResponse;
import com.enigmacamp.springbootwmbreview.dto.response.PagingResponse;
import com.enigmacamp.springbootwmbreview.entity.Menu;
import com.enigmacamp.springbootwmbreview.service.MenuService;
import com.enigmacamp.springbootwmbreview.util.ValidationUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {
    @NonNull
    private MenuService menuService;

    @NonNull
    private ValidationUtil validationUtil;

    @PostMapping()
    public ResponseEntity<CommonResponse<MenuResponse>> createNewMenu(@RequestBody NewMenuRequest newMenuRequest){
        validationUtil.validate(newMenuRequest);
        MenuResponse menuResponse = menuService.createNewMenu(newMenuRequest);
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .message("Successfully created new menu")
                .statusCode(HttpStatus.CREATED.value())
                .data(menuResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable String id){
        return menuService.getMenuById(id);
    }

    @GetMapping()
    public ResponseEntity<?> getAllMenus(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) Long minPrice,
                                         @RequestParam(required = false) Long maxPrice,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "5") Integer size){

        PagingMenuRequest pagingMenuRequest = PagingMenuRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .build();
        Page<Menu> menus = menuService.getAllMenus(pagingMenuRequest);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(menus.getTotalElements())
                .totalPages(menus.getTotalPages())
                .build();

        CommonResponse<List<Menu>> response = CommonResponse.<List<Menu>>builder()
                .message("Successfully get all customers")
                .statusCode(HttpStatus.OK.value())
                .data(menus.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
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
