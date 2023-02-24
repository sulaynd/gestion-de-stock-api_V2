package com.talixmines.management.usermanager.services.impl;

import com.talixmines.management.usermanager.services.AuthenticationService;
import com.talixmines.management.utils.ConstantUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationServiceImplTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void shouldUserAuthenticateWithSuccess() {
        var request = ConstantUtils.getAuthenticationRequest();
        var jwtToken = authenticationService.authenticate(request);

        assertNotNull(jwtToken);
    }
}
