package com.volunteer.participation.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.volunteer.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * EventsMassIdParticipantsGet200Response
 */

@JsonTypeName("_events_mass__id__participants_get_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class EventsMassIdParticipants {

  @Valid
  private List<@Valid User> participants = new ArrayList<>();

  private @Nullable Integer totalCount;

  public EventsMassIdParticipants participants(List<@Valid User> participants) {
    this.participants = participants;
    return this;
  }

  public EventsMassIdParticipants addParticipantsItem(User participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<@Valid User> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid User> participants) {
    this.participants = participants;
  }

  public EventsMassIdParticipants totalCount(@Nullable Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  /**
   * Общее количество участников
   * @return totalCount
   */
  
  @Schema(name = "totalCount", description = "Общее количество участников", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalCount")
  public @Nullable Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(@Nullable Integer totalCount) {
    this.totalCount = totalCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventsMassIdParticipants eventsMassIdParticipantsGet200Response = (EventsMassIdParticipants) o;
    return Objects.equals(this.participants, eventsMassIdParticipantsGet200Response.participants) &&
        Objects.equals(this.totalCount, eventsMassIdParticipantsGet200Response.totalCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participants, totalCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventsMassIdParticipantsGet200Response {\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
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

