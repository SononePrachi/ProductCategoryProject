package com.example.ProductCategory.Controller;

import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    //Create
    @PostMapping("/createUser")
    public String createUser(User u,Model m)
    {
        userService.saveUser(u);
        m.addAttribute("msg","User Created Successfully");
        return "success";
    }

    @GetMapping("/Login")
    public String loginUser()
    {
        return "LoginPage";
    }



}
