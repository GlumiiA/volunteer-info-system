package com.volunteer.participation.service;

import com.volunteer.auth.repository.UserRepository;
import com.volunteer.auth.service.CurrentUserService;
import com.volunteer.entity.*;
import com.volunteer.event.service.EventService;
import com.volunteer.participation.dto.EventsIndividualEventIdParticipants;
import com.volunteer.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final EventService eventService;
    private final ReviewRepository reviewRepository;
    private final MassEventRepository massEventRepository;
    private final IndividualEventRepository individualEventRepository;

    public ParticipationService(
            ParticipationRepository participationRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService,
            EventService eventService,
            ReviewRepository reviewRepository,
            MassEventRepository massEventRepository,
            IndividualEventRepository individualEventRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.eventService = eventService;
        this.reviewRepository = reviewRepository;
        this.massEventRepository = massEventRepository;
        this.individualEventRepository = individualEventRepository;
    }

    @Transactional
    public void acceptMassEventRequest(Integer eventId, Integer requestId) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = massEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Mass event not found"));

        // Check permissions
        if (user.getRole() != UserRole.ADMIN &&
            (user.getRole() != UserRole.ORG_REPRESENTATIVE || !event.getOrganisationId().equals(user.getOrganisationId()))) {
            throw new RuntimeException("Only admins or organization representatives can accept requests");
        }

        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Participation request not found"));

        if (!participation.getEventId().equals(eventId) || 
            participation.getEventType() != Participation.EventType.MASS) {
            throw new RuntimeException("Request does not match event");
        }

        // Check if limit is reached
        long acceptedCount = participationRepository.countByEventIdAndEventTypeAndStatus(
                eventId, Participation.EventType.MASS, Participation.ParticipationStatus.ACCEPTED);
        if (acceptedCount >= event.getVolunteersRequired()) {
            throw new RuntimeException("Event has reached the maximum number of volunteers");
        }

        participation.setStatus(Participation.ParticipationStatus.ACCEPTED);
        participationRepository.save(participation);
    }

    @Transactional
    public void rejectMassEventRequest(Integer eventId, Integer requestId) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = massEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Mass event not found"));

        // Check permissions
        if (user.getRole() != UserRole.ADMIN &&
            (user.getRole() != UserRole.ORG_REPRESENTATIVE || !event.getOrganisationId().equals(user.getOrganisationId()))) {
            throw new RuntimeException("Only admins or organization representatives can reject requests");
        }

        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Participation request not found"));

        if (!participation.getEventId().equals(eventId) || 
            participation.getEventType() != Participation.EventType.MASS) {
            throw new RuntimeException("Request does not match event");
        }

        participation.setStatus(Participation.ParticipationStatus.REJECTED);
        participationRepository.save(participation);
    }

    @Transactional
    public void acceptIndividualEventRequest(Integer eventId, Integer requestId) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = individualEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Individual event not found"));

        // Check permissions - only event creator or org representative can accept
        Integer creatorUserId = event.getCreatorUserId();
        Integer eventOrgId = event.getOrganisationId();
        
        boolean isCreator = creatorUserId != null && creatorUserId.equals(user.getId());
        boolean isOrgRepForEvent = eventOrgId != null && 
                                   user.getOrganisationId() != null && 
                                   eventOrgId.equals(user.getOrganisationId());
        
        if (!isCreator && !isOrgRepForEvent && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only the event creator can accept requests");
        }

        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Participation request not found"));

        if (!participation.getEventId().equals(eventId) || 
            participation.getEventType() != Participation.EventType.INDIVIDUAL) {
            throw new RuntimeException("Request does not match event");
        }

        // Check if limit is reached
        long acceptedCount = participationRepository.countByEventIdAndEventTypeAndStatus(
                eventId, Participation.EventType.INDIVIDUAL, Participation.ParticipationStatus.ACCEPTED);
        if (acceptedCount >= event.getVolunteersRequired()) {
            throw new RuntimeException("Event has reached the maximum number of volunteers");
        }

        participation.setStatus(Participation.ParticipationStatus.ACCEPTED);
        participationRepository.save(participation);
    }

    @Transactional
    public void rejectIndividualEventRequest(Integer eventId, Integer requestId) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = individualEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Individual event not found"));

        // Check permissions - only event creator or org representative can reject
        Integer creatorUserId = event.getCreatorUserId();
        Integer eventOrgId = event.getOrganisationId();
        
        boolean isCreator = creatorUserId != null && creatorUserId.equals(user.getId());
        boolean isOrgRepForEvent = eventOrgId != null && 
                                   user.getOrganisationId() != null && 
                                   eventOrgId.equals(user.getOrganisationId());
        
        if (!isCreator && !isOrgRepForEvent && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only the event creator can reject requests");
        }

        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Participation request not found"));

        if (!participation.getEventId().equals(eventId) || 
            participation.getEventType() != Participation.EventType.INDIVIDUAL) {
            throw new RuntimeException("Request does not match event");
        }

        participation.setStatus(Participation.ParticipationStatus.REJECTED);
        participationRepository.save(participation);
    }

    @Transactional
    public void createReview(Integer eventId, Integer userId, EventsIndividualEventIdParticipants reviewRequest) {
        User reviewer = currentUserService.getCurrentUser();
        IndividualEvent event = individualEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Individual event not found"));

        // Check permissions - only event creator can review
        Integer creatorId = event.getOrganisationId();
        if (creatorId == null || 
            (!creatorId.equals(reviewer.getId()) && 
             (reviewer.getOrganisationId() == null || !creatorId.equals(reviewer.getOrganisationId())))) {
            throw new RuntimeException("Only the event creator can create reviews");
        }

        // Check if event is completed (dateEnd has passed)
        if (event.getDateEnd() != null && event.getDateEnd().isAfter(OffsetDateTime.now())) {
            throw new RuntimeException("Cannot create review before event is completed");
        }

        // Check if user was a participant
        Participation participation = participationRepository
                .findByEventIdAndEventTypeAndUserId(eventId, Participation.EventType.INDIVIDUAL, userId)
                .orElseThrow(() -> new RuntimeException("User was not a participant in this event"));

        if (participation.getStatus() != Participation.ParticipationStatus.ACCEPTED) {
            throw new RuntimeException("Can only review accepted participants");
        }

        // Check if review already exists
        if (!reviewRepository.findByReviewerIdAndReviewedIdAndEventId(
                reviewer.getId(), userId, eventId).isEmpty()) {
            throw new RuntimeException("Review already exists for this participant");
        }

        Review review = Review.builder()
                .reviewerId(reviewer.getId())
                .reviewedId(userId)
                .eventId(eventId)
                .rating(reviewRequest.getRating())
                .content(reviewRequest.getContent())
                .createdAt(OffsetDateTime.now())
                .build();

        reviewRepository.save(review);

        // Update reviewed user's rating
        updateUserRating(userId);
    }

    private void updateUserRating(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        java.util.List<Review> reviews = reviewRepository.findByReviewedId(userId);
        if (reviews.isEmpty()) {
            user.setRating(null);
        } else {
            double averageRating = reviews.stream()
                    .mapToDouble(r -> r.getRating() != null ? r.getRating() : 0.0)
                    .average()
                    .orElse(0.0);
            user.setRating((float) averageRating);
        }
        
        userRepository.save(user);
    }
}
