package com.volunteer.model;

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
 * OrganizationRoleRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class OrganizationRoleRequest {

  /**
   * Тип запроса - для существующей организации или создание новой
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

  private RequestTypeEnum requestType;

  private JsonNullable<Integer> organizationId = JsonNullable.<Integer>undefined();

  private JsonNullable<String> organizationName = JsonNullable.<String>undefined();

  private JsonNullable<String> organizationDescription = JsonNullable.<String>undefined();

  private JsonNullable<String> organizationAddress = JsonNullable.<String>undefined();

  public OrganizationRoleRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationRoleRequest(RequestTypeEnum requestType) {
    this.requestType = requestType;
  }

  public OrganizationRoleRequest requestType(RequestTypeEnum requestType) {
    this.requestType = requestType;
    return this;
  }

  /**
   * Тип запроса - для существующей организации или создание новой
   * @return requestType
   */
  @NotNull 
  @Schema(name = "requestType", description = "Тип запроса - для существующей организации или создание новой", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requestType")
  public RequestTypeEnum getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestTypeEnum requestType) {
    this.requestType = requestType;
  }

  public OrganizationRoleRequest organizationId(Integer organizationId) {
    this.organizationId = JsonNullable.of(organizationId);
    return this;
  }

  /**
   * ID существующей организации (обязательно если requestType=EXISTING)
   * @return organizationId
   */
  
  @Schema(name = "organizationId", description = "ID существующей организации (обязательно если requestType=EXISTING)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationId")
  public JsonNullable<Integer> getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(JsonNullable<Integer> organizationId) {
    this.organizationId = organizationId;
  }

  public OrganizationRoleRequest organizationName(String organizationName) {
    this.organizationName = JsonNullable.of(organizationName);
    return this;
  }

  /**
   * Название новой организации (обязательно если requestType=NEW)
   * @return organizationName
   */
  
  @Schema(name = "organizationName", description = "Название новой организации (обязательно если requestType=NEW)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationName")
  public JsonNullable<String> getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(JsonNullable<String> organizationName) {
    this.organizationName = organizationName;
  }

  public OrganizationRoleRequest organizationDescription(String organizationDescription) {
    this.organizationDescription = JsonNullable.of(organizationDescription);
    return this;
  }

  /**
   * Описание новой организации (для requestType=NEW)
   * @return organizationDescription
   */
  
  @Schema(name = "organizationDescription", description = "Описание новой организации (для requestType=NEW)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationDescription")
  public JsonNullable<String> getOrganizationDescription() {
    return organizationDescription;
  }

  public void setOrganizationDescription(JsonNullable<String> organizationDescription) {
    this.organizationDescription = organizationDescription;
  }

  public OrganizationRoleRequest organizationAddress(String organizationAddress) {
    this.organizationAddress = JsonNullable.of(organizationAddress);
    return this;
  }

  /**
   * Адрес новой организации (для requestType=NEW)
   * @return organizationAddress
   */
  
  @Schema(name = "organizationAddress", description = "Адрес новой организации (для requestType=NEW)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationAddress")
  public JsonNullable<String> getOrganizationAddress() {
    return organizationAddress;
  }

  public void setOrganizationAddress(JsonNullable<String> organizationAddress) {
    this.organizationAddress = organizationAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationRoleRequest organizationRoleRequest = (OrganizationRoleRequest) o;
    return Objects.equals(this.requestType, organizationRoleRequest.requestType) &&
        equalsNullable(this.organizationId, organizationRoleRequest.organizationId) &&
        equalsNullable(this.organizationName, organizationRoleRequest.organizationName) &&
        equalsNullable(this.organizationDescription, organizationRoleRequest.organizationDescription) &&
        equalsNullable(this.organizationAddress, organizationRoleRequest.organizationAddress);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestType, hashCodeNullable(organizationId), hashCodeNullable(organizationName), hashCodeNullable(organizationDescription), hashCodeNullable(organizationAddress));
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
    sb.append("class OrganizationRoleRequest {\n");
    sb.append("    requestType: ").append(toIndentedString(requestType)).append("\n");
    sb.append("    organizationId: ").append(toIndentedString(organizationId)).append("\n");
    sb.append("    organizationName: ").append(toIndentedString(organizationName)).append("\n");
    sb.append("    organizationDescription: ").append(toIndentedString(organizationDescription)).append("\n");
    sb.append("    organizationAddress: ").append(toIndentedString(organizationAddress)).append("\n");
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

