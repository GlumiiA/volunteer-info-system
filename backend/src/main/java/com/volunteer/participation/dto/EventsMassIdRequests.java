package com.volunteer.participation.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.volunteer.entity.User;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * EventsMassIdRequestsGet200ResponseInner
 */

@JsonTypeName("_events_mass__id__requests_get_200_response_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class EventsMassIdRequests {

  private @Nullable Integer requestId;

  private @Nullable User user;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime applicationDate;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    ACCEPTED("ACCEPTED"),
    
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

  public EventsMassIdRequests requestId(@Nullable Integer requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * @return requestId
   */
  
  @Schema(name = "requestId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestId")
  public @Nullable Integer getRequestId() {
    return requestId;
  }

  public void setRequestId(@Nullable Integer requestId) {
    this.requestId = requestId;
  }

  public EventsMassIdRequests user(@Nullable User user) {
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

  public EventsMassIdRequests applicationDate(@Nullable OffsetDateTime applicationDate) {
    this.applicationDate = applicationDate;
    return this;
  }

  /**
   * Get applicationDate
   * @return applicationDate
   */
  @Valid 
  @Schema(name = "applicationDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applicationDate")
  public @Nullable OffsetDateTime getApplicationDate() {
    return applicationDate;
  }

  public void setApplicationDate(@Nullable OffsetDateTime applicationDate) {
    this.applicationDate = applicationDate;
  }

  public EventsMassIdRequests status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventsMassIdRequests eventsMassIdRequestsGet200ResponseInner = (EventsMassIdRequests) o;
    return Objects.equals(this.requestId, eventsMassIdRequestsGet200ResponseInner.requestId) &&
        Objects.equals(this.user, eventsMassIdRequestsGet200ResponseInner.user) &&
        Objects.equals(this.applicationDate, eventsMassIdRequestsGet200ResponseInner.applicationDate) &&
        Objects.equals(this.status, eventsMassIdRequestsGet200ResponseInner.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestId, user, applicationDate, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventsMassIdRequestsGet200ResponseInner {\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    applicationDate: ").append(toIndentedString(applicationDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

