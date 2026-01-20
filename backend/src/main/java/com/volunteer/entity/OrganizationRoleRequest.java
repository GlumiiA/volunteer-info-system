package com.volunteer.entity;

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
@Table(name = "organization_role_requests")
public class OrganizationRoleRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "organization_description", columnDefinition = "TEXT")
    private String organizationDescription;

    @Column(name = "organization_address")
    private String organizationAddress;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public enum RequestType {
        EXISTING,
        NEW
    }

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
