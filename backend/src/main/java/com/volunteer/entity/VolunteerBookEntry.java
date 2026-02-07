package com.volunteer.entity;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * VolunteerBookEntry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "volunteer_book_entries")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class VolunteerBookEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "user_id", nullable = false)
  @NotNull
  private Integer userId;

  @Column(name = "event_id", nullable = false)
  @NotNull
  private Integer eventId;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", insertable = false, updatable = false)
  @Valid
  private MassEvent event;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(name = "completed_at")
  private OffsetDateTime completedAt;
}
