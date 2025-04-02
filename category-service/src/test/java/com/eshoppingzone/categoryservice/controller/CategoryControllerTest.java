package com.eshoppingzone.categoryservice.controller;

import com.eshoppingzone.categoryservice.entity.Category;
import com.eshoppingzone.categoryservice.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc to test our controller without starting a server
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        // Creating sample category objects
        category1 = new Category(1L, "Electronics", "Gadgets and Devices");
        category2 = new Category(2L, "Clothing", "Apparel and Accessories");
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Mocking service layer response
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        // Performing GET request and checking response
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.size()").value(2)); // Checking the size of returned list

        // Verify that getAllCategories() was called once
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById_WhenExists() throws Exception {
        // Mocking category retrieval by ID
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category1));

        // Performing GET request for category with ID 1
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.name").value("Electronics")); // Checking category name

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testGetCategoryById_WhenNotExists() throws Exception {
        // Mocking empty response when category is not found
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        // Performing GET request for a non-existent category
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testAddCategory() throws Exception {
        // Mocking category creation
        when(categoryService.addCategory(any(Category.class))).thenReturn(category1);

        // Performing POST request with category data
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\",\"description\":\"Gadgets and Devices\"}"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService, times(1)).addCategory(any(Category.class));
    }

    @Test
    void testUpdateCategory_WhenExists() throws Exception {
        // Creating updated category data
        Category updatedCategory = new Category(1L, "Updated Electronics", "Updated Description");

        // Mocking successful update
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updatedCategory);

        // Performing PUT request to update category
        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Electronics\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.name").value("Updated Electronics"));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void testUpdateCategory_WhenNotExists() throws Exception {
        // Mocking failed update (category does not exist)
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(null);

        // Performing PUT request for non-existent category
        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void testDeleteCategory_WhenExists() throws Exception {
        // Mocking successful deletion
        when(categoryService.deleteCategory(1L)).thenReturn(true);

        // Performing DELETE request
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void testDeleteCategory_WhenNotExists() throws Exception {
        // Mocking failed deletion (category does not exist)
        when(categoryService.deleteCategory(1L)).thenReturn(false);

        // Performing DELETE request for non-existent category
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found

        verify(categoryService, times(1)).deleteCategory(1L);
    }
}
