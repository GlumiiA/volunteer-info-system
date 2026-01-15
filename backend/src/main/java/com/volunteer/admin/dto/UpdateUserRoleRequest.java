package com.volunteer.admin.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateUserRoleRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class UpdateUserRoleRequest {

  /**
   * Новая роль пользователя
   */
  public enum RoleEnum {
    USER("USER"),
    
    ORG_REPRESENTATIVE("ORG_REPRESENTATIVE");

    private final String value;

    RoleEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  private JsonNullable<Integer> organisationId = JsonNullable.<Integer>undefined();

  public UpdateUserRoleRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateUserRoleRequest(RoleEnum role) {
    this.role = role;
  }

  public UpdateUserRoleRequest role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Новая роль пользователя
   * @return role
   */
  @NotNull 
  @Schema(name = "role", description = "Новая роль пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public UpdateUserRoleRequest organisationId(Integer organisationId) {
    this.organisationId = JsonNullable.of(organisationId);
    return this;
  }

  /**
   * ID организации (обязательно для роли ORG_REPRESENTATIVE, null для USER)
   * @return organisationId
   */
  
  @Schema(name = "organisationId", description = "ID организации (обязательно для роли ORG_REPRESENTATIVE, null для USER)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organisationId")
  public JsonNullable<Integer> getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(JsonNullable<Integer> organisationId) {
    this.organisationId = organisationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateUserRoleRequest updateUserRoleRequest = (UpdateUserRoleRequest) o;
    return Objects.equals(this.role, updateUserRoleRequest.role) &&
        equalsNullable(this.organisationId, updateUserRoleRequest.organisationId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, hashCodeNullable(organisationId));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateUserRoleRequest {\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    organisationId: ").append(toIndentedString(organisationId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

