package com.siramiks.AuthService.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  @Column(name = "email")
  private String email;

  @Column(name = "card_id")
  private UUID cardId;

  @Column(name = "purchases")
  private List<UUID> purchases;

  @Column(name = "email_confirmed")
  private Boolean emailConfirmed;

  @Column(name = "phone")
  private String phone;

  @Column(name = "phone_confirmed")
  private Boolean phoneConfirmed;

  @Column(name = "encrypted_password")
  private String passwordHash;

  @Column(name = "password_reset_token")
  private String passwordResetToken;

  @Column(name = "password_reset_sent_at")
  private OffsetDateTime passwordResetSentAt;

  @Column(name = "aud")
  private String aud;

  @Column(name = "role")
  private String role;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "raw_user_meta_data")
  private JsonNode rawUserMetadata;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return emailConfirmed;
  }
}
