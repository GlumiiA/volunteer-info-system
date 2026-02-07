package com.volunteer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * IndividualEvent entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "individual_events")
public class IndividualEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organisation_id")
    private Integer organisationId;

    @Column(name = "creator_user_id")
    private Integer creatorUserId;

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

    @Column(name = "volunteers_required")
    private Integer volunteersRequired;

    @Column(name = "age_restriction")
    private Integer ageRestriction;
}