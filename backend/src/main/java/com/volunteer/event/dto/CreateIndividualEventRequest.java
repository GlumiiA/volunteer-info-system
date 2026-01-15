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
 * CreateIndividualEventRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class CreateIndividualEventRequest {

  private String title;

  private String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime dateStart;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime dateEnd;

  private Integer volunteersRequired;

  private JsonNullable<Integer> ageRestriction = JsonNullable.<Integer>undefined();

  public CreateIndividualEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateIndividualEventRequest(String title, String description, OffsetDateTime dateStart, OffsetDateTime dateEnd, Integer volunteersRequired) {
    this.title = title;
    this.description = description;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
    this.volunteersRequired = volunteersRequired;
  }

  public CreateIndividualEventRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Название заявки
   * @return title
   */
  @NotNull 
  @Schema(name = "title", description = "Название заявки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CreateIndividualEventRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание заявки
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "Описание заявки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CreateIndividualEventRequest dateStart(OffsetDateTime dateStart) {
    this.dateStart = dateStart;
    return this;
  }

  /**
   * Дата и время начала
   * @return dateStart
   */
  @NotNull @Valid 
  @Schema(name = "dateStart", description = "Дата и время начала", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dateStart")
  public OffsetDateTime getDateStart() {
    return dateStart;
  }

  public void setDateStart(OffsetDateTime dateStart) {
    this.dateStart = dateStart;
  }

  public CreateIndividualEventRequest dateEnd(OffsetDateTime dateEnd) {
    this.dateEnd = dateEnd;
    return this;
  }

  /**
   * Дата и время окончания
   * @return dateEnd
   */
  @NotNull @Valid 
  @Schema(name = "dateEnd", description = "Дата и время окончания", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dateEnd")
  public OffsetDateTime getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(OffsetDateTime dateEnd) {
    this.dateEnd = dateEnd;
  }

  public CreateIndividualEventRequest volunteersRequired(Integer volunteersRequired) {
    this.volunteersRequired = volunteersRequired;
    return this;
  }

  /**
   * Требуемое количество волонтеров
   * minimum: 1
   * @return volunteersRequired
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "volunteersRequired", description = "Требуемое количество волонтеров", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("volunteersRequired")
  public Integer getVolunteersRequired() {
    return volunteersRequired;
  }

  public void setVolunteersRequired(Integer volunteersRequired) {
    this.volunteersRequired = volunteersRequired;
  }

  public CreateIndividualEventRequest ageRestriction(Integer ageRestriction) {
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
    CreateIndividualEventRequest createIndividualEventRequest = (CreateIndividualEventRequest) o;
    return Objects.equals(this.title, createIndividualEventRequest.title) &&
        Objects.equals(this.description, createIndividualEventRequest.description) &&
        Objects.equals(this.dateStart, createIndividualEventRequest.dateStart) &&
        Objects.equals(this.dateEnd, createIndividualEventRequest.dateEnd) &&
        Objects.equals(this.volunteersRequired, createIndividualEventRequest.volunteersRequired) &&
        equalsNullable(this.ageRestriction, createIndividualEventRequest.ageRestriction);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, dateStart, dateEnd, volunteersRequired, hashCodeNullable(ageRestriction));
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
    sb.append("class CreateIndividualEventRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dateStart: ").append(toIndentedString(dateStart)).append("\n");
    sb.append("    dateEnd: ").append(toIndentedString(dateEnd)).append("\n");
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

