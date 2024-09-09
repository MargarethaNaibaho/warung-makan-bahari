package com.enigmacamp.springbootwmbreview.repository;

import com.enigmacamp.springbootwmbreview.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, String> {
    public List<Menu> findAllByNameLikeIgnoreCaseOrPriceBetween(String name, Long minPrice, Long maxPrice);
}
