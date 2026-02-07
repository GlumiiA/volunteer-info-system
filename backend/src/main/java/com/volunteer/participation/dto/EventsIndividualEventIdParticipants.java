package com.volunteer.participation.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * EventsIndividualEventIdParticipantsUserIdReviewPostRequest
 */

@JsonTypeName("_events_individual__eventId__participants__userId__review_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class EventsIndividualEventIdParticipants {

  private Float rating;

  private String content;

  public EventsIndividualEventIdParticipants() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EventsIndividualEventIdParticipants(Float rating, String content) {
    this.rating = rating;
    this.content = content;
  }

  public EventsIndividualEventIdParticipants rating(Float rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Оценка от 1 до 5
   * minimum: 1
   * maximum: 5
   * @return rating
   */
  @NotNull @DecimalMin(value = "1") @DecimalMax(value = "5") 
  @Schema(name = "rating", description = "Оценка от 1 до 5", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rating")
  public Float getRating() {
    return rating;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public EventsIndividualEventIdParticipants content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Текст отзыва
   * @return content
   */
  @NotNull 
  @Schema(name = "content", description = "Текст отзыва", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventsIndividualEventIdParticipants eventsIndividualEventIdParticipantsUserIdReviewPostRequest = (EventsIndividualEventIdParticipants) o;
    return Objects.equals(this.rating, eventsIndividualEventIdParticipantsUserIdReviewPostRequest.rating) &&
        Objects.equals(this.content, eventsIndividualEventIdParticipantsUserIdReviewPostRequest.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rating, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventsIndividualEventIdParticipantsUserIdReviewPostRequest {\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

