package com.example.ProductCategory.Controller;

import com.example.ProductCategory.DTO.CategoryDto;
import com.example.ProductCategory.Entity.Category;
import com.example.ProductCategory.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Create
    @PostMapping("/addCategory")
    public void addCategory(@RequestBody Category c) {
        categoryService.saveCategory(c);
    }

    // Read All
    @GetMapping("/getAll")
    public List<CategoryDto> fetchAllCategory() {

        return categoryService.fetchAll();
    }

    // Read By Id
    @GetMapping("/fetchById/{id}")
    public CategoryDto fetchById(@PathVariable int id) {

        return categoryService.getById(id);
    }

    // Update By Id
    @PutMapping("/update/{id}")
    public CategoryDto updateById(@PathVariable int id,
                                  @RequestBody Category c) {

        Category c1 = categoryService.getCategoryEntityById(id);

        c1.setCategoryName(c.getCategoryName());

        categoryService.saveCategory(c1);

        return categoryService.getById(id);
    }

    // Delete By Id
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable int id) {

        categoryService.delete(id);
    }
}