package com.siramiks.AuthService.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AuthenticatedUserDTO {
  private UUID id;
  private String email;
  private String role;
  private String phone;
  private UUID cardId;
  private List<UUID> purchases;
  private JsonNode rawUserMetadata;
}
