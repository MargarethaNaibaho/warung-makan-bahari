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
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {
    @NonNull
    private MenuService menuService;

    @NonNull
    private ValidationUtil validationUtil;

    //consume ini digunakan untuk supaya si postmapping bisa terima dalam bentuk file
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //yg preauthorized ini bisa diletakkan juga di atas kelas untuk membatasi role nya untuk keseluruhan kelas
    @PreAuthorize("hasRole('ADMIN')") //ini untuk batasi akses berdasarkan rolenya. aku buat cuma ADMIN karna si spring dah pintar karna langsung baca setelah karakter role_
    public ResponseEntity<CommonResponse<MenuResponse>> createNewMenu(@RequestParam String name,
                                                                      @RequestParam Long price,
                                                                      @RequestParam MultipartFile image
    ){
        NewMenuRequest newMenuRequest = NewMenuRequest.builder()
                .name(name)
                .price(price)
                .multipartFile(image)
                .build();
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

    @GetMapping("/withPaging")
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

    @GetMapping()
    public ResponseEntity<?> getAllMenusWithoutPaging(){
        CommonResponse<List<MenuResponse>> response = CommonResponse.<List<MenuResponse>>builder()
                .message("Successfully get all customers")
                .statusCode(HttpStatus.OK.value())
                .data(menuService.getAllMenusWithoutPaging())
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

    //id menu, bukan menu image
    @GetMapping("/{id}/image")
    public ResponseEntity<?> downloadMenuImage(@PathVariable String id){
        Resource resource = menuService.getMenuImageById(id);

        //HTTP Header Response
        //ini si attachment maksudnya adalah konten harus diperlakukan sebagai lampiran yan perlu diunduh
        //ada juga inline yg gunanya untuk ditampikan di browser
        //filename ini maksudnya nama file yg akan didownload nanti
        String headerValues = "inline; attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity
                .status(HttpStatus.OK)

                //HttpHeaders.CONTENT_DISPOSITION untuk memberitahu bagaimana oknten dari response seharusnya diperlakukan oleh klien (seperti browser atau aplikasi)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValues)
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(resource);
    }
}
