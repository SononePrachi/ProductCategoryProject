package com.example.ProductCategory.Controller;

import com.example.ProductCategory.DTO.ProductDto;
import com.example.ProductCategory.DTO.UserDto;
import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.ProductService;
import com.example.ProductCategory.Service.UserService;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Entity;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;



    //GetAllProducts
    @GetMapping("/getAllProducts/{page}")
    public String getAllProducts(@PathVariable Integer page, Model m)
    {
        Pageable pageable = PageRequest.of(page,5);
        //offset=pageNumber*pageSize;
        Page<ProductDto> products=productService.fetchAllProduct(pageable);
        List<UserDto> users=userService.getAll();
        m.addAttribute("users",users);
        m.addAttribute("list", products.getContent());  // actual data
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", products.getTotalPages());
         return "AdminHomePage";

    }

}
