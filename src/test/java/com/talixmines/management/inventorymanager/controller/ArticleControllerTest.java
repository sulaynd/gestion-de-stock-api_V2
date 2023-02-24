package com.talixmines.management.inventorymanager.controller;

import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.utils.ConstantUtils;
import com.talixmines.management.utils.GlobalControllerExceptionHandler;
import com.talixmines.management.utils.TestUtils;
import org.hamcrest.Matchers;
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

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleControllerTest {

    private static final String BASE_URL = "/inventory-manager";
    @Autowired
    private ArticleController articleController;

    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX");

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                }).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public void testReadArticleById() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/articles/103"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReadNonExistingArticleById() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/articles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public final void testCreateArticle() throws Exception {

        /* Initializing category */
        var category = ConstantUtils.getCategory();
        category.setId(103);
        var articleDto = ConstantUtils.getArticle();
        var codeArticle = "ART-Y";
        articleDto.setCodeArticle(codeArticle);
        articleDto.setCategory(category);

        /* Creating article */
        mockMvc.perform(post(BASE_URL + "/v1/articles")
                .contentType(utils.APPLICATION_JSON_UTF8)
                .content(utils.convertObjectToJsonBytes(articleDto)))
                .andExpect(status().isOk());

    }

    @Test
    public final void testAddEmptyArticle() throws Exception {

        /* Initializing article */
        var articleDto = ArticleDto.builder()
                .build();

        /* Creating article */
        mockMvc.perform(post(BASE_URL + "/v1/articles")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(articleDto)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void testReadArticles() throws Exception {

        mockMvc.perform(get(BASE_URL + "/v1/articles"))
                .andExpect(status().isOk());
    }


    @Test
    public final void testReadArticlesWithParams() throws Exception {

        /* Reading articles */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer categoryId = 101;
        String dateFromInString = "2023-02-06";
        String dateToInString = "2023-02-07";
        String articleDesignation = "ART101";
        Date creationDateFrom = sdf.parse(dateFromInString);
        Date creationDateTo = sdf.parse(dateToInString);


        mockMvc.perform(
                        get(BASE_URL + "/v1/articles")
                                .param("creationDateFrom", DATE_FORMATTER.format(creationDateFrom))
                                .param("creationDateTo", DATE_FORMATTER.format(creationDateTo))
                                .param("categoryId", String.valueOf(categoryId))
                                .param("designation", articleDesignation)


                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*").value(Matchers.hasSize(1)));

    }


    @Test
    public final void testReadArticlesByCategory() throws Exception {

        /* Reading articles By category */
        mockMvc.perform(get(BASE_URL + "/v1/category/101/articles")).andExpect(status().isOk());

    }

    @Test
    public final void testReadArticlesByDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateFromInString = "2023-02-06";
        String dateToInString = "2023-02-07";
        Date creationDateFrom = sdf.parse(dateFromInString);
        Date creationDateTo = sdf.parse(dateToInString);

        mockMvc.perform(
                        get(BASE_URL + "/v1/articles")
                                .param("creationDateFrom", DATE_FORMATTER.format(creationDateFrom))
                                .param("creationDateTo", DATE_FORMATTER.format(creationDateTo))

                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*").value(Matchers.hasSize(1)));
    }

}
