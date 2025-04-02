package com.eshoppingzone.categoryservice.service;

import com.eshoppingzone.categoryservice.entity.Category;
import com.eshoppingzone.categoryservice.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category electronicsCategory;
    private Category clothingCategory;

    @BeforeEach
    void setUp() {
        // Initializing category objects before each test
        electronicsCategory = new Category(1L, "Electronics", "Gadgets and Devices");
        clothingCategory = new Category(2L, "Clothing", "Apparel and Accessories");
    }

    @Test
    void testGetAllCategories_ReturnsCategoryList() {
        // Mocking repository response with a list of categories
        List<Category> categories = Arrays.asList(electronicsCategory, clothingCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // Calling service method
        List<Category> result = categoryService.getAllCategories();

        // Verifying the response
        assertEquals(2, result.size(), "The size of returned categories should be 2.");
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_WhenCategoryExists() {
        // Mocking repository to return a category for a given ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronicsCategory));

        // Calling service method
        Optional<Category> result = categoryService.getCategoryById(1L);

        // Verifying the response
        assertTrue(result.isPresent(), "Category should be found.");
        assertEquals("Electronics", result.get().getName(), "Category name should match.");
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryById_WhenCategoryDoesNotExist() {
        // Mocking repository to return empty when category is not found
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        // Calling service method
        Optional<Category> result = categoryService.getCategoryById(3L);

        // Verifying the response
        assertFalse(result.isPresent(), "Category should not be found.");
        verify(categoryRepository, times(1)).findById(3L);
    }

    @Test
    void testAddCategory_SavesAndReturnsCategory() {
        // Mocking repository save operation
        when(categoryRepository.save(electronicsCategory)).thenReturn(electronicsCategory);

        // Calling service method
        Category result = categoryService.addCategory(electronicsCategory);

        // Verifying the response
        assertNotNull(result, "Saved category should not be null.");
        assertEquals("Electronics", result.getName(), "Category name should match.");
        verify(categoryRepository, times(1)).save(electronicsCategory);
    }

    @Test
    void testUpdateCategory_WhenCategoryExists() {
        // Mocking repository to find the existing category
        Category updatedCategory = new Category(1L, "Updated Electronics", "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronicsCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Calling service method
        Category result = categoryService.updateCategory(1L, updatedCategory);

        // Verifying the response
        assertNotNull(result, "Updated category should not be null.");
        assertEquals("Updated Electronics", result.getName(), "Category name should be updated.");
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_WhenCategoryDoesNotExist() {
        // Mocking repository to return empty when category is not found
        Category updatedCategory = new Category(3L, "Non-Existent", "This category doesn't exist.");
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        // Calling service method
        Category result = categoryService.updateCategory(3L, updatedCategory);

        // Verifying the response
        assertNull(result, "Updating a non-existent category should return null.");
        verify(categoryRepository, times(1)).findById(3L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testDeleteCategory_WhenCategoryExists() {
        // Mocking repository to return true when category exists
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        // Calling service method
        boolean result = categoryService.deleteCategory(1L);

        // Verifying the response
        assertTrue(result, "Deleting an existing category should return true.");
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_WhenCategoryDoesNotExist() {
        // Mocking repository to return false when category does not exist
        when(categoryRepository.existsById(3L)).thenReturn(false);

        // Calling service method
        boolean result = categoryService.deleteCategory(3L);

        // Verifying the response
        assertFalse(result, "Deleting a non-existent category should return false.");
        verify(categoryRepository, times(1)).existsById(3L);
        verify(categoryRepository, never()).deleteById(3L);
    }
}
