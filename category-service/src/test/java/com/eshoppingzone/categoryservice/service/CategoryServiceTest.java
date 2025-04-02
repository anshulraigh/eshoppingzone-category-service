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

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category(1L, "Electronics", "Gadgets and Devices");
        category2 = new Category(2L, "Clothing", "Apparel and Accessories");
    }

    @Test
    void getAllCategories() {
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void addCategory() {
        when(categoryRepository.save(category1)).thenReturn(category1);

        Category result = categoryService.addCategory(category1);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(category1);
    }

    @Test
    void updateCategory() {
        Category updatedCategory = new Category(1L, "Updated Electronics", "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of((category1)));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(1L, updatedCategory);

        assertNotNull(result);
        assertEquals("Updated Electronics", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategory() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        boolean result = categoryService.deleteCategory(1L);

        assertTrue(result);
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategory_NotExists() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        boolean result = categoryService.deleteCategory(1L);

        assertFalse(result);
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, never()).deleteById(1L);
    }
}
