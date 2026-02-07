package com.volunteer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mass_events")
public class MassEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "organisation_id", nullable = false)
  private Integer organisationId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(name = "date_start")
  private OffsetDateTime dateStart;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(name = "date_end")
  private OffsetDateTime dateEnd;

  @Column(name = "work_hours")
  private Float workHours;

  @Column(name = "address")
  private String address;

  /*
   * Количество волонтеров, необходимых для мероприятия
   */
  @Column(name = "volunteers_required")
  private Integer volunteersRequired;

  @Column(name = "age_restriction")
  private Integer ageRestriction;
}