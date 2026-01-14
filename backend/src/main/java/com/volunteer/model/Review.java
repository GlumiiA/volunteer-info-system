package com.volunteer.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Review
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class Review {

  private @Nullable Integer id;

  private @Nullable Integer reviewerId;

  private @Nullable Integer reviewedId;

  private @Nullable Integer eventId;

  private @Nullable Float rating;

  private @Nullable String content;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public Review id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор отзыва
   * @return id
   */
  
  @Schema(name = "id", description = "Уникальный идентификатор отзыва", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public Review reviewerId(@Nullable Integer reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  /**
   * ID автора отзыва
   * @return reviewerId
   */
  
  @Schema(name = "reviewerId", description = "ID автора отзыва", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewerId")
  public @Nullable Integer getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(@Nullable Integer reviewerId) {
    this.reviewerId = reviewerId;
  }

  public Review reviewedId(@Nullable Integer reviewedId) {
    this.reviewedId = reviewedId;
    return this;
  }

  /**
   * ID пользователя, о котором отзыв
   * @return reviewedId
   */
  
  @Schema(name = "reviewedId", description = "ID пользователя, о котором отзыв", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewedId")
  public @Nullable Integer getReviewedId() {
    return reviewedId;
  }

  public void setReviewedId(@Nullable Integer reviewedId) {
    this.reviewedId = reviewedId;
  }

  public Review eventId(@Nullable Integer eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * ID заявки
   * @return eventId
   */
  
  @Schema(name = "eventId", description = "ID заявки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable Integer getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable Integer eventId) {
    this.eventId = eventId;
  }

  public Review rating(@Nullable Float rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Оценка от 1 до 5
   * @return rating
   */
  
  @Schema(name = "rating", description = "Оценка от 1 до 5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rating")
  public @Nullable Float getRating() {
    return rating;
  }

  public void setRating(@Nullable Float rating) {
    this.rating = rating;
  }

  public Review content(@Nullable String content) {
    this.content = content;
    return this;
  }

  /**
   * Текст отзыва
   * @return content
   */
  
  @Schema(name = "content", description = "Текст отзыва", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable String getContent() {
    return content;
  }

  public void setContent(@Nullable String content) {
    this.content = content;
  }

  public Review createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Дата создания отзыва
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", description = "Дата создания отзыва", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Review review = (Review) o;
    return Objects.equals(this.id, review.id) &&
        Objects.equals(this.reviewerId, review.reviewerId) &&
        Objects.equals(this.reviewedId, review.reviewedId) &&
        Objects.equals(this.eventId, review.eventId) &&
        Objects.equals(this.rating, review.rating) &&
        Objects.equals(this.content, review.content) &&
        Objects.equals(this.createdAt, review.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, reviewerId, reviewedId, eventId, rating, content, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Review {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    reviewedId: ").append(toIndentedString(reviewedId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

