package com.volunteer.event.service;

import com.volunteer.auth.repository.UserRepository;
import com.volunteer.auth.service.CurrentUserService;
import com.volunteer.entity.*;
import com.volunteer.event.dto.CreateIndividualEventRequest;
import com.volunteer.event.dto.CreateMassEventRequest;
import com.volunteer.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final MassEventRepository massEventRepository;
    private final IndividualEventRepository individualEventRepository;
    private final OrganizationRepository organizationRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    public EventService(
            MassEventRepository massEventRepository,
            IndividualEventRepository individualEventRepository,
            OrganizationRepository organizationRepository,
            CurrentUserService currentUserService,
            UserRepository userRepository,
            ParticipationRepository participationRepository) {
        this.massEventRepository = massEventRepository;
        this.individualEventRepository = individualEventRepository;
        this.organizationRepository = organizationRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
    }

    public List<MassEvent> getMassEvents(String title, LocalDate dateFrom, LocalDate dateTo, String address, Integer organisationId) {
        List<MassEvent> allEvents = massEventRepository.findAll();
        
        return allEvents.stream()
                .filter(event -> title == null || title.isEmpty() || 
                        event.getTitle() != null && event.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(event -> dateFrom == null || 
                        event.getDateStart() != null && event.getDateStart().toLocalDate().isAfter(dateFrom.minusDays(1)))
                .filter(event -> dateTo == null || 
                        event.getDateEnd() != null && event.getDateEnd().toLocalDate().isBefore(dateTo.plusDays(1)))
                .filter(event -> address == null || address.isEmpty() || 
                        event.getAddress() != null && event.getAddress().toLowerCase().contains(address.toLowerCase()))
                .filter(event -> organisationId == null || 
                        event.getOrganisationId() != null && event.getOrganisationId().equals(organisationId))
                .collect(Collectors.toList());
    }

    @Transactional
    public MassEvent createMassEvent(CreateMassEventRequest request) {
        User user = currentUserService.getCurrentUser();
        if (user.getRole() != UserRole.ORG_REPRESENTATIVE || user.getOrganisationId() == null) {
            throw new RuntimeException("Only organization representatives can create mass events");
        }

        MassEvent event = new MassEvent();
        event.setOrganisationId(user.getOrganisationId());
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setWorkHours(request.getWorkHours());
        event.setAddress(request.getAddress());
        event.setVolunteersRequired(request.getVolunteersRequired());
        event.setAgeRestriction(request.getAgeRestriction() != null && request.getAgeRestriction().isPresent() 
                ? request.getAgeRestriction().get() : null);

        return massEventRepository.save(event);
    }

    public MassEvent getMassEvent(Integer id) {
        return massEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mass event not found"));
    }

    @Transactional
    public MassEvent updateMassEvent(Integer id, CreateMassEventRequest request) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = getMassEvent(id);

        if (user.getRole() != UserRole.ORG_REPRESENTATIVE || 
            !event.getOrganisationId().equals(user.getOrganisationId())) {
            throw new RuntimeException("Only organization representatives of the event's organization can update it");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setWorkHours(request.getWorkHours());
        event.setAddress(request.getAddress());
        event.setVolunteersRequired(request.getVolunteersRequired());
        event.setAgeRestriction(request.getAgeRestriction() != null && request.getAgeRestriction().isPresent() 
                ? request.getAgeRestriction().get() : null);

        return massEventRepository.save(event);
    }

    @Transactional
    public void deleteMassEvent(Integer id) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = getMassEvent(id);

        if (user.getRole() != UserRole.ADMIN) {
            if (user.getRole() != UserRole.ORG_REPRESENTATIVE || 
                !event.getOrganisationId().equals(user.getOrganisationId())) {
                throw new RuntimeException("Only admins or organization representatives can delete mass events");
            }
        }

        massEventRepository.delete(event);
    }

    @Transactional
    public void participateInMassEvent(Integer eventId) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = getMassEvent(eventId);

        // Check if already participating
        participationRepository.findByEventIdAndEventTypeAndUserId(eventId, Participation.EventType.MASS, user.getId())
                .ifPresent(p -> {
                    throw new RuntimeException("User already participating in this event");
                });

        Participation participation = new Participation();
        participation.setEventId(eventId);
        participation.setEventType(Participation.EventType.MASS);
        participation.setUserId(user.getId());
        participation.setStatus(Participation.ParticipationStatus.PENDING);
        participation.setApplicationDate(OffsetDateTime.now());

        participationRepository.save(participation);
    }

    public List<IndividualEvent> getIndividualEvents(String title, LocalDate dateFrom, LocalDate dateTo, Integer organisationId) {
        List<IndividualEvent> allEvents = individualEventRepository.findAll();
        
        return allEvents.stream()
                .filter(event -> title == null || title.isEmpty() || 
                        event.getTitle() != null && event.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(event -> dateFrom == null || 
                        event.getDateStart() != null && event.getDateStart().toLocalDate().isAfter(dateFrom.minusDays(1)))
                .filter(event -> dateTo == null || 
                        event.getDateEnd() != null && event.getDateEnd().toLocalDate().isBefore(dateTo.plusDays(1)))
                .filter(event -> organisationId == null || 
                        event.getOrganisationId() != null && event.getOrganisationId().equals(organisationId))
                .collect(Collectors.toList());
    }

    @Transactional
    public IndividualEvent createIndividualEvent(CreateIndividualEventRequest request) {
        User user = currentUserService.getCurrentUser();

        IndividualEvent event = new IndividualEvent();
        // For individual events, organisationId stores the organization ID for org representatives,
        // or NULL for regular users (who don't belong to an organization)
        event.setOrganisationId(user.getOrganisationId());
        // Always track the creator's user ID
        event.setCreatorUserId(user.getId());
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setVolunteersRequired(request.getVolunteersRequired());
        event.setAgeRestriction(request.getAgeRestriction() != null && request.getAgeRestriction().isPresent() 
                ? request.getAgeRestriction().get() : null);

        return individualEventRepository.save(event);
    }

    public IndividualEvent getIndividualEvent(Integer id) {
        return individualEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Individual event not found"));
    }

    @Transactional
    public IndividualEvent updateIndividualEvent(Integer id, CreateIndividualEventRequest request) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = getIndividualEvent(id);

        // Check if user is the creator
        Integer creatorUserId = event.getCreatorUserId();
        Integer eventOrgId = event.getOrganisationId();
        
        // User can update if: they are the creator, OR (for org reps) they belong to the event's organization
        boolean isCreator = creatorUserId != null && creatorUserId.equals(user.getId());
        boolean isOrgRepForEvent = eventOrgId != null && 
                                   user.getOrganisationId() != null && 
                                   eventOrgId.equals(user.getOrganisationId());
        
        if (!isCreator && !isOrgRepForEvent) {
            throw new RuntimeException("Only the event creator can update it");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setVolunteersRequired(request.getVolunteersRequired());
        event.setAgeRestriction(request.getAgeRestriction() != null && request.getAgeRestriction().isPresent() 
                ? request.getAgeRestriction().get() : null);

        return individualEventRepository.save(event);
    }

    @Transactional
    public void deleteIndividualEvent(Integer id) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = getIndividualEvent(id);

        if (user.getRole() != UserRole.ADMIN) {
            Integer creatorUserId = event.getCreatorUserId();
            Integer eventOrgId = event.getOrganisationId();
            
            boolean isCreator = creatorUserId != null && creatorUserId.equals(user.getId());
            boolean isOrgRepForEvent = eventOrgId != null && 
                                       user.getOrganisationId() != null && 
                                       eventOrgId.equals(user.getOrganisationId());
            
            if (!isCreator && !isOrgRepForEvent) {
                throw new RuntimeException("Only admins or event creators can delete individual events");
            }
        }

        individualEventRepository.delete(event);
    }

    @Transactional
    public void participateInIndividualEvent(Integer eventId) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = getIndividualEvent(eventId);

        // Check if already participating
        participationRepository.findByEventIdAndEventTypeAndUserId(eventId, Participation.EventType.INDIVIDUAL, user.getId())
                .ifPresent(p -> {
                    throw new RuntimeException("User already participating in this event");
                });

        Participation participation = new Participation();
        participation.setEventId(eventId);
        participation.setEventType(Participation.EventType.INDIVIDUAL);
        participation.setUserId(user.getId());
        participation.setStatus(Participation.ParticipationStatus.PENDING);
        participation.setApplicationDate(OffsetDateTime.now());

        participationRepository.save(participation);
    }

    public Map<String, Object> getMassEventParticipants(Integer eventId) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = getMassEvent(eventId);

        if (user.getRole() != UserRole.ADMIN && 
            (user.getRole() != UserRole.ORG_REPRESENTATIVE || !event.getOrganisationId().equals(user.getOrganisationId()))) {
            throw new RuntimeException("Only admins or organization representatives can view participants");
        }

        List<Participation> participations = participationRepository.findByEventIdAndEventTypeAndStatus(
                eventId, Participation.EventType.MASS, Participation.ParticipationStatus.ACCEPTED);
        
        List<User> participants = participations.stream()
                .map(p -> userRepository.findById(p.getUserId()).orElse(null))
                .filter(u -> u != null)
                .collect(Collectors.toList());

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("participants", participants);
        result.put("totalCount", participants.size());
        return result;
    }

    public Map<String, Object> getIndividualEventParticipants(Integer eventId) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = getIndividualEvent(eventId);

        Integer creatorUserId = event.getCreatorUserId();
        Integer eventOrgId = event.getOrganisationId();
        
        boolean isCreator = creatorUserId != null && creatorUserId.equals(user.getId());
        boolean isOrgRepForEvent = eventOrgId != null && 
                                   user.getOrganisationId() != null && 
                                   eventOrgId.equals(user.getOrganisationId());
        
        if (!isCreator && !isOrgRepForEvent) {
            throw new RuntimeException("Only the event creator can view participants");
        }

        List<Participation> participations = participationRepository.findByEventIdAndEventTypeAndStatus(
                eventId, Participation.EventType.INDIVIDUAL, Participation.ParticipationStatus.ACCEPTED);
        
        List<User> participants = participations.stream()
                .map(p -> userRepository.findById(p.getUserId()).orElse(null))
                .filter(u -> u != null)
                .collect(Collectors.toList());

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("participants", participants);
        result.put("totalCount", participants.size());
        return result;
    }

    public List<Map<String, Object>> getMassEventRequests(Integer eventId) {
        User user = currentUserService.getCurrentUser();
        MassEvent event = getMassEvent(eventId);

        if (user.getRole() != UserRole.ADMIN && 
            (user.getRole() != UserRole.ORG_REPRESENTATIVE || !event.getOrganisationId().equals(user.getOrganisationId()))) {
            throw new RuntimeException("Only admins or organization representatives can view requests");
        }

        List<Participation> participations = participationRepository.findByEventIdAndEventType(
                eventId, Participation.EventType.MASS);

        return participations.stream().map(p -> {
            Map<String, Object> request = new java.util.HashMap<>();
            request.put("requestId", p.getId());
            userRepository.findById(p.getUserId()).ifPresent(u -> request.put("user", u));
            request.put("applicationDate", p.getApplicationDate());
            request.put("status", p.getStatus().name());
            return request;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getIndividualEventRequests(Integer eventId) {
        User user = currentUserService.getCurrentUser();
        IndividualEvent event = getIndividualEvent(eventId);

        Integer creatorUserId = event.getCreatorUserId();
        Integer eventOrgId = event.getOrganisationId();
        
        boolean isCreator = creatorUserId != null && creatorUserId.equals(user.getId());
        boolean isOrgRepForEvent = eventOrgId != null && 
                                   user.getOrganisationId() != null && 
                                   eventOrgId.equals(user.getOrganisationId());
        
        if (!isCreator && !isOrgRepForEvent) {
            throw new RuntimeException("Only the event creator can view requests");
        }

        List<Participation> participations = participationRepository.findByEventIdAndEventType(
                eventId, Participation.EventType.INDIVIDUAL);

        return participations.stream().map(p -> {
            Map<String, Object> request = new java.util.HashMap<>();
            request.put("requestId", p.getId());
            userRepository.findById(p.getUserId()).ifPresent(u -> request.put("user", u));
            request.put("applicationDate", p.getApplicationDate());
            request.put("status", p.getStatus().name());
            return request;
        }).collect(Collectors.toList());
    }
}
