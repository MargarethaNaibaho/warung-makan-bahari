package com.enigmacamp.springbootwmbreview.repository;

import com.enigmacamp.springbootwmbreview.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, String> {
//    public List<Table> findAllByName(String name);
    public Optional<Table> findByName(String name);
}
