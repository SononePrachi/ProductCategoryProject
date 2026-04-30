package com.example.ProductCategory.Controller;

import com.example.ProductCategory.DTO.CategoryDto;

import com.example.ProductCategory.DTO.ProductDto;

import com.example.ProductCategory.DTO.UserDto;
import com.example.ProductCategory.Entity.Category;
import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import com.example.ProductCategory.Service.CategoryService;
import com.example.ProductCategory.Service.ProductService;
import com.example.ProductCategory.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/product/getHomePage/0";
    }

    // Home Page
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getHomePage/{page}")
    public String getAllProduct(@PathVariable Integer page, Model m) {

        Pageable pageable = PageRequest.of(page, 5);

        Page<ProductDto> list = productService.fetchAllProduct(pageable);
        List<CategoryDto> categories = categoryService.fetchAll();

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserDto user = userService.findByUserName(username);

        m.addAttribute("userId", user.getUid());
        m.addAttribute("categories", categories);
        m.addAttribute("products", list.getContent());
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", list.getTotalPages());

        return "home";
    }

    // Show Add Form
    @GetMapping("/showAddProduct/{page}")
    public String showAddForm(Model m, @PathVariable int page) {

        m.addAttribute("product", new Product());

        List<CategoryDto> category = categoryService.fetchAll();

        m.addAttribute("category", category);
        m.addAttribute("page", page);

        return "addForm";
    }

    // Create Product
    @GetMapping("/addProduct/{page}")
    public String createProduct(@ModelAttribute Product p,
                                @PathVariable int page) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userService.getUserEntityByUsername(username);

        int id = p.getCategory().getCategoryId();

        Category c1 = categoryService.getCategoryEntityById(id);

        if (c1 != null) {
            p.setCategory(c1);
            p.setUser(user);

            productService.addProduct(p);
        }

        return "redirect:/product/showMyProduct/" + page;
    }

    // Fetch Product By Id
    @GetMapping("/fetchProduct/{id}")
    public String getProduct(@PathVariable int id, Model m) {

        ProductDto p = productService.fetchById(id);

        m.addAttribute("singleProduct", p);

        return "showProduct";
    }

    // Category Wise Filter
    @GetMapping("/fetchByCategoryWise")
    public String fetchAllByCategory(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            Model m) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String role =
                auth.getAuthorities().iterator().next().getAuthority();

        Page<ProductDto> products;

        if (categoryId != null) {
            products = productService.findAllProduct(categoryId, page);
        } else {
            products = productService.fetchAllProduct(
                    PageRequest.of(page, 5));
        }
        m.addAttribute("isFiltered", true);

        m.addAttribute("products", products.getContent());
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", products.getTotalPages());
        m.addAttribute("categoryId", categoryId);
        m.addAttribute("categories", categoryService.fetchAll());

        if (role.equals("ROLE_SELLER")) {
            return "SellerProducts";
        } else {
            return "home";
        }
    }

    // Search By Product Name
    @GetMapping("/fetchByProductName")
    public String fetchByName(@RequestParam String productName,
                              Model m) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String role =
                auth.getAuthorities().iterator().next().getAuthority();

        ProductDto p = productService.findByName(productName);


        m.addAttribute("products", java.util.List.of(p));


        if (role.equals("ROLE_SELLER")) {
            return "SellerProducts";
        } else {
            return "home";
        }
    }

    // Show Edit Form
    @GetMapping("/showEditForm/{id}/{page}")
    public String editForm(@PathVariable int id,
                           @PathVariable int page,
                           Model m) {

        ProductDto p = productService.fetchById(id);

        m.addAttribute("product", p);
        m.addAttribute("category", categoryService.fetchAll());
        m.addAttribute("page", page);

        return "edit";
    }

    // Update Product
    @PostMapping("/update/{id}/{page}")
    public String updateProduct(@PathVariable int id,
                                @PathVariable int page,
                                @ModelAttribute Product p) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userService.getUserEntityByUsername(username);

        Product oldProduct =
                productService.getProductEntityById(id);

        if (!oldProduct.getUser().getUid().equals(user.getUid())
                && !user.getRole().contains("ADMIN")) {

            return "redirect:/product/getHomePage/0";
        }

        oldProduct.setProductName(p.getProductName());
        oldProduct.setProductPrice(p.getProductPrice());
        oldProduct.setCategory(
                categoryService.getCategoryEntityById(
                        p.getCategory().getCategoryId()));

        productService.addProduct(oldProduct);

        if (user.getRole().contains("ADMIN")) {
            return "redirect:/admin/getAllProducts/0";
        }

        return "redirect:/product/showMyProduct/" + page;
    }

    // Delete Product
    @PostMapping("/delete/{id}/{page}")
    public String deleteProduct(@PathVariable int id,
                                @PathVariable int page) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userService.getUserEntityByUsername(username);

        Product oldProduct =
                productService.getProductEntityById(id);

        if (!oldProduct.getUser().getUid().equals(user.getUid())
                && !user.getRole().contains("ADMIN")) {

            return "redirect:/product/getHomePage/0";
        }

        productService.deleteById(id);

        if (user.getRole().contains("ADMIN")) {
            return "redirect:/admin/getAllProducts/0";
        }

        return "redirect:/product/showMyProduct/" + page;
    }

    // My Products
    @GetMapping("/showMyProduct/{page}")
    public String getMyProduct(@PathVariable int page, Model m) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userService.getUserEntityByUsername(username);

        if (user.getRole().contains("SELLER")) {

            Page<ProductDto> productPage =
                    productService.getProductsByUser(user, page);

            m.addAttribute("categories",
                    categoryService.fetchAll());

            m.addAttribute("products",
                    productPage.getContent());

            m.addAttribute("currentPage", page);
            m.addAttribute("totalPages",
                    productPage.getTotalPages());
            m.addAttribute("myTotalProducts",
                    productService.getMyTotalProducts(user));

            m.addAttribute("myCategories",
                    productService.getMyTotalCategories(user));

            m.addAttribute("myTotalValue",
                    productService.getMyTotalValue(user));

            return "SellerProducts";
        }

        return "redirect:/product/getHomePage/0";
    }
}