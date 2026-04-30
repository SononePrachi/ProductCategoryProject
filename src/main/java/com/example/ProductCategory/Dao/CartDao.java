package com.example.ProductCategory.Dao;

import com.example.ProductCategory.Entity.Cart;
import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends JpaRepository<Cart,Integer> {

    List<Cart> findByUser(User user);

    Cart findByUserAndProduct(User user, Product product);
}
