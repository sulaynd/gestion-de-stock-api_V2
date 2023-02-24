package com.talixmines.management.usermanager.controller.api;

import static com.talixmines.management.utils.Constants.AUTHENTICATION_ENDPOINT;

import com.talixmines.management.usermanager.dto.auth.AuthenticationRequest;
import com.talixmines.management.usermanager.dto.auth.AuthenticationResponse;
import com.talixmines.management.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = Constants.TAG_USER_MANAGEMENT)
public interface AuthenticationApi {

  @ApiOperation(
          value = Constants.ACTION_AUTHENTICATION,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Authenticate a user in the Talix Mines Management"
  )
  @PostMapping(AUTHENTICATION_ENDPOINT + "/authenticate")
  ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);

}
