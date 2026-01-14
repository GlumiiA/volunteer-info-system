package com.volunteer.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Organization
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class Organization {

  private @Nullable Integer id;

  private @Nullable String name;

  private JsonNullable<String> description = JsonNullable.<String>undefined();

  private JsonNullable<String> address = JsonNullable.<String>undefined();

  public Organization id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор организации
   * @return id
   */
  
  @Schema(name = "id", description = "Уникальный идентификатор организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public Organization name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Название организации
   * @return name
   */
  
  @Schema(name = "name", description = "Название организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Organization description(String description) {
    this.description = JsonNullable.of(description);
    return this;
  }

  /**
   * Описание организации
   * @return description
   */
  
  @Schema(name = "description", description = "Описание организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public JsonNullable<String> getDescription() {
    return description;
  }

  public void setDescription(JsonNullable<String> description) {
    this.description = description;
  }

  public Organization address(String address) {
    this.address = JsonNullable.of(address);
    return this;
  }

  /**
   * Адрес организации
   * @return address
   */
  
  @Schema(name = "address", description = "Адрес организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public JsonNullable<String> getAddress() {
    return address;
  }

  public void setAddress(JsonNullable<String> address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Organization organization = (Organization) o;
    return Objects.equals(this.id, organization.id) &&
        Objects.equals(this.name, organization.name) &&
        equalsNullable(this.description, organization.description) &&
        equalsNullable(this.address, organization.address);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, hashCodeNullable(description), hashCodeNullable(address));
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
    sb.append("class Organization {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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

