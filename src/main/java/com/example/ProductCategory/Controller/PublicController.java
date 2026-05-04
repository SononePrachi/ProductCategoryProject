package com.example.ProductCategory.Controller;

import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;


    @GetMapping("/register")
    public String showRegistration(Model m)
    {
        m.addAttribute("user", new User());
       List<String> roles =List.of("USER","SELLER");
       m.addAttribute("roles",roles);
        return "registrationPage";
    }

    @PostMapping("/createUser")
    public String createUser(User u, RedirectAttributes ra)
    {
        userService.saveUser(u);

        ra.addFlashAttribute("msg",
                "User Created Successfully");

        return "redirect:/public/Login";
    }

    @GetMapping("/Login")
    public String loginUser()
    {
        return "LoginPage";
    }



}
