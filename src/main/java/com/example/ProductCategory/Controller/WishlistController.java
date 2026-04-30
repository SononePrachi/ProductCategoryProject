package com.example.ProductCategory.Controller;

import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Entity.Wishlist;
import com.example.ProductCategory.Service.ProductService;
import com.example.ProductCategory.Service.UserService;
import com.example.ProductCategory.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wish")
public class WishlistController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WishlistService wishlistService;


    @GetMapping("/my")
    public String showWishlist(Model m) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userService.getUserEntityByUsername(username);

        m.addAttribute("list",
                wishlistService.getByUser(user));

        return "wishlist";
    }


    @GetMapping("/add/{productId}")
    public String addWishlist(@PathVariable int productId) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userService.getUserEntityByUsername(username);

        Product product =
                productService.getProductEntityById(productId);

        Wishlist old =
                wishlistService.findByUserAndProduct(user, product);

        if(old == null){
            Wishlist w = new Wishlist();
            w.setUser(user);
            w.setProduct(product);
            wishlistService.save(w);
        }

        return "redirect:/product/getHomePage/0";
    }

    @GetMapping("/delete/{id}")
    public String removeWishlist(@PathVariable int id)
    {
        wishlistService.deleteById(id);

        return "redirect:/wish/my";
    }
}
