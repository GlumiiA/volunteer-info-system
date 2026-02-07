package com.volunteer.user.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.volunteer.entity.IndividualEvent;

import java.util.ArrayList;
import java.util.List;

import com.volunteer.entity.MassEvent;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * UsersMeEventsGet200Response
 */

@JsonTypeName("_users_me_events_get_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class UsersMeEvents {

  @Valid
  private List<@Valid MassEvent> massEvents = new ArrayList<>();

  @Valid
  private List<@Valid IndividualEvent> individualEvents = new ArrayList<>();

  public UsersMeEvents massEvents(List<@Valid MassEvent> massEvents) {
    this.massEvents = massEvents;
    return this;
  }

  public UsersMeEvents addMassEventsItem(MassEvent massEventsItem) {
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

  public UsersMeEvents individualEvents(List<@Valid IndividualEvent> individualEvents) {
    this.individualEvents = individualEvents;
    return this;
  }

  public UsersMeEvents addIndividualEventsItem(IndividualEvent individualEventsItem) {
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
    UsersMeEvents usersMeEvents = (UsersMeEvents) o;
    return Objects.equals(this.massEvents, usersMeEvents.massEvents) &&
        Objects.equals(this.individualEvents, usersMeEvents.individualEvents);
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

