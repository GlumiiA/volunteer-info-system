package com.volunteer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "organisation_id")

  private Integer organisationId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private UserRole role;

  @Column(unique = true, nullable = false)
  private String username;

  @Email
  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "birthday")
  private LocalDate birthday;

  @Column(name = "location")
  private String location;

  @Column(name = "volunteer_hours")
  private Float volunteerHours = 0f;

  @Column(name = "rating")
  private Float rating;

  @Column(name = "password_hash_bcrypt", nullable = false)
  private String passwordHash;

  @Column(name = "avatar_url", columnDefinition = "TEXT")
  private String avatarUrl;

  // Map to OpenAPI name field
  public String getName() {
    return username;
  }

  public void setName(String name) {
    this.username = name;
  }
}
