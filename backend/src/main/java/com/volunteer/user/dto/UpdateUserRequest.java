package com.volunteer.user.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * UpdateUserRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class UpdateUserRequest {

  private @Nullable String name;

  private JsonNullable<String> description = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<LocalDate> birthday = JsonNullable.<LocalDate>undefined();

  private JsonNullable<String> location = JsonNullable.<String>undefined();

  public UpdateUserRequest name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Полное имя пользователя
   * @return name
   */
  
  @Schema(name = "name", description = "Полное имя пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public UpdateUserRequest description(String description) {
    this.description = JsonNullable.of(description);
    return this;
  }

  /**
   * Описание \"о себе\"
   * @return description
   */
  
  @Schema(name = "description", description = "Описание \"о себе\"", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public JsonNullable<String> getDescription() {
    return description;
  }

  public void setDescription(JsonNullable<String> description) {
    this.description = description;
  }

  public UpdateUserRequest birthday(LocalDate birthday) {
    this.birthday = JsonNullable.of(birthday);
    return this;
  }

  /**
   * День рождения
   * @return birthday
   */
  @Valid 
  @Schema(name = "birthday", description = "День рождения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("birthday")
  public JsonNullable<LocalDate> getBirthday() {
    return birthday;
  }

  public void setBirthday(JsonNullable<LocalDate> birthday) {
    this.birthday = birthday;
  }

  public UpdateUserRequest location(String location) {
    this.location = JsonNullable.of(location);
    return this;
  }

  /**
   * Место проживания
   * @return location
   */
  
  @Schema(name = "location", description = "Место проживания", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public JsonNullable<String> getLocation() {
    return location;
  }

  public void setLocation(JsonNullable<String> location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateUserRequest updateUserRequest = (UpdateUserRequest) o;
    return Objects.equals(this.name, updateUserRequest.name) &&
        equalsNullable(this.description, updateUserRequest.description) &&
        equalsNullable(this.birthday, updateUserRequest.birthday) &&
        equalsNullable(this.location, updateUserRequest.location);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, hashCodeNullable(description), hashCodeNullable(birthday), hashCodeNullable(location));
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
    sb.append("class UpdateUserRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

