package com.talixmines.management.usermanager.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    private static final String BASE_URL = "/user-manager";

    @Autowired
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                }).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public final void testCreateArticle() throws Exception {

        /* Initializing request */
        var request = ConstantUtils.getAuthenticationRequest();

        /* Creating request */
        mockMvc.perform(post(BASE_URL + "/v1/users/authenticate")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk());

    }
}
