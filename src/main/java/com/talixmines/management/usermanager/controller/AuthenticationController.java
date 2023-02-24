package com.talixmines.management.usermanager.controller;

import com.talixmines.management.usermanager.controller.api.AuthenticationApi;
import com.talixmines.management.usermanager.dto.auth.AuthenticationRequest;
import com.talixmines.management.usermanager.dto.auth.AuthenticationResponse;
import com.talixmines.management.usermanager.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {
  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}
