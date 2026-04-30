package com.example.ProductCategory.Controller;

import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.CartService;
import com.example.ProductCategory.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping("/order/place")
    public String placeOrder(RedirectAttributes ra) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user =
                userService.getUserEntityByUsername(username);

        cartService.clearCart(user);

        ra.addFlashAttribute("msg",
                "✅ Order Placed Successfully");

        return "redirect:/cart/my";
    }
}
