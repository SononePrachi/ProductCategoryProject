package com.example.ProductCategory.Dao;

import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ProductDao extends JpaRepository<Product,Integer> {



        Page<Product> findByCategory_CategoryId(int id,Pageable pageable);

        Product findByProductName(String name);

       // SELECT * FROM product WHERE user_id = ?
        Page<Product> findByUser(User user, Pageable pageable);

        @Transactional
        void deleteByUserUid( Integer uid);


    @Query("select max(p.productPrice) from Product p")
    Double getHighestPrice();

    long countByUser(User user);

    @Query("select count(distinct p.category.categoryId) from Product p where p.user=:user")
    long countDistinctCategoryByUser(User user);

    @Query("select sum(p.productPrice) from Product p where p.user=:user")
    Double sumPriceByUser(User user);

}
