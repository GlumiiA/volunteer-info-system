package com.volunteer.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class User {

  private @Nullable Integer id;

  private JsonNullable<Integer> organisationId = JsonNullable.<Integer>undefined();

  /**
   * Роль пользователя
   */
  public enum RoleEnum {
    USER("USER"),
    
    ORG_REPRESENTATIVE("ORG_REPRESENTATIVE"),
    
    ADMIN("ADMIN");

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

  private @Nullable RoleEnum role;

  private @Nullable String name;

  private @Nullable String email;

  private JsonNullable<String> description = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<LocalDate> birthday = JsonNullable.<LocalDate>undefined();

  private JsonNullable<String> location = JsonNullable.<String>undefined();

  private Float volunteerHours = 0f;

  private JsonNullable<Float> rating = JsonNullable.<Float>undefined();

  public User id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор пользователя
   * @return id
   */
  
  @Schema(name = "id", description = "Уникальный идентификатор пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public User organisationId(Integer organisationId) {
    this.organisationId = JsonNullable.of(organisationId);
    return this;
  }

  /**
   * ID организации (для представителей организации)
   * @return organisationId
   */
  
  @Schema(name = "organisationId", description = "ID организации (для представителей организации)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organisationId")
  public JsonNullable<Integer> getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(JsonNullable<Integer> organisationId) {
    this.organisationId = organisationId;
  }

  public User role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Роль пользователя
   * @return role
   */
  
  @Schema(name = "role", description = "Роль пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public User name(@Nullable String name) {
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

  public User email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Email пользователя
   * @return email
   */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", description = "Email пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public User description(String description) {
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

  public User birthday(LocalDate birthday) {
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

  public User location(String location) {
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

  public User volunteerHours(Float volunteerHours) {
    this.volunteerHours = volunteerHours;
    return this;
  }

  /**
   * Количество отработанных волонтерских часов
   * @return volunteerHours
   */
  
  @Schema(name = "volunteerHours", description = "Количество отработанных волонтерских часов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volunteerHours")
  public Float getVolunteerHours() {
    return volunteerHours;
  }

  public void setVolunteerHours(Float volunteerHours) {
    this.volunteerHours = volunteerHours;
  }

  public User rating(Float rating) {
    this.rating = JsonNullable.of(rating);
    return this;
  }

  /**
   * Средний рейтинг пользователя по отзывам
   * @return rating
   */
  
  @Schema(name = "rating", description = "Средний рейтинг пользователя по отзывам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rating")
  public JsonNullable<Float> getRating() {
    return rating;
  }

  public void setRating(JsonNullable<Float> rating) {
    this.rating = rating;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        equalsNullable(this.organisationId, user.organisationId) &&
        Objects.equals(this.role, user.role) &&
        Objects.equals(this.name, user.name) &&
        Objects.equals(this.email, user.email) &&
        equalsNullable(this.description, user.description) &&
        equalsNullable(this.birthday, user.birthday) &&
        equalsNullable(this.location, user.location) &&
        Objects.equals(this.volunteerHours, user.volunteerHours) &&
        equalsNullable(this.rating, user.rating);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, hashCodeNullable(organisationId), role, name, email, hashCodeNullable(description), hashCodeNullable(birthday), hashCodeNullable(location), volunteerHours, hashCodeNullable(rating));
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
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    organisationId: ").append(toIndentedString(organisationId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    volunteerHours: ").append(toIndentedString(volunteerHours)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
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

