package com.volunteer.organization.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.volunteer.entity.User;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * OrganizationRoleRequestItem
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class OrganizationRoleRequestItem {

  private @Nullable Integer id;

  private @Nullable Integer userId;

  private @Nullable User user;

  /**
   * Тип запроса
   */
  public enum RequestTypeEnum {
    EXISTING("EXISTING"),
    
    NEW("NEW");

    private final String value;

    RequestTypeEnum(String value) {
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
    public static RequestTypeEnum fromValue(String value) {
      for (RequestTypeEnum b : RequestTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RequestTypeEnum requestType;

  private JsonNullable<Integer> organizationId = JsonNullable.<Integer>undefined();

  private JsonNullable<String> organizationName = JsonNullable.<String>undefined();

  private JsonNullable<String> organizationDescription = JsonNullable.<String>undefined();

  private JsonNullable<String> organizationAddress = JsonNullable.<String>undefined();

  /**
   * Статус запроса
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public OrganizationRoleRequestItem id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * ID запроса
   * @return id
   */
  
  @Schema(name = "id", description = "ID запроса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public OrganizationRoleRequestItem userId(@Nullable Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * ID пользователя
   * @return userId
   */
  
  @Schema(name = "userId", description = "ID пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public @Nullable Integer getUserId() {
    return userId;
  }

  public void setUserId(@Nullable Integer userId) {
    this.userId = userId;
  }

  public OrganizationRoleRequestItem user(@Nullable User user) {
    this.user = user;
    return this;
  }

  /**
   * Get user
   * @return user
   */
  @Valid 
  @Schema(name = "user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user")
  public @Nullable User getUser() {
    return user;
  }

  public void setUser(@Nullable User user) {
    this.user = user;
  }

  public OrganizationRoleRequestItem requestType(@Nullable RequestTypeEnum requestType) {
    this.requestType = requestType;
    return this;
  }

  /**
   * Тип запроса
   * @return requestType
   */
  
  @Schema(name = "requestType", description = "Тип запроса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestType")
  public @Nullable RequestTypeEnum getRequestType() {
    return requestType;
  }

  public void setRequestType(@Nullable RequestTypeEnum requestType) {
    this.requestType = requestType;
  }

  public OrganizationRoleRequestItem organizationId(Integer organizationId) {
    this.organizationId = JsonNullable.of(organizationId);
    return this;
  }

  /**
   * ID существующей организации
   * @return organizationId
   */
  
  @Schema(name = "organizationId", description = "ID существующей организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationId")
  public JsonNullable<Integer> getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(JsonNullable<Integer> organizationId) {
    this.organizationId = organizationId;
  }

  public OrganizationRoleRequestItem organizationName(String organizationName) {
    this.organizationName = JsonNullable.of(organizationName);
    return this;
  }

  /**
   * Название новой организации
   * @return organizationName
   */
  
  @Schema(name = "organizationName", description = "Название новой организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationName")
  public JsonNullable<String> getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(JsonNullable<String> organizationName) {
    this.organizationName = organizationName;
  }

  public OrganizationRoleRequestItem organizationDescription(String organizationDescription) {
    this.organizationDescription = JsonNullable.of(organizationDescription);
    return this;
  }

  /**
   * Описание новой организации
   * @return organizationDescription
   */
  
  @Schema(name = "organizationDescription", description = "Описание новой организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationDescription")
  public JsonNullable<String> getOrganizationDescription() {
    return organizationDescription;
  }

  public void setOrganizationDescription(JsonNullable<String> organizationDescription) {
    this.organizationDescription = organizationDescription;
  }

  public OrganizationRoleRequestItem organizationAddress(String organizationAddress) {
    this.organizationAddress = JsonNullable.of(organizationAddress);
    return this;
  }

  /**
   * Адрес новой организации
   * @return organizationAddress
   */
  
  @Schema(name = "organizationAddress", description = "Адрес новой организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationAddress")
  public JsonNullable<String> getOrganizationAddress() {
    return organizationAddress;
  }

  public void setOrganizationAddress(JsonNullable<String> organizationAddress) {
    this.organizationAddress = organizationAddress;
  }

  public OrganizationRoleRequestItem status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Статус запроса
   * @return status
   */
  
  @Schema(name = "status", description = "Статус запроса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public OrganizationRoleRequestItem createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Дата создания запроса
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", description = "Дата создания запроса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationRoleRequestItem organizationRoleRequestItem = (OrganizationRoleRequestItem) o;
    return Objects.equals(this.id, organizationRoleRequestItem.id) &&
        Objects.equals(this.userId, organizationRoleRequestItem.userId) &&
        Objects.equals(this.user, organizationRoleRequestItem.user) &&
        Objects.equals(this.requestType, organizationRoleRequestItem.requestType) &&
        equalsNullable(this.organizationId, organizationRoleRequestItem.organizationId) &&
        equalsNullable(this.organizationName, organizationRoleRequestItem.organizationName) &&
        equalsNullable(this.organizationDescription, organizationRoleRequestItem.organizationDescription) &&
        equalsNullable(this.organizationAddress, organizationRoleRequestItem.organizationAddress) &&
        Objects.equals(this.status, organizationRoleRequestItem.status) &&
        Objects.equals(this.createdAt, organizationRoleRequestItem.createdAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, user, requestType, hashCodeNullable(organizationId), hashCodeNullable(organizationName), hashCodeNullable(organizationDescription), hashCodeNullable(organizationAddress), status, createdAt);
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
    sb.append("class OrganizationRoleRequestItem {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    requestType: ").append(toIndentedString(requestType)).append("\n");
    sb.append("    organizationId: ").append(toIndentedString(organizationId)).append("\n");
    sb.append("    organizationName: ").append(toIndentedString(organizationName)).append("\n");
    sb.append("    organizationDescription: ").append(toIndentedString(organizationDescription)).append("\n");
    sb.append("    organizationAddress: ").append(toIndentedString(organizationAddress)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

