package com.volunteer.user.service;

import com.volunteer.auth.repository.UserRepository;
import com.volunteer.auth.service.CurrentUserService;
import com.volunteer.entity.*;
import com.volunteer.repository.*;
import com.volunteer.user.dto.UpdateUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final MassEventRepository massEventRepository;
    private final IndividualEventRepository individualEventRepository;
    private final ReviewRepository reviewRepository;
    private final VolunteerBookEntryRepository volunteerBookEntryRepository;
    private final OrganizationRoleRequestRepository organizationRoleRequestRepository;
    private final OrganizationRepository organizationRepository;

    public UserService(
            UserRepository userRepository,
            CurrentUserService currentUserService,
            MassEventRepository massEventRepository,
            IndividualEventRepository individualEventRepository,
            ReviewRepository reviewRepository,
            VolunteerBookEntryRepository volunteerBookEntryRepository,
            OrganizationRoleRequestRepository organizationRoleRequestRepository,
            OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.massEventRepository = massEventRepository;
        this.individualEventRepository = individualEventRepository;
        this.reviewRepository = reviewRepository;
        this.volunteerBookEntryRepository = volunteerBookEntryRepository;
        this.organizationRoleRequestRepository = organizationRoleRequestRepository;
        this.organizationRepository = organizationRepository;
    }

    public User getCurrentUser() {
        return currentUserService.getCurrentUser();
    }

    @Transactional
    public User updateCurrentUser(UpdateUserRequest request) {
        User user = getCurrentUser();

        if (request.getName() != null) {
            user.setUsername(request.getName());
        }

        if (request.getDescription() != null && request.getDescription().isPresent()) {
            user.setDescription(request.getDescription().get());
        }

        if (request.getBirthday() != null && request.getBirthday().isPresent()) {
            user.setBirthday(request.getBirthday().get());
        }

        if (request.getLocation() != null && request.getLocation().isPresent()) {
            user.setLocation(request.getLocation().get());
        }

        return userRepository.save(user);
    }

    public String uploadAvatar(MultipartFile file) {
        // Simple implementation - in production, save to file storage and return URL
        // For now, just return a placeholder
        return "/api/v1/avatars/" + currentUserService.getCurrentUserId() + ".jpg";
    }

    public com.volunteer.user.dto.UsersMeEvents getUserEvents(String status) {
        User user = getCurrentUser();
        Integer userId = user.getId();
        OffsetDateTime now = OffsetDateTime.now();

        List<MassEvent> massEvents;
        List<IndividualEvent> individualEvents;

        if ("active".equals(status)) {
            // Mass events: events from user's organization (if user is org rep)
            massEvents = massEventRepository.findAll().stream()
                    .filter(e -> e.getDateEnd() != null && e.getDateEnd().isAfter(now))
                    .filter(e -> user.getOrganisationId() != null && 
                               e.getOrganisationId() != null && 
                               e.getOrganisationId().equals(user.getOrganisationId()))
                    .toList();

            // Individual events: events created by user OR from user's organization
            individualEvents = individualEventRepository.findAll().stream()
                    .filter(e -> e.getDateEnd() != null && e.getDateEnd().isAfter(now))
                    .filter(e -> {
                        // Event created by user
                        if (e.getCreatorUserId() != null && e.getCreatorUserId().equals(userId)) {
                            return true;
                        }
                        // Event from user's organization (if user is org rep)
                        if (user.getOrganisationId() != null && 
                            e.getOrganisationId() != null && 
                            e.getOrganisationId().equals(user.getOrganisationId())) {
                            return true;
                        }
                        return false;
                    })
                    .toList();
        } else if ("completed".equals(status)) {
            // Mass events: completed events from user's organization
            massEvents = massEventRepository.findAll().stream()
                    .filter(e -> e.getDateEnd() != null && e.getDateEnd().isBefore(now))
                    .filter(e -> user.getOrganisationId() != null && 
                               e.getOrganisationId() != null && 
                               e.getOrganisationId().equals(user.getOrganisationId()))
                    .toList();

            // Individual events: completed events created by user OR from user's organization
            individualEvents = individualEventRepository.findAll().stream()
                    .filter(e -> e.getDateEnd() != null && e.getDateEnd().isBefore(now))
                    .filter(e -> {
                        if (e.getCreatorUserId() != null && e.getCreatorUserId().equals(userId)) {
                            return true;
                        }
                        if (user.getOrganisationId() != null && 
                            e.getOrganisationId() != null && 
                            e.getOrganisationId().equals(user.getOrganisationId())) {
                            return true;
                        }
                        return false;
                    })
                    .toList();
        } else {
            // All events: from user's organization
            massEvents = massEventRepository.findAll().stream()
                    .filter(e -> user.getOrganisationId() != null && 
                               e.getOrganisationId() != null && 
                               e.getOrganisationId().equals(user.getOrganisationId()))
                    .toList();

            // Individual events: all events created by user OR from user's organization
            individualEvents = individualEventRepository.findAll().stream()
                    .filter(e -> {
                        if (e.getCreatorUserId() != null && e.getCreatorUserId().equals(userId)) {
                            return true;
                        }
                        if (user.getOrganisationId() != null && 
                            e.getOrganisationId() != null && 
                            e.getOrganisationId().equals(user.getOrganisationId())) {
                            return true;
                        }
                        return false;
                    })
                    .toList();
        }

        com.volunteer.user.dto.UsersMeEvents result = new com.volunteer.user.dto.UsersMeEvents();
        result.setMassEvents(massEvents);
        result.setIndividualEvents(individualEvents);
        return result;
    }

    public List<Review> getUserReviews() {
        User user = getCurrentUser();
        return reviewRepository.findByReviewedId(user.getId());
    }

    public com.volunteer.user.dto.UsersMeVolunteerBook getUserVolunteerBook() {
        User user = getCurrentUser();
        List<VolunteerBookEntry> entries = volunteerBookEntryRepository.findByUserId(user.getId());

        // Calculate total hours using stream to avoid lambda closure issues
        float totalHours = (float) entries.stream()
                .filter(entry -> entry.getEventId() != null)
                .mapToDouble(entry -> massEventRepository.findById(entry.getEventId())
                        .map(event -> event.getWorkHours() != null ? event.getWorkHours().doubleValue() : 0.0)
                        .orElse(0.0))
                .sum();

        com.volunteer.user.dto.UsersMeVolunteerBook result = new com.volunteer.user.dto.UsersMeVolunteerBook();
        result.setEntries(entries);
        result.setTotalHours(totalHours);
        result.setTotalEvents(entries.size());
        return result;
    }

    @Transactional
    public void createOrganizationRoleRequest(com.volunteer.organization.dto.OrganizationRoleRequest request) {
        User user = getCurrentUser();

        if (user.getRole() == UserRole.ORG_REPRESENTATIVE) {
            throw new RuntimeException("User is already a representative of an organization");
        }

        // Check for existing pending request
        organizationRoleRequestRepository.findByUserIdAndStatus(user.getId(), OrganizationRoleRequest.RequestStatus.PENDING)
                .ifPresent(r -> {
                    throw new RuntimeException("User already has a pending organization role request");
                });

        com.volunteer.organization.dto.OrganizationRoleRequest.RequestTypeEnum dtoRequestType = request.getRequestType();
        OrganizationRoleRequest.RequestType requestType = OrganizationRoleRequest.RequestType.valueOf(dtoRequestType.name());

        if (requestType == OrganizationRoleRequest.RequestType.EXISTING) {
            if (request.getOrganizationId() == null || !request.getOrganizationId().isPresent()) {
                throw new RuntimeException("Organization ID is required for EXISTING request type");
            }
            organizationRepository.findById(request.getOrganizationId().get())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
        } else if (requestType == OrganizationRoleRequest.RequestType.NEW) {
            if (request.getOrganizationName() == null || !request.getOrganizationName().isPresent() || 
                request.getOrganizationName().get().trim().isEmpty()) {
                throw new RuntimeException("Organization name is required for NEW request type");
            }
        }

        OrganizationRoleRequest orgRequest = new OrganizationRoleRequest();
        orgRequest.setUserId(user.getId());
        orgRequest.setRequestType(requestType);
        orgRequest.setOrganizationId(request.getOrganizationId() != null && request.getOrganizationId().isPresent() ? 
                                     request.getOrganizationId().get() : null);
        orgRequest.setOrganizationName(request.getOrganizationName() != null && request.getOrganizationName().isPresent() ? 
                                      request.getOrganizationName().get() : null);
        orgRequest.setOrganizationDescription(request.getOrganizationDescription() != null && request.getOrganizationDescription().isPresent() ? 
                                             request.getOrganizationDescription().get() : null);
        orgRequest.setOrganizationAddress(request.getOrganizationAddress() != null && request.getOrganizationAddress().isPresent() ? 
                                         request.getOrganizationAddress().get() : null);
        orgRequest.setStatus(OrganizationRoleRequest.RequestStatus.PENDING);
        orgRequest.setCreatedAt(OffsetDateTime.now());

        organizationRoleRequestRepository.save(orgRequest);
    }
}
