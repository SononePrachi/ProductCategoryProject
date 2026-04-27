package com.example.ProductCategory.Dao;

import com.example.ProductCategory.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category,Integer> {
}
