package com.talixmines.management.usermanager.services.impl;

import com.talixmines.management.exception.EntityNotFoundException;
import com.talixmines.management.exception.InvalidEntityException;
import com.talixmines.management.exception.ResourceAlreadyExistException;
import com.talixmines.management.usermanager.dto.UtilisateurDto;
import com.talixmines.management.usermanager.services.UtilisateurService;
import com.talixmines.management.utils.ConstantUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilisateurServiceImplTest {

    @Autowired
    private UtilisateurService utilisateurService;

    @Test
    public void shouldSaveUserWithSuccess() {

        var entreprise = ConstantUtils.getEntreprise();
        entreprise.setId(3);
        var user = ConstantUtils.getUtilisateur();
        user.setEntreprise(entreprise);

        var savedUser = utilisateurService.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getMoteDePasse());
        assertEquals(savedUser.getEmail(), "joe@gmail.com");
        assertEquals(savedUser.getEntreprise().getId(), entreprise.getId());
    }

    @Test(expected = ResourceAlreadyExistException.class)
    public final void testCreateUserAlreadyExists() throws Exception {

        var entreprise = ConstantUtils.getEntreprise();
        entreprise.setId(3);
        var user = ConstantUtils.getUtilisateur();
        user.setEntreprise(entreprise);
        user.setEmail("john@email.com");

        /* Creating user */
        utilisateurService.save(user);
    }

    @Test
    public final void testReadUserById() throws Exception {

        /* Getting user */
        var user = utilisateurService.findById(105);

        assertNotNull(user,"The user should be existing");
        assertEquals(user.getEmail(),"user@email.com");
        assertEquals(user.getMoteDePasse(),"password");
    }

    @Test(expected = EntityNotFoundException.class)
    public final void testReadNonExistingUserIdentifier() throws Exception {
        /* Getting non existing User */
        utilisateurService.findById(999);
    }


    @Test
    public final void testReadUserDtoByEmptyId() throws Exception {

        /* Getting user */
        var user = utilisateurService.findById(null);

        assertNull(user);
    }

    @Test
    public final void testReadUserByEmail() throws Exception {

        /* Getting user */
        var user = utilisateurService.findByEmail("user@email.com");

        assertNotNull(user,"The user should be existing");
        assertEquals(user.getEmail(),"user@email.com");
        assertEquals(user.getMoteDePasse(),"password");
    }

    @Test(expected = EntityNotFoundException.class)
    public final void testReadNonExistingUserEmail() throws Exception {
        /* Getting non existing Email */
        utilisateurService.findByEmail("999@email.com");
    }

    @Test
    public final void testReadUsers() throws Exception {

        /* Getting users */
        List<UtilisateurDto> users = utilisateurService.findAll();

        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test(expected = EntityNotFoundException.class)
    public final void testDeleteUser() throws Exception {
        /* Deleting user */
        utilisateurService.delete(108);
        /* Getting user */
        utilisateurService.findById(108);
    }
}
