package com.example.ProductCategory.Service;

import com.example.ProductCategory.DTO.ProductDto;
import com.example.ProductCategory.Dao.ProductDao;

import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    // Add Product
    public void addProduct(Product p) {
        productDao.save(p);
    }

    // Read All Product DTO
    public Page<ProductDto> fetchAllProduct(Pageable pageable) {

        Page<Product> products = productDao.findAll(pageable);

        return products.map(this::convertToDTO);
    }

    // Read Product By Category DTO
    public Page<ProductDto> findAllProduct(int id, int page) {

        Pageable pageable = PageRequest.of(page, 5);

        Page<Product> products =
                productDao.findByCategory_CategoryId(id, pageable);

        return products.map(this::convertToDTO);
    }

    // Find By Name DTO
    public ProductDto findByName(String name) {

        Product p = productDao.findByProductName(name);

        if (p == null) {
            return null;
        }

        return convertToDTO(p);
    }

    // Read Product By Id DTO
    public ProductDto fetchById(int id) {

        Product p = productDao.findById(id).orElse(null);

        if (p == null) {
            return null;
        }

        return convertToDTO(p);
    }

    // Get Product Entity By Id (for update/delete)
    public Product getProductEntityById(int id) {

        return productDao.findById(id).orElse(null);
    }

    // Delete Product
    public void deleteById(int id) {

        productDao.deleteById(id);
    }

    // Get Products By User DTO
    public Page<ProductDto> getProductsByUser(User user, int page) {

        Pageable pageable = PageRequest.of(page, 5);

        Page<Product> products =
                productDao.findByUser(user, pageable);

        return products.map(this::convertToDTO);
    }

    // Delete By User Id
    public void deleteByUserUid(Integer uid) {

        productDao.deleteByUserUid(uid);
    }

    // Convert Entity To DTO
    private ProductDto convertToDTO(Product p) {

        ProductDto dto = new ProductDto();

        dto.setProductId(p.getProductId());
        dto.setProductName(p.getProductName());
        dto.setProductPrice(p.getProductPrice());

        if (p.getCategory() != null) {
            dto.setCategoryId(
                    p.getCategory().getCategoryId());
            dto.setCategoryName(
                    p.getCategory().getCategoryName());
        }

        if (p.getUser() != null) {
            dto.setUserId(
                    p.getUser().getUid());
            dto.setUsername(
                    p.getUser().getUsername());
        }

        return dto;
    }
}