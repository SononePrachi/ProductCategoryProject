package com.example.ProductCategory.Controller;


import com.example.ProductCategory.DTO.UserDto;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.ProductService;
import com.example.ProductCategory.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get All Users
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    // Get User By Id
    @GetMapping("/getUserById/{uid}")
    @ResponseBody
    public UserDto getUserById(@PathVariable Integer uid) {
        return userService.getUserById(uid);
    }

    // Show Update User
    @GetMapping("/showUpdateUser/{uid}")
    public String showUpdateUser(@PathVariable int uid, Model m) {

        UserDto u = userService.getUserById(uid);

        m.addAttribute("user", u);

        return "updateUser";
    }

    // Update User
    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User u, Model m) {

        UserDto dto = userService.getUserById(u.getUid());

        User u1 = new User();
        u1.setUid(dto.getUid());
        u1.setUsername(u.getUsername());
        u1.setRole(dto.getRole());

        if (u.getPassword() == null || u.getPassword().isEmpty()) {
            User oldUser = userService.getUserEntityById(u.getUid());
            u1.setPassword(oldUser.getPassword());
        } else {
            u1.setPassword(passwordEncoder.encode(u.getPassword()));
        }

        userService.saveUser(u1);

        m.addAttribute("msg", "User Updated Successfully");

        return "success";
    }

    // Delete User
    @PostMapping("/delete/{uid}")
    public String deleteUser(@PathVariable Integer uid) {

        productService.deleteByUserUid(uid);
        userService.deleteUser(uid);

        return "redirect:/admin/getAllProducts/0";
    }

    // Save User
    @PostMapping("/save")
    public String saveUser(User u) {

        userService.saveUser(u);

        return "redirect:/Public/Login";
    }

}