package com.volunteer.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * MassEvent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class MassEvent {

  private @Nullable Integer id;

  private @Nullable Integer organisationId;

  private @Nullable String title;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime dateStart;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime dateEnd;

  private @Nullable Float workHours;

  private @Nullable String address;

  private @Nullable Integer volunteersRequired;

  private JsonNullable<Integer> ageRestriction = JsonNullable.<Integer>undefined();

  public MassEvent id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор массового мероприятия
   * @return id
   */
  
  @Schema(name = "id", description = "Уникальный идентификатор массового мероприятия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public MassEvent organisationId(@Nullable Integer organisationId) {
    this.organisationId = organisationId;
    return this;
  }

  /**
   * ID организации-организатора
   * @return organisationId
   */
  
  @Schema(name = "organisationId", description = "ID организации-организатора", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organisationId")
  public @Nullable Integer getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(@Nullable Integer organisationId) {
    this.organisationId = organisationId;
  }

  public MassEvent title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Название мероприятия
   * @return title
   */
  
  @Schema(name = "title", description = "Название мероприятия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public MassEvent description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание мероприятия
   * @return description
   */
  
  @Schema(name = "description", description = "Описание мероприятия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public MassEvent dateStart(@Nullable OffsetDateTime dateStart) {
    this.dateStart = dateStart;
    return this;
  }

  /**
   * Дата и время начала
   * @return dateStart
   */
  @Valid 
  @Schema(name = "dateStart", description = "Дата и время начала", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dateStart")
  public @Nullable OffsetDateTime getDateStart() {
    return dateStart;
  }

  public void setDateStart(@Nullable OffsetDateTime dateStart) {
    this.dateStart = dateStart;
  }

  public MassEvent dateEnd(@Nullable OffsetDateTime dateEnd) {
    this.dateEnd = dateEnd;
    return this;
  }

  /**
   * Дата и время окончания
   * @return dateEnd
   */
  @Valid 
  @Schema(name = "dateEnd", description = "Дата и время окончания", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dateEnd")
  public @Nullable OffsetDateTime getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(@Nullable OffsetDateTime dateEnd) {
    this.dateEnd = dateEnd;
  }

  public MassEvent workHours(@Nullable Float workHours) {
    this.workHours = workHours;
    return this;
  }

  /**
   * Количество часов работы для волонтера
   * @return workHours
   */
  
  @Schema(name = "workHours", description = "Количество часов работы для волонтера", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("workHours")
  public @Nullable Float getWorkHours() {
    return workHours;
  }

  public void setWorkHours(@Nullable Float workHours) {
    this.workHours = workHours;
  }

  public MassEvent address(@Nullable String address) {
    this.address = address;
    return this;
  }

  /**
   * Адрес проведения мероприятия
   * @return address
   */
  
  @Schema(name = "address", description = "Адрес проведения мероприятия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public @Nullable String getAddress() {
    return address;
  }

  public void setAddress(@Nullable String address) {
    this.address = address;
  }

  public MassEvent volunteersRequired(@Nullable Integer volunteersRequired) {
    this.volunteersRequired = volunteersRequired;
    return this;
  }

  /**
   * Требуемое количество волонтеров
   * @return volunteersRequired
   */
  
  @Schema(name = "volunteersRequired", description = "Требуемое количество волонтеров", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volunteersRequired")
  public @Nullable Integer getVolunteersRequired() {
    return volunteersRequired;
  }

  public void setVolunteersRequired(@Nullable Integer volunteersRequired) {
    this.volunteersRequired = volunteersRequired;
  }

  public MassEvent ageRestriction(Integer ageRestriction) {
    this.ageRestriction = JsonNullable.of(ageRestriction);
    return this;
  }

  /**
   * Возрастное ограничение
   * @return ageRestriction
   */
  
  @Schema(name = "ageRestriction", description = "Возрастное ограничение", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ageRestriction")
  public JsonNullable<Integer> getAgeRestriction() {
    return ageRestriction;
  }

  public void setAgeRestriction(JsonNullable<Integer> ageRestriction) {
    this.ageRestriction = ageRestriction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MassEvent massEvent = (MassEvent) o;
    return Objects.equals(this.id, massEvent.id) &&
        Objects.equals(this.organisationId, massEvent.organisationId) &&
        Objects.equals(this.title, massEvent.title) &&
        Objects.equals(this.description, massEvent.description) &&
        Objects.equals(this.dateStart, massEvent.dateStart) &&
        Objects.equals(this.dateEnd, massEvent.dateEnd) &&
        Objects.equals(this.workHours, massEvent.workHours) &&
        Objects.equals(this.address, massEvent.address) &&
        Objects.equals(this.volunteersRequired, massEvent.volunteersRequired) &&
        equalsNullable(this.ageRestriction, massEvent.ageRestriction);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, organisationId, title, description, dateStart, dateEnd, workHours, address, volunteersRequired, hashCodeNullable(ageRestriction));
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
    sb.append("class MassEvent {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    organisationId: ").append(toIndentedString(organisationId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dateStart: ").append(toIndentedString(dateStart)).append("\n");
    sb.append("    dateEnd: ").append(toIndentedString(dateEnd)).append("\n");
    sb.append("    workHours: ").append(toIndentedString(workHours)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    volunteersRequired: ").append(toIndentedString(volunteersRequired)).append("\n");
    sb.append("    ageRestriction: ").append(toIndentedString(ageRestriction)).append("\n");
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

