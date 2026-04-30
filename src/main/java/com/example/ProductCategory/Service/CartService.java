package com.example.ProductCategory.Service;

import com.example.ProductCategory.DTO.CartDto;
import com.example.ProductCategory.Dao.CartDao;
import com.example.ProductCategory.Entity.Cart;
import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartDao cartDao;

    public void saveCart(CartDto dto)
    {
        User user =
                userService.getUserEntityById(dto.getUserId());

        Product product =
                productService.getProductEntityById(dto.getProductId());

        Cart old =
                cartDao.findByUserAndProduct(user, product);

        if(old == null)
        {
            Cart cart = new Cart();

            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(dto.getQuantity());

            cartDao.save(cart);
        }
        else
        {
            old.setQuantity(old.getQuantity() + 1);

            cartDao.save(old);
        }
    }

    // View Cart
    public List<Cart> getByUser(User user) {
        return cartDao.findByUser(user);
    }

    //Delete from cart
    public void deleteById(int id) {
        cartDao.deleteById(id);
    }

    //To increase quantity
    public void increaseQty(int id) {

        Cart cart = cartDao.findById(id).orElse(null);

        if(cart != null) {
            cart.setQuantity(cart.getQuantity() + 1);
            cartDao.save(cart);
        }
    }

    //To decrease quantity
    public void decreaseQty(int id) {

        Cart cart = cartDao.findById(id).orElse(null);

        if(cart != null) {

            if(cart.getQuantity() > 1) {
                cart.setQuantity(cart.getQuantity() - 1);
                cartDao.save(cart);
            } else {
                cartDao.deleteById(id);
            }
        }
    }

    public double getGrandTotal(User user) {

        double total = 0;

        List<Cart> list = cartDao.findByUser(user);

        for(Cart c : list) {

            total = total +
                    (c.getProduct().getProductPrice() * c.getQuantity());
        }

        return total;
    }

    public void clearCart(User user) {

        List<Cart> list = cartDao.findByUser(user);

        cartDao.deleteAll(list);
    }
}
