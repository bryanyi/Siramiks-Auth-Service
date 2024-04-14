package com.siramiks.AuthService.controller;

import com.siramiks.AuthService.model.AuthRequest;
import com.siramiks.AuthService.model.UserDTO;
import com.siramiks.AuthService.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  @Autowired
  private AuthService authService;

  @PreAuthorize(value = "hasRole('ROLE_ANONYMOUS')")
  @GetMapping("/test")
  public String test() {
    return "auth route test passed";
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
    log.info("registering new user!");
    String response = authService.registerUser(userDTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/getToken")
  public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
    log.info("Getting token!");
    String token = authService.generateToken(authRequest);
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @GetMapping("/validateToken")
  public String validateToken(@RequestParam("token") String token) {
    log.info("Validating token!");
    authService.validateToken(token);
    return "Validated token!";
  }
}
