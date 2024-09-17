package com.enigmacamp.springbootwmbreview.repository;

import com.enigmacamp.springbootwmbreview.entity.MenuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuImageRepository extends JpaRepository<MenuImage, String> {
}
