package com.eshoppingzone.categoryservice.service;

import com.eshoppingzone.categoryservice.config.UserContext;
import com.eshoppingzone.categoryservice.entity.Category;
import com.eshoppingzone.categoryservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category addCategory(Category category) {
        checkAdminAccess();
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        checkAdminAccess();
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setDescription(categoryDetails.getDescription());
            return categoryRepository.save(category);
        }).orElse(null);
    }

    public boolean deleteCategory(Long id) {
        checkAdminAccess();
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void checkAdminAccess() {
        if (!"ADMIN".equalsIgnoreCase(UserContext.getRole())) {
            throw new RuntimeException("Access denied: Admin privileges required.");
        }
    }
}
