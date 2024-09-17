package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.entity.Table;
import com.enigmacamp.springbootwmbreview.service.TableService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tables")
public class TableController {
    @NonNull
    private TableService tableService;

    @PostMapping()
    @SecurityRequirements
    public Table createNewTable(@RequestBody Table table){
        return tableService.createNewTable(table);
    }

    @GetMapping()
    @SecurityRequirement(name = "Bearer Authentication") //ini supaya sebelum akses API, perlu login. ini bisa diletakkan di atas kelas juga
//    public List<Table> getAllTables(@RequestParam(required = false) String name){
    public List<Table> getAllTables(){
        return tableService.getAllTables();
    }

    @GetMapping("/{name}")
    public Table findTableByName(@PathVariable String name){
        return tableService.getTableByName(name);
    }

    @PutMapping()
    public Table updateTable(@RequestBody Table table){
        return tableService.updateTable(table);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable String id){
        tableService.deleteTableById(id);
    }
}
