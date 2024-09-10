package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.Table;

import java.util.List;

public interface TableService {
    Table createNewTable(Table table);
    List<Table> getAllTables();
    Table getTableByName(String name);
    Table getTableById(String id);
    Table updateTable(Table table);
    void deleteTableById(String id);
}
