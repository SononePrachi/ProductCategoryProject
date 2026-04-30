package com.example.ProductCategory.Dao;

import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistDao extends JpaRepository<Wishlist,Integer> {
    List<Wishlist> findByUser(User user);

    Wishlist findByUserAndProduct(User user, Product product);
}
