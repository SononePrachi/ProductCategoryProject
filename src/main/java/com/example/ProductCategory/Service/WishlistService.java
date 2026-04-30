package com.example.ProductCategory.Service;

import com.example.ProductCategory.Dao.WishlistDao;
import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Entity.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistDao wishlistDao;

    public void save(Wishlist w) {
        wishlistDao.save(w);
    }

    public List<Wishlist> getByUser(User user) {
        return wishlistDao.findByUser(user);
    }

    public Wishlist findByUserAndProduct(User user, Product product) {
        return wishlistDao.findByUserAndProduct(user, product);
    }

    public void deleteById(int id)
    {
        wishlistDao.deleteById(id);
    }
}
