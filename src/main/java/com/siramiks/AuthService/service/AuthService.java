package com.siramiks.AuthService.service;

import com.siramiks.AuthService.entity.User;
import com.siramiks.AuthService.model.AuthRequest;
import com.siramiks.AuthService.model.AuthenticatedUserDTO;
import com.siramiks.AuthService.model.UserDTO;
import com.siramiks.AuthService.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  public JwtService jwtService;
  @Autowired
  private AuthenticationManager authenticationManager;

  public String registerUser(UserDTO userDTO) {
    log.info("Prepping to register new user...");
    // Check if the user email already exists
    if (userExists(userDTO.getEmail())) {
      log.info("Email exists! Responding to client with 409 conflict");
      return "Email taken";
    }

    String encodedPW = passwordEncoder.encode(userDTO.getPassword());
    User user = User.builder()
            .email(userDTO.getEmail())
            .passwordHash(encodedPW)
            .phone(userDTO.getPhone())
            .createdAt(LocalDateTime.now())
            .rawUserMetadata(userDTO.getUserMetaData())
            .build();
    userRepository.save(user);
    log.info("Successfully saved user!!");
    return "Successfully registered user";
  }

  public String generateToken(AuthRequest authRequest) {
    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getEncrypted_password()));
    log.info("Validating use with authentication manager {}", auth);
    if (auth.isAuthenticated()) {
      log.info("Is authenticated. Generating token...");
      AuthenticatedUserDTO authenticatedUserDTO = fetchAuthenticatedUserByUsername(authRequest.getUsername());
      return jwtService.generateToken(authenticatedUserDTO);
    } else {
      log.info("Failed to generate token! User not found!");
      throw new RuntimeException("user not found");
    }
  }

  public void validateToken(String token) {
    log.info("Validating token!");
    jwtService.validateToken(token);
  }

  private boolean userExists(String email) {
    // Check if the user email already exists
    Optional<User> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      return true;
    }
    return false;
  }

  private AuthenticatedUserDTO fetchAuthenticatedUserByUsername(String username) {
    // Check if the user email already exists
    Optional<User> existingUser = userRepository.findByEmail(username);
    if (!existingUser.isPresent()) return null;

    User user = existingUser.get();

    return AuthenticatedUserDTO.builder()
            .email(user.getEmail())
            .role(user.getRole())
            .phone(user.getPhone())
            .cardId(user.getCardId())
            .purchases(user.getPurchases())
            .rawUserMetadata(user.getRawUserMetadata())
            .build();
  }

}
