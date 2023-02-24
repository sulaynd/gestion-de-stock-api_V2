package com.talixmines.management.usermanager.controller;

import com.talixmines.management.usermanager.dto.UtilisateurDto;
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
public class UtilisateurControllerTest {

    private static final String BASE_URL = "/user-manager";

    @Autowired
    private UtilisateurController utilisateurController;

    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(utilisateurController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                }).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public final void testCreateUser() throws Exception {

        /* Initializing request */
        var request = ConstantUtils.getUtilisateur();
        request.setEmail("johnDoe@gmail.com");
        /* Creating request */
        mockMvc.perform(post(BASE_URL + "/v1/users")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk());

    }

    @Test
    public final void testCreateUserAlreadyExists() throws Exception {

        /* Initializing user */
        var request = ConstantUtils.getUtilisateur();
        request.setEmail("user@email.com");

        /* Creating user */
        mockMvc.perform(post(BASE_URL + "/v1/users")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(request)))
                .andExpect(status().isConflict());

    }

    @Test
    public final void testAddEmptyUser() throws Exception {

        /* Initializing user */
        var user = UtilisateurDto.builder()
                .build();

        /* Creating user */
        mockMvc.perform(post(BASE_URL + "/v1/users")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(user)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void testReadUserId() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/users/105"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReadUserNotExistsById() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReadUserByEmail() throws Exception {
        // Calling
        mockMvc.perform(get(BASE_URL + "/v1/users/email/user@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    public final void testReadUsers() throws Exception {

        /* Reading user */
        mockMvc.perform(get(BASE_URL + "/v1/users")).andExpect(status().isOk());
    }

    @Test
    public final void testDeleteUser() throws Exception {

        /* Deleting user */
        mockMvc.perform(delete(BASE_URL + "/v1/users/107")).andExpect(status().isOk());

    }
}
