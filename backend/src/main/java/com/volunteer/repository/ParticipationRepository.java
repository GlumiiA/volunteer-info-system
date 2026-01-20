package com.volunteer.repository;

import com.volunteer.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    List<Participation> findByEventIdAndEventType(Integer eventId, Participation.EventType eventType);
    List<Participation> findByUserId(Integer userId);
    List<Participation> findByEventIdAndEventTypeAndStatus(Integer eventId, Participation.EventType eventType, Participation.ParticipationStatus status);
    Optional<Participation> findByEventIdAndEventTypeAndUserId(Integer eventId, Participation.EventType eventType, Integer userId);
    long countByEventIdAndEventTypeAndStatus(Integer eventId, Participation.EventType eventType, Participation.ParticipationStatus status);
}
