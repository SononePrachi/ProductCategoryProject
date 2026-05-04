package com.example.ProductCategory.Service;

import com.example.ProductCategory.DTO.ProductDto;
import com.example.ProductCategory.Dao.ProductDao;

import com.example.ProductCategory.Entity.Product;
import com.example.ProductCategory.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.PageImpl;

import java.time.Duration;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // Add Product
    public void addProduct(Product p) {
        productDao.save(p);

        //  Clear search cache
        redisTemplate.delete("product:name:" + p.getProductName().toLowerCase());

        //  Clear category cache
        Set<String> keys = redisTemplate.keys("product:category:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        System.out.println("Cache cleared for add");
    }

    // Read All Product DTO
    public Page<ProductDto> fetchAllProduct(Pageable pageable) {

        Page<Product> products = productDao.findAll(pageable);

        return products.map(this::convertToDTO);
    }

    // Read Product By Category DTO
    public Page<ProductDto> findAllProduct(int id, int page) {

        String key = "product:category:" + id + ":page:" + page;

        Pageable pageable = PageRequest.of(page, 5);

        // STEP 1: Check Redis
        List<ProductDto> cachedList =
                (List<ProductDto>) redisTemplate.opsForValue().get(key);

        if (cachedList != null) {
            System.out.println("From REDIS");
            return new PageImpl<>(cachedList, pageable, cachedList.size());
        }

        // STEP 2: DB call
        Page<Product> products =
                productDao.findByCategory_CategoryId(id, pageable);

        List<ProductDto> dtoList = products
                .getContent()
                .stream()
                .map(this::convertToDTO)
                .toList();

        // STEP 3: Store in Redis
        redisTemplate.opsForValue()
                .set(key, dtoList, Duration.ofMinutes(10));

        System.out.println("From DATABASE");

        return new PageImpl<>(dtoList, pageable, products.getTotalElements());
    }
    // Find By Name DTO
    public ProductDto findByName(String name) {

        String key = "product:name:" + name.toLowerCase();

        // STEP 1: Check Redis
        ProductDto cachedDto = (ProductDto) redisTemplate.opsForValue().get(key);

        if (cachedDto != null) {
            System.out.println("From REDIS");
            return cachedDto;
        }

        //  STEP 2: Fetch from DB
        System.out.println(" From DATABASE");

        Product p = productDao.findByProductName(name);

        if (p == null) {
            return null;
        }

        ProductDto dto = convertToDTO(p);

        // STEP 3: Store in Redis (with TTL)
        redisTemplate.opsForValue()
                .set(key, dto, Duration.ofMinutes(10));

        return dto;
    }

    // Read Product By Id DTO

    public ProductDto fetchById(int id) {

        String key = "product:" + id;

        // STEP 1: Check Redis
        ProductDto cached =
                (ProductDto) redisTemplate.opsForValue().get(key);

        if (cached != null) {
            System.out.println(" From REDIS");
            return cached;
        }

        // STEP 2: DB call
        System.out.println("From DATABASE");

        Product p = productDao.findById(id).orElse(null);

        if (p == null) return null;

        ProductDto dto = convertToDTO(p);

        // STEP 3: Store in Redis
        redisTemplate.opsForValue()
                .set(key, dto, Duration.ofMinutes(10));

        return dto;
    }


    public void updateProduct(Product updatedProduct) {

        Product existingProduct = productDao
                .findById(updatedProduct.getProductId())
                .orElse(null);

        if (existingProduct == null) return;

        productDao.save(updatedProduct);

        // Clear ID cache
        redisTemplate.delete("product:" + updatedProduct.getProductId());

        // Clear OLD name cache
        redisTemplate.delete("product:name:" +
                existingProduct.getProductName().toLowerCase());

        // Clear NEW name cache
        redisTemplate.delete("product:name:" +
                updatedProduct.getProductName().toLowerCase());

        //  Clear category cache
        Set<String> keys = redisTemplate.keys("product:category:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        System.out.println("Cache cleared for update");
    }


    // Get Product Entity By Id (for update/delete)
    public Product getProductEntityById(int id) {

        return productDao.findById(id).orElse(null);
    }

    // Delete Product
    public void deleteById(int id) {

        Product product = productDao.findById(id).orElse(null);

        if (product != null) {

            productDao.deleteById(id);

            // Clear ID cache
            redisTemplate.delete("product:" + id);

            // Clear name cache
            redisTemplate.delete("product:name:" +
                    product.getProductName().toLowerCase());

            // Clear category cache
            Set<String> keys = redisTemplate.keys("product:category:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }

            System.out.println("Cache cleared for delete");
        }
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

    //to count total Product
    public long getTotalProducts()
    {
        return productDao.count();
    }

    //to get highest salary
    public Double getHighestPrice() {
        return productDao.getHighestPrice();
    }

    public long getMyTotalProducts(User user) {
        return productDao.countByUser(user);
    }

    public long getMyTotalCategories(User user) {
        return productDao.countDistinctCategoryByUser(user);
    }

    public Double getMyTotalValue(User user) {
        return productDao.sumPriceByUser(user);
    }
}