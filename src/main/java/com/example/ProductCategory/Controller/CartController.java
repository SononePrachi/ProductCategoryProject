package com.example.ProductCategory.Controller;

import com.example.ProductCategory.DTO.CartDto;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.CartService;
import com.example.ProductCategory.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;


    // Add To Cart
    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable int productId)
    {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user =
                userService.getUserEntityByUsername(username);

        CartDto dto = new CartDto();

        dto.setUserId(user.getUid());
        dto.setProductId(productId);
        dto.setQuantity(1);

        cartService.saveCart(dto);

        return "redirect:/product/getHomePage/0";
    }

    // View Cart
    @GetMapping("/my")
    public String showCart(Model m) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user =
                userService.getUserEntityByUsername(username);

        m.addAttribute("list",
                cartService.getByUser(user));

        m.addAttribute("grandTotal",
                cartService.getGrandTotal(user));

        return "cart";
    }

    //Delete product From cart
    @GetMapping("/delete/{id}")
    public String removeCart(@PathVariable int id) {

        cartService.deleteById(id);

        return "redirect:/cart/my";
    }

    //To increase Quantity
    @GetMapping("/increase/{id}")
    public String increaseQuantity(@PathVariable int id) {

        cartService.increaseQty(id);

        return "redirect:/cart/my";
    }

    //To decrease Quantity
    @GetMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable int id) {

        cartService.decreaseQty(id);

        return "redirect:/cart/my";
    }


}
