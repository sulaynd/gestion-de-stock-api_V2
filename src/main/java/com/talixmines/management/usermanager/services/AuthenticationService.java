package com.talixmines.management.usermanager.services;

import com.talixmines.management.usermanager.dto.auth.AuthenticationRequest;
import com.talixmines.management.usermanager.dto.auth.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
