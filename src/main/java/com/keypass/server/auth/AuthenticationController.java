package com.keypass.server.auth;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  private final AuthenticationManager authenticationManager;
  private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);


  @PostMapping("/new")
  public Object authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
    Authentication authentication = authenticationManager
    .authenticate(new UsernamePasswordAuthenticationToken(
      authenticationRequestDto.username(),
      authenticationRequestDto.password()));
    return authenticationService.authenticate(authentication);
  }

}
