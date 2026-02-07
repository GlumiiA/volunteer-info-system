package com.volunteer.entity;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.annotation.Generated;

/**
 * Review
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  @Schema(description = "Unique identifier of the review", accessMode = Schema.AccessMode.READ_ONLY)
  private Integer id;

  @Column(name = "reviewer_id", nullable = true)
  @Schema(description = "ID of the user who wrote the review")
  private @Nullable Integer reviewerId;

  @Column(name = "reviewed_id", nullable = true)
  @Schema(description = "ID of the user being reviewed")
  private @Nullable Integer reviewedId;

  @Column(name = "event_id", nullable = true)
  @Schema(description = "ID of the event related to the review")
  private @Nullable Integer eventId;

  @Column(name = "rating", nullable = true)
  @Schema(description = "Rating value (typically 1-5)")
  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating must be at most 5")
  private @Nullable Float rating;

  @Column(name = "content", nullable = true, columnDefinition = "TEXT")
  @Schema(description = "Text content of the review")
  @Size(max = 2000, message = "Review content must not exceed 2000 characters")
  private @Nullable String content;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Schema(description = "Timestamp when the review was created", accessMode = Schema.AccessMode.READ_ONLY)
  private @Nullable OffsetDateTime createdAt;

  public Review id(@Nullable Integer id) {
    this.id = id;
    return this;
  }
}
