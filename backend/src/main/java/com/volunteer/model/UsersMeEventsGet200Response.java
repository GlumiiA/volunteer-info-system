package com.volunteer.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.volunteer.model.IndividualEvent;
import com.volunteer.model.MassEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * UsersMeEventsGet200Response
 */

@JsonTypeName("_users_me_events_get_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class UsersMeEventsGet200Response {

  @Valid
  private List<@Valid MassEvent> massEvents = new ArrayList<>();

  @Valid
  private List<@Valid IndividualEvent> individualEvents = new ArrayList<>();

  public UsersMeEventsGet200Response massEvents(List<@Valid MassEvent> massEvents) {
    this.massEvents = massEvents;
    return this;
  }

  public UsersMeEventsGet200Response addMassEventsItem(MassEvent massEventsItem) {
    if (this.massEvents == null) {
      this.massEvents = new ArrayList<>();
    }
    this.massEvents.add(massEventsItem);
    return this;
  }

  /**
   * Get massEvents
   * @return massEvents
   */
  @Valid 
  @Schema(name = "massEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("massEvents")
  public List<@Valid MassEvent> getMassEvents() {
    return massEvents;
  }

  public void setMassEvents(List<@Valid MassEvent> massEvents) {
    this.massEvents = massEvents;
  }

  public UsersMeEventsGet200Response individualEvents(List<@Valid IndividualEvent> individualEvents) {
    this.individualEvents = individualEvents;
    return this;
  }

  public UsersMeEventsGet200Response addIndividualEventsItem(IndividualEvent individualEventsItem) {
    if (this.individualEvents == null) {
      this.individualEvents = new ArrayList<>();
    }
    this.individualEvents.add(individualEventsItem);
    return this;
  }

  /**
   * Get individualEvents
   * @return individualEvents
   */
  @Valid 
  @Schema(name = "individualEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("individualEvents")
  public List<@Valid IndividualEvent> getIndividualEvents() {
    return individualEvents;
  }

  public void setIndividualEvents(List<@Valid IndividualEvent> individualEvents) {
    this.individualEvents = individualEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersMeEventsGet200Response usersMeEventsGet200Response = (UsersMeEventsGet200Response) o;
    return Objects.equals(this.massEvents, usersMeEventsGet200Response.massEvents) &&
        Objects.equals(this.individualEvents, usersMeEventsGet200Response.individualEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(massEvents, individualEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersMeEventsGet200Response {\n");
    sb.append("    massEvents: ").append(toIndentedString(massEvents)).append("\n");
    sb.append("    individualEvents: ").append(toIndentedString(individualEvents)).append("\n");
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

