package com.talixmines.management.inventorymanager.controller;

import com.talixmines.management.inventorymanager.dto.CategoryDto;
import com.talixmines.management.utils.ConstantUtils;
import com.talixmines.management.utils.GlobalControllerExceptionHandler;
import com.talixmines.management.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryControllerTest {

    private static final String BASE_URL = "/inventory-manager";

    @Autowired
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                }).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public final void testCreateCategory() throws Exception {

        /* Initializing category */
        CategoryDto categoryDto = ConstantUtils.getCategory();

        /* Creating category */
        mockMvc.perform(post(BASE_URL + "/v1/categories")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(categoryDto)))
                .andExpect(status().isOk());
    }

    @Test
    public final void testAddEmptyCategory() throws Exception {

        /* Initializing category */
        var categoryDto = CategoryDto.builder()
                .build();

        /* Creating category */
        mockMvc.perform(post(BASE_URL + "/v1/categories")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(categoryDto)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void testUpdateCategory() throws Exception {
        /* Initializing category
        CategoryDto categoryDto = ConstantUtils.getCategory();
        categoryDto.setId(102);
        categoryDto.setDesignation("Category Updated");


        /* Creating category
        mockMvc.perform(post(BASE_URL + "/v1/categories/update")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(categoryDto)))
                .andExpect(status().isOk());

         */
    }

    @Test
    public void testReadCategoryById() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/categories/103"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReadNonExistingCategoryById() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReadCategoryByCode() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/categories/code/CAT-103"))
                .andExpect(status().isOk());
    }

    @Test
    public final void testReadCategories() throws Exception {

        /* Reading categories */
        mockMvc.perform(get(BASE_URL + "/v1/categories")).andExpect(status().isOk());
    }

    @Test
    public final void testDeleteCategorie() throws Exception {

        /* Deleting subscription */
        mockMvc.perform(delete(BASE_URL + "/v1/categories/104")).andExpect(status().isOk());

    }

}
