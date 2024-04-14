package com.siramiks.AuthService.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
  private String email;
  private String password;
  private String phone;
  private JsonNode userMetaData;
}
