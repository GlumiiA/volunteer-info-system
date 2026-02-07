package com.volunteer.user.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

import com.volunteer.entity.VolunteerBookEntry;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * UsersMeVolunteerBookGet200Response
 */

@JsonTypeName("_users_me_volunteer_book_get_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class UsersMeVolunteerBook {

  @Valid
  private List<@Valid VolunteerBookEntry> entries = new ArrayList<>();

  private @Nullable Float totalHours;

  private @Nullable Integer totalEvents;

  public UsersMeVolunteerBook entries(List<@Valid VolunteerBookEntry> entries) {
    this.entries = entries;
    return this;
  }

  public UsersMeVolunteerBook addEntriesItem(VolunteerBookEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Список записей в книжке
   * @return entries
   */
  @Valid 
  @Schema(name = "entries", description = "Список записей в книжке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entries")
  public List<@Valid VolunteerBookEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid VolunteerBookEntry> entries) {
    this.entries = entries;
  }

  public UsersMeVolunteerBook totalHours(@Nullable Float totalHours) {
    this.totalHours = totalHours;
    return this;
  }

  /**
   * Общее количество отработанных часов
   * @return totalHours
   */
  
  @Schema(name = "totalHours", description = "Общее количество отработанных часов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalHours")
  public @Nullable Float getTotalHours() {
    return totalHours;
  }

  public void setTotalHours(@Nullable Float totalHours) {
    this.totalHours = totalHours;
  }

  public UsersMeVolunteerBook totalEvents(@Nullable Integer totalEvents) {
    this.totalEvents = totalEvents;
    return this;
  }

  /**
   * Общее количество завершенных мероприятий
   * @return totalEvents
   */
  
  @Schema(name = "totalEvents", description = "Общее количество завершенных мероприятий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalEvents")
  public @Nullable Integer getTotalEvents() {
    return totalEvents;
  }

  public void setTotalEvents(@Nullable Integer totalEvents) {
    this.totalEvents = totalEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersMeVolunteerBook usersMeVolunteerBookGet200Response = (UsersMeVolunteerBook) o;
    return Objects.equals(this.entries, usersMeVolunteerBookGet200Response.entries) &&
        Objects.equals(this.totalHours, usersMeVolunteerBookGet200Response.totalHours) &&
        Objects.equals(this.totalEvents, usersMeVolunteerBookGet200Response.totalEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, totalHours, totalEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersMeVolunteerBookGet200Response {\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    totalHours: ").append(toIndentedString(totalHours)).append("\n");
    sb.append("    totalEvents: ").append(toIndentedString(totalEvents)).append("\n");
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

