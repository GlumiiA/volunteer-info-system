package com.volunteer.participation.controller;

import com.volunteer.common.dto.Error;
import com.volunteer.event.service.EventService;
import com.volunteer.participation.dto.EventsIndividualEventIdParticipants;
import com.volunteer.participation.dto.EventsMassIdParticipants;
import com.volunteer.participation.dto.EventsMassIdRequests;
import com.volunteer.participation.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class ParticipationApiController implements ParticipationApi {

    private final NativeWebRequest request;
    private final EventService eventService;
    private final ParticipationService participationService;

    @Autowired
    public ParticipationApiController(
            NativeWebRequest request,
            EventService eventService,
            ParticipationService participationService) {
        this.request = request;
        this.eventService = eventService;
        this.participationService = participationService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    // Participate endpoints

    @PostMapping("/events/mass/{id}/participate")
    public ResponseEntity<Void> eventsMassIdParticipatePost(@PathVariable("id") @NotNull Integer id) {
        try {
            eventService.participateInMassEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("already participating")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/events/individual/{id}/participate")
    public ResponseEntity<Void> eventsIndividualIdParticipatePost(@PathVariable("id") @NotNull Integer id) {
        try {
            eventService.participateInIndividualEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("already participating")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Participants endpoints

    @GetMapping("/events/mass/{id}/participants")
    public ResponseEntity<EventsMassIdParticipants> eventsMassIdParticipantsGet(@PathVariable("id") @NotNull Integer id) {
        try {
            Map<String, Object> result = eventService.getMassEventParticipants(id);
            EventsMassIdParticipants response = new EventsMassIdParticipants();
            response.setParticipants((List) result.get("participants"));
            response.setTotalCount((Integer) result.get("totalCount"));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("representatives") || e.getMessage().contains("admins")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/events/individual/{id}/participants")
    public ResponseEntity<EventsMassIdParticipants> eventsIndividualIdParticipantsGet(@PathVariable("id") @NotNull Integer id) {
        try {
            Map<String, Object> result = eventService.getIndividualEventParticipants(id);
            EventsMassIdParticipants response = new EventsMassIdParticipants();
            response.setParticipants((List) result.get("participants"));
            response.setTotalCount((Integer) result.get("totalCount"));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("creator") || e.getMessage().contains("author")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Requests endpoints

    @GetMapping("/events/mass/{id}/requests")
    public ResponseEntity<List<EventsMassIdRequests>> eventsMassIdRequestsGet(@PathVariable("id") @NotNull Integer id) {
        try {
            List<Map<String, Object>> requests = eventService.getMassEventRequests(id);
            List<EventsMassIdRequests> response = requests.stream().map(map -> {
                EventsMassIdRequests req = new EventsMassIdRequests();
                req.setRequestId((Integer) map.get("requestId"));
                req.setUser((com.volunteer.entity.User) map.get("user"));
                req.setApplicationDate((java.time.OffsetDateTime) map.get("applicationDate"));
                String statusStr = (String) map.get("status");
                req.setStatus(EventsMassIdRequests.StatusEnum.fromValue(statusStr));
                return req;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("representatives") || e.getMessage().contains("admins")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/events/individual/{id}/requests")
    public ResponseEntity<List<EventsMassIdRequests>> eventsIndividualIdRequestsGet(@PathVariable("id") @NotNull Integer id) {
        try {
            List<Map<String, Object>> requests = eventService.getIndividualEventRequests(id);
            List<EventsMassIdRequests> response = requests.stream().map(map -> {
                EventsMassIdRequests req = new EventsMassIdRequests();
                req.setRequestId((Integer) map.get("requestId"));
                req.setUser((com.volunteer.entity.User) map.get("user"));
                req.setApplicationDate((java.time.OffsetDateTime) map.get("applicationDate"));
                String statusStr = (String) map.get("status");
                req.setStatus(EventsMassIdRequests.StatusEnum.fromValue(statusStr));
                return req;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("creator") || e.getMessage().contains("author")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Accept/Reject endpoints

    @PostMapping("/events/mass/{eventId}/requests/{requestId}/accept")
    public ResponseEntity<Void> eventsMassEventIdRequestsRequestIdAcceptPost(
            @PathVariable("eventId") @NotNull Integer eventId,
            @PathVariable("requestId") @NotNull Integer requestId) {
        try {
            participationService.acceptMassEventRequest(eventId, requestId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("representatives") || e.getMessage().contains("admins") ||
                e.getMessage().contains("forbidden") || e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (e.getMessage().contains("maximum") || e.getMessage().contains("limit") ||
                e.getMessage().contains("reached")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/events/mass/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<Void> eventsMassEventIdRequestsRequestIdRejectPost(
            @PathVariable("eventId") @NotNull Integer eventId,
            @PathVariable("requestId") @NotNull Integer requestId) {
        try {
            participationService.rejectMassEventRequest(eventId, requestId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("representatives") || e.getMessage().contains("admins") ||
                e.getMessage().contains("forbidden") || e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/events/individual/{eventId}/requests/{requestId}/accept")
    public ResponseEntity<Void> eventsIndividualEventIdRequestsRequestIdAcceptPost(
            @PathVariable("eventId") @NotNull Integer eventId,
            @PathVariable("requestId") @NotNull Integer requestId) {
        try {
            participationService.acceptIndividualEventRequest(eventId, requestId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("creator") || e.getMessage().contains("author") ||
                e.getMessage().contains("forbidden") || e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (e.getMessage().contains("maximum") || e.getMessage().contains("limit") ||
                e.getMessage().contains("reached")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/events/individual/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<Void> eventsIndividualEventIdRequestsRequestIdRejectPost(
            @PathVariable("eventId") @NotNull Integer eventId,
            @PathVariable("requestId") @NotNull Integer requestId) {
        try {
            participationService.rejectIndividualEventRequest(eventId, requestId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("creator") || e.getMessage().contains("author") ||
                e.getMessage().contains("forbidden") || e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Review endpoint

    @PostMapping("/events/individual/{eventId}/participants/{userId}/review")
    public ResponseEntity<Void> eventsIndividualEventIdParticipantsUserIdReviewPost(
            @PathVariable("eventId") @NotNull Integer eventId,
            @PathVariable("userId") @NotNull Integer userId,
            @Valid @RequestBody EventsIndividualEventIdParticipants reviewRequest) {
        try {
            participationService.createReview(eventId, userId, reviewRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("creator") || e.getMessage().contains("author") ||
                e.getMessage().contains("forbidden") || e.getMessage().contains("Forbidden") ||
                e.getMessage().contains("completed")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
