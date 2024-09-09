package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.entity.Table;
import com.enigmacamp.springbootwmbreview.service.TableService;
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
    public Table createNewTable(@RequestBody Table table){
        return tableService.createNewTable(table);
    }

    @GetMapping()
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
