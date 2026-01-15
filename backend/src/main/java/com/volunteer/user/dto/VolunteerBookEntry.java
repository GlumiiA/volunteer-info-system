package com.volunteer.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.volunteer.dto.MassEvent;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VolunteerBookEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class VolunteerBookEntry {

  private @Nullable Integer id;

  private @Nullable Integer userId;

  private @Nullable Integer eventId;

  private @Nullable String content;

  private @Nullable MassEvent event;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  public VolunteerBookEntry id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор записи
   * @return id
   */
  
  @Schema(name = "id", description = "Уникальный идентификатор записи", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public VolunteerBookEntry userId(@Nullable Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * ID волонтера
   * @return userId
   */
  
  @Schema(name = "userId", description = "ID волонтера", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public @Nullable Integer getUserId() {
    return userId;
  }

  public void setUserId(@Nullable Integer userId) {
    this.userId = userId;
  }

  public VolunteerBookEntry eventId(@Nullable Integer eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * ID массового мероприятия
   * @return eventId
   */
  
  @Schema(name = "eventId", description = "ID массового мероприятия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable Integer getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable Integer eventId) {
    this.eventId = eventId;
  }

  public VolunteerBookEntry content(@Nullable String content) {
    this.content = content;
    return this;
  }

  /**
   * Описание записи/комментарий
   * @return content
   */
  
  @Schema(name = "content", description = "Описание записи/комментарий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable String getContent() {
    return content;
  }

  public void setContent(@Nullable String content) {
    this.content = content;
  }

  public VolunteerBookEntry event(@Nullable MassEvent event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable MassEvent getEvent() {
    return event;
  }

  public void setEvent(@Nullable MassEvent event) {
    this.event = event;
  }

  public VolunteerBookEntry completedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
    return this;
  }

  /**
   * Дата завершения участия
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completedAt", description = "Дата завершения участия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completedAt")
  public @Nullable OffsetDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VolunteerBookEntry volunteerBookEntry = (VolunteerBookEntry) o;
    return Objects.equals(this.id, volunteerBookEntry.id) &&
        Objects.equals(this.userId, volunteerBookEntry.userId) &&
        Objects.equals(this.eventId, volunteerBookEntry.eventId) &&
        Objects.equals(this.content, volunteerBookEntry.content) &&
        Objects.equals(this.event, volunteerBookEntry.event) &&
        Objects.equals(this.completedAt, volunteerBookEntry.completedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, eventId, content, event, completedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VolunteerBookEntry {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
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

