package com.example.ProductCategory.Service;

import com.example.ProductCategory.DTO.CategoryDto;
import com.example.ProductCategory.Dao.CategoryDao;
import com.example.ProductCategory.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    // Add Category
    public void saveCategory(Category c) {
        categoryDao.save(c);
    }

    //get category count
    public long getCount() {
        return categoryDao.count();
    }

    // Get All Categories DTO
    public List<CategoryDto> fetchAll() {

        List<Category> categories = categoryDao.findAll();
        List<CategoryDto> dtoList = new ArrayList<>();

        for (Category c : categories) {

            CategoryDto dto = new CategoryDto();

            dto.setCategoryId(c.getCategoryId());
            dto.setCategoryName(c.getCategoryName());

            dtoList.add(dto);
        }

        return dtoList;
    }

    // Get Category By Id DTO
    public CategoryDto getById(int id) {

        Category c = categoryDao.findById(id).orElse(null);

        if (c == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();

        dto.setCategoryId(c.getCategoryId());
        dto.setCategoryName(c.getCategoryName());

        return dto;
    }

    // Get Entity By Id (for update / internal use)
    public Category getCategoryEntityById(int id) {
        return categoryDao.findById(id).orElse(null);
    }

    // Delete Category
    public void delete(int id) {
        categoryDao.deleteById(id);
    }
}