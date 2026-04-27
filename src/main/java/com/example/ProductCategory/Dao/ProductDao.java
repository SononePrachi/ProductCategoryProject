package com.example.ProductCategory.Dao;

import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ProductDao extends JpaRepository<Product,Integer> {



        Page<Product> findByCategory_CategoryId(int id,Pageable pageable);

        Product findByProductName(String name);

       // SELECT * FROM product WHERE user_id = ?
        Page<Product> findByUser(User user, Pageable pageable);

        @Transactional
        void deleteByUserUid( Integer uid);

}
