package com.devmarrima.DMcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devmarrima.DMcatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
