package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.Table;
import com.enigmacamp.springbootwmbreview.repository.TableRepository;
import com.enigmacamp.springbootwmbreview.service.TableService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    @NonNull
    private TableRepository tableRepository;

    @Override
    public Table createNewTable(Table table) {
        try{
            return tableRepository.save(table);
        } catch(DataIntegrityViolationException e){
            throw new RuntimeException("Table name already exists");
        }
    }

    @Override
//    public List<Table> getAllTables(String name) {
    public List<Table> getAllTables() {
//        if(name == null || name.isEmpty()){
//            return tableRepository.findAll();
//        }
//        return tableRepository.findAllByName(name);
        return tableRepository.findAll();
    }

    @Override
    public Table getTableByName(String name) {
        return findByNameOrThrowNotFound(name);
    }

    @Override
    public Table updateTable(Table table) {
        try{
            findByIdOrThrowNotFound(table.getId());
            return tableRepository.save(table);
        } catch(DataIntegrityViolationException e){
            throw new RuntimeException("Table name already exists!");
        }

    }

    @Override
    public void deleteTableById(String id) {
        tableRepository.deleteById(id);
    }

    private Table findByIdOrThrowNotFound(String id){
        return tableRepository.findById(id).orElseThrow(() -> new RuntimeException("Table name isn't found!"));
    }

    private Table findByNameOrThrowNotFound(String name) {
        return tableRepository.findByName(name).orElseThrow(() -> new RuntimeException("Table name isn't found!"));
    }
}
